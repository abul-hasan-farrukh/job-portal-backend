package com.aimbitious.job.service.impl;

import com.aimbitious.job.client.CompanyClient;
import com.aimbitious.job.client.JobClient;
import com.aimbitious.job.client.ResumeClient;
import com.aimbitious.job.client.UserClient;
import com.aimbitious.job.domain.ApplicationStatus;
import com.aimbitious.job.dto.ApplicationResponse;
import com.aimbitious.job.dto.CompanyResponse;
import com.aimbitious.job.dto.JobResponse;
import com.aimbitious.job.dto.ResumeResponse;
import com.aimbitious.job.dto.response.UserResponse;
import com.aimbitious.job.event.ApplicationEventPublisher;
import com.aimbitious.job.mapper.ApplicationMapper;
import com.aimbitious.job.model.Application;
import com.aimbitious.job.model.ApplicationNote;
import com.aimbitious.job.payload.CompanyApplicationFilterRequest;
import com.aimbitious.job.payload.CreateApplicationRequest;
import com.aimbitious.job.payload.WithdrawApplicationRequest;
import com.aimbitious.job.repository.ApplicationNoteRepository;
import com.aimbitious.job.repository.ApplicationRepository;
import com.aimbitious.job.repository.ApplicationSpecification;
import com.aimbitious.job.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ApplicationNoteRepository applicationNoteRepository;
    private final JobClient jobClient;
    private final ResumeClient resumeClient;
    private final CompanyClient companyClient;
    private final UserClient userClient;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public ApplicationResponse createApplication(Long candidateId, CreateApplicationRequest req) throws Exception {

        if (applicationRepository.existsByCandidateIdAndJobId(candidateId, req.getJobId())) {
            throw new Exception("you have already applied");
        }

        JobResponse jobResponse = jobClient.getJobById(req.getJobId());
        Long companyId=jobResponse.getCompany().getId();
        Long employeeId=jobResponse.getEmployerId();


        ResumeResponse resumeResponse = resumeClient.getResumeById(req.getResumeId(), candidateId);


        Application application = ApplicationMapper.toEntity(
                req, candidateId, companyId, employeeId
        );

        Application savedApplication = applicationRepository.save(application);

//        todo: AI Screening runs in the background thread, no callback needed

        return buildFullResponse(savedApplication);
    }

    @Override
    public ApplicationResponse getApplicationById(Long id) throws Exception {
        Application application=getApplicationEntity(id);
        return buildFullResponse(application);
    }

    @Override
    public List<ApplicationResponse> getMyApplications(Long candidateId) {
        return applicationRepository.findByCandidateId(candidateId)
                .stream().map(
                        this::buildFullResponse
                ).toList();
    }

    @Override
    public List<ApplicationResponse> getApplicationsForJob(Long jobId) {
        return applicationRepository.findByJobId(jobId)
                .stream().map(
                        this::buildFullResponse
                ).toList();
    }

    @Override
    public List<ApplicationResponse> getApplicationsForCompany(Long userId,
                                                               CompanyApplicationFilterRequest filter) {
        // fetch company by OwnerId
        Long companyId = companyClient.getMyCompany(userId).getId();
        Sort sort = buildSort(filter.getSortBy());

        return applicationRepository.findAll(
                ApplicationSpecification.forCompanyWithFilters(
                companyId,
                filter.getJobId(),
                filter.getApplicationStatus(),
                filter.getIsStarred(),
                filter.getAiShortListStatus(),
                filter.getMinAiScore()
                ), sort)
                .stream().map(
                        this::buildFullResponse
                ).toList();

    }



    @Override
    public ApplicationResponse updateStatus(Long applicationId,
                                            Long employerId,
                                            ApplicationStatus status) throws Exception {

        Application application = getApplicationEntity(applicationId);
        ApplicationStatus oldStatus = application.getStatus();
        assertEmployer(application, employerId);

        if (application.getStatus() == ApplicationStatus.WITHDRAWN) {
            throw new Exception("candidate have already withdrawn");
        }

        application.setStatus(status);
        Application savedApplication = applicationRepository.save(application);

//        publishing the Kafka event here.
        applicationEventPublisher.publishStatusChange(application, oldStatus, status, "your application has changed");

        return buildFullResponse(savedApplication);
    }



    @Override
    public ApplicationResponse withdraw(Long applicationId, Long candidateId,
                                        WithdrawApplicationRequest req) throws Exception {
        Application application = getApplicationEntity(applicationId);
        assertCandidate(application, candidateId);
        application.setStatus(ApplicationStatus.WITHDRAWN);
        application.setWithdrawnReason(req.getReason());
        Application savedApplication = applicationRepository.save(application);

        return buildFullResponse(savedApplication);
    }



    @Override
    public ApplicationResponse toggleStar(Long applicationId, Long employerId) throws Exception {
        Application application = getApplicationEntity(applicationId);
        assertEmployer(application, employerId);
        application.setIsStarred(true);

        Application savedApplication = applicationRepository.save(application);
        return buildFullResponse(savedApplication);
    }

    @Override
    public void deleteApplication(Long applicationId, Long candidateId) throws Exception {
        Application application = getApplicationEntity(applicationId);
        assertCandidate(application, candidateId);

        applicationRepository.delete(application);
    }

    @Override
    public Application getApplicationEntity(Long id) throws Exception {
        return applicationRepository.findById(id).orElseThrow(
                () -> new Exception("application not found")
        );
    }

    public ApplicationResponse buildFullResponse(Application application) {

        //    fetch  real data from respective microservice
        JobResponse job = jobClient.getJobById(application.getJobId());

        CompanyResponse company = companyClient.getCompanyById(application.getCompanyId());

        UserResponse candidate = userClient.getUserById(application.getCandidateId());

        List<ApplicationNote> notes = applicationNoteRepository
                .findByApplicationId(application.getId());

        return ApplicationMapper.toResponse(
                application,
                notes,
                job,
                company,
                candidate
        );
    }

    // Sorting the applications using AI Score
    private Sort buildSort(String sortBy) {

        if ("AI_SCORE_DESC".equals(sortBy)) {
            return Sort.by(
                    Sort.Order.desc("aiScore")
                            .with(Sort.NullHandling.NULLS_LAST)
            );
        } else if ("AI_SCORE_ASC".equals(sortBy)) {
            return Sort.by(
                    Sort.Order.asc("aiScore")
                            .with(Sort.NullHandling.NULLS_LAST)
            );
        }

        return Sort.by(Sort.Direction.DESC, "appliedAt");
    }

    //below method will verify the candidate
    private void assertCandidate(Application application, Long candidateId) throws Exception {
        if(!application.getCandidateId().equals(candidateId)){
            throw new Exception("you are not the owner of this application");
        }
    }

    //below method will verify the employer
    private void assertEmployer(Application application, Long employerId) throws Exception {

        if (!application.getEmployerId().equals(employerId)) {
            throw new Exception("you are not the employer for this application");
        }
    }
}
