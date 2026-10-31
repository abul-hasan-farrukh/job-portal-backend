package com.aimbitious.job.service.impl;

import com.aimbitious.job.client.CompanyClient;
import com.aimbitious.job.client.JobClient;
import com.aimbitious.job.client.ResumeClient;
import com.aimbitious.job.client.UserClient;
import com.aimbitious.job.domain.ApplicationStatus;
import com.aimbitious.job.dto.response.*;
import com.aimbitious.job.event.ApplicationEventPublisher;
import com.aimbitious.job.exception.ApplicationException;
import com.aimbitious.job.exception.ResourceNotFoundException;
import com.aimbitious.job.mapper.ApplicationMapper;
import com.aimbitious.job.model.Application;
import com.aimbitious.job.model.ApplicationNote;
import com.aimbitious.job.model.ApplicationScreening;
import com.aimbitious.job.model.ApplicationStatusHistory;
import com.aimbitious.job.payload.CompanyApplicationFilterRequest;
import com.aimbitious.job.payload.CreateApplicationRequest;
import com.aimbitious.job.payload.UpdateApplicationStatusRequest;
import com.aimbitious.job.payload.WithdrawApplicationRequest;
import com.aimbitious.job.repository.*;
import com.aimbitious.job.service.ApplicationScreeningService;
import com.aimbitious.job.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ApplicationScreeningRepository screeningRepository;
    private final ApplicationStatusHistoryRepository historyRepository;
    private final ApplicationNoteRepository applicationNoteRepository;
    private final JobClient jobClient;
    private final ResumeClient resumeClient;
    private final CompanyClient companyClient;
    private final UserClient userClient;
    private final ApplicationScreeningService screeningService;
    private final ApplicationEventPublisher eventPublisher;

    private final ApplicationEventPublisher applicationEventPublisher;

    // ── Create ────────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public ApplicationResponse createApplication(Long candidateId,
                                                 CreateApplicationRequest req)
            throws ApplicationException {
        if (applicationRepository.existsByCandidateIdAndJobId(candidateId, req.getJobId())) {
            throw new ApplicationException("You have already applied for this job");
        }

        // Fetch job to resolve companyId and employerId
        JobResponse job = jobClient.getJobById(req.getJobId());

        // Validate resume belongs to the candidate (security check only)
        ResumeResponse resume = resumeClient.getResumeById(req.getResumeId(), candidateId);
        if (!resume.getCandidateId().equals(candidateId)) {
            throw new ApplicationException("Resume does not belong to you");
        }

        Application application = ApplicationMapper.toEntity(req, candidateId,
                job.getCompany().getId(), job.getEmployerId());

        application = applicationRepository.save(application);

        ApplicationStatusHistory initialHistory = ApplicationStatusHistory.builder()
                .application(application)
                .fromStatus(null)
                .toStatus(ApplicationStatus.PENDING)
                .changedByUserId(candidateId)
                .note("Application submitted")
                .build();
        historyRepository.save(initialHistory);

        // Fire-and-forget — AI screening runs in a background thread, no callback needed
        screeningService.screenAsync(application.getId(), candidateId, req.getJobId(), req.getResumeId());

        return buildFullResponse(application);
    }

    // ── Read ──────────────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public ApplicationResponse getApplicationById(Long id) throws ResourceNotFoundException {
        Application application = getApplicationEntity(id);
        return buildFullResponse(application);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationResponse> getMyApplications(Long candidateId) {
        return applicationRepository.findByCandidateId(candidateId).stream()
                .map(this::buildFullResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationResponse> getApplicationsForJob(Long jobId) {
        return applicationRepository.findByJobId(jobId).stream()
                .map(this::buildFullResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationResponse> getApplicationsForCompany(
            Long userId,
            CompanyApplicationFilterRequest filter
    ) throws ResourceNotFoundException {
        Long companyId = companyClient.getMyCompany(userId).getId();

        LocalDateTime from = filter.getAppliedFrom() != null
                ? filter.getAppliedFrom().atStartOfDay() : null;
        LocalDateTime to = filter.getAppliedTo() != null
                ? filter.getAppliedTo().atTime(LocalTime.MAX) : null;

        Sort sort = buildSort(filter.getSortBy());

        return applicationRepository.findAll(
                        ApplicationSpecification.forCompanyWithFilters(
                                companyId,
                                filter.getJobId(),
                                filter.getStatus(),
                                filter.getSource(),
                                filter.getIsRead(),
                                filter.getIsStarred(),
                                from,
                                to,
                                filter.getAiShortlistStatus(),
                                filter.getMinAiScore()
                        ), sort).stream()
                .map(this::buildFullResponse)
                .collect(Collectors.toList());
    }


// ── Status update ─────────────────────────────────────────────────────────

    @Override
    @Transactional
    public ApplicationResponse updateStatus(Long applicationId, Long employerId,
                                            UpdateApplicationStatusRequest req)
            throws ResourceNotFoundException, ApplicationException {
        Application application = getApplicationEntity(applicationId);
        assertEmployer(application, employerId);

        if (application.getStatus() == ApplicationStatus.WITHDRAWN) {
            throw new ApplicationException("Cannot update status of a withdrawn application");
        }
        if (application.getStatus() == req.getStatus()) {
            throw new ApplicationException("Application is already in status: " + req.getStatus());
        }

        ApplicationStatus oldStatus = application.getStatus();
        application.setStatus(req.getStatus());
        application = applicationRepository.save(application);

        historyRepository.save(ApplicationStatusHistory.builder()
                .application(application)
                .fromStatus(oldStatus)
                .toStatus(req.getStatus())
                .changedByUserId(employerId)
                .note(req.getNote())
                .build());

        eventPublisher.publishStatusChanged(application, oldStatus, req.getNote());

        return buildFullResponse(application);
    }



// ── Withdraw ──────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public ApplicationResponse withdraw(Long applicationId, Long candidateId,
                                        WithdrawApplicationRequest req)
            throws ResourceNotFoundException, ApplicationException {
        Application application = getApplicationEntity(applicationId);
        assertCandidate(application, candidateId);

        if (application.getStatus() == ApplicationStatus.WITHDRAWN) {
            throw new ApplicationException("Application is already withdrawn");
        }
        if (application.getStatus() == ApplicationStatus.HIRED) {
            throw new ApplicationException("Cannot withdraw an accepted offer");
        }

        ApplicationStatus oldStatus = application.getStatus();
        application.setStatus(ApplicationStatus.WITHDRAWN);
        application.setWithdrawnAt(LocalDateTime.now());
        application.setWithdrawnReason(req.getReason());
        application = applicationRepository.save(application);

        historyRepository.save(ApplicationStatusHistory.builder()
                .application(application)
                .fromStatus(oldStatus)
                .toStatus(ApplicationStatus.WITHDRAWN)
                .changedByUserId(candidateId)
                .note(req.getReason())
                .build());

        return buildFullResponse(application);
    }


// ── Tracking flags ────────────────────────────────────────────────────────

    @Override
    @Transactional
    public ApplicationResponse markAsRead(Long applicationId, Long employerId)
            throws ResourceNotFoundException, ApplicationException {
        Application application = getApplicationEntity(applicationId);
        assertEmployer(application, employerId);
        application.setIsRead(true);
        return buildFullResponse(applicationRepository.save(application));
    }

    @Override
    @Transactional
    public ApplicationResponse toggleStar(Long applicationId, Long employerId)
            throws ResourceNotFoundException, ApplicationException {
        Application application = getApplicationEntity(applicationId);
        assertEmployer(application, employerId);
        application.setIsStarred(!application.getIsStarred());
        return buildFullResponse(applicationRepository.save(application));
    }

// ── Delete ────────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public void deleteApplication(Long applicationId, Long candidateId)
            throws ResourceNotFoundException, ApplicationException {
        Application application = getApplicationEntity(applicationId);
        assertCandidate(application, candidateId);
        applicationRepository.delete(application);
    }

// ── Internal ──────────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public Application getApplicationEntity(Long id) throws ResourceNotFoundException {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Application not found with id: " + id));
    }

    @Override
    @Transactional
    public void markScreeningsStaleForJob(Long jobId) {
        List<Long> applicationIds = applicationRepository.findByJobId(jobId)
                .stream().map(Application::getId).collect(Collectors.toList());
        if (applicationIds.isEmpty()) return;
        List<ApplicationScreening> screenings = screeningRepository.findByApplicationIdIn(applicationIds);
        screenings.forEach(s -> s.setIsStale(true));
        screeningRepository.saveAll(screenings);
    }


// ── Private utilities ─────────────────────────────────────────────────────

    private void assertEmployer(Application application, Long employerId) throws ApplicationException {
        if (!application.getEmployerId().equals(employerId)) {
            throw new ApplicationException("You are not the employer for this application");
        }
    }

    private void assertCandidate(Application application, Long candidateId) throws ApplicationException {
        if (!application.getCandidateId().equals(candidateId)) {
            throw new ApplicationException("You are not the owner of this application");
        }
    }

    private Sort buildSort(String sortBy) {
        if ("AI_SCORE_DESC".equals(sortBy)) {
            return Sort.by(Sort.Order.desc("aiScore").with(Sort.NullHandling.NULLS_LAST));
        } else if ("AI_SCORE_ASC".equals(sortBy)) {
            return Sort.by(Sort.Order.asc("aiScore").with(Sort.NullHandling.NULLS_LAST));
        }
        return Sort.by(Sort.Direction.DESC, "appliedAt");
    }

    private ApplicationResponse buildFullResponse(Application application) {
        List<ApplicationStatusHistory> history =
                historyRepository.findByApplicationIdOrderByChangedAtAsc(application.getId());

        List<ApplicationNote> notes =
                applicationNoteRepository.findByApplicationIdOrderByCreatedAtDesc(application.getId());
        JobSummaryResponse job = jobClient.getJobSummaryById(application.getJobId());
        CompanySummaryResponse company = companyClient.getCompanySummaryById(application.getCompanyId());
        UserResponse candidate = userClient.getUserById(application.getCandidateId());
        ApplicationScreening screening = screeningRepository.findByApplicationId(application.getId()).orElse(null);

        return ApplicationMapper.toResponse(application, history, notes, job, company, candidate, screening);
    }
}
