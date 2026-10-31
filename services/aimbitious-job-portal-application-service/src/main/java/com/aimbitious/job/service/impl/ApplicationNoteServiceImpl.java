package com.aimbitious.job.service.impl;

import com.aimbitious.job.dto.response.ApplicationNoteResponse;
import com.aimbitious.job.event.ApplicationEventPublisher;
import com.aimbitious.job.exception.ApplicationException;
import com.aimbitious.job.exception.ResourceNotFoundException;
import com.aimbitious.job.mapper.ApplicationMapper;
import com.aimbitious.job.model.Application;
import com.aimbitious.job.model.ApplicationNote;
import com.aimbitious.job.payload.AddApplicationNoteRequest;
import com.aimbitious.job.repository.ApplicationNoteRepository;
import com.aimbitious.job.service.ApplicationNoteService;
import com.aimbitious.job.service.ApplicationService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class ApplicationNoteServiceImpl implements ApplicationNoteService {

    private final ApplicationService applicationService;
    private final ApplicationNoteRepository applicationNoteRepository;
    private final ApplicationEventPublisher eventPublisher;

    // ── Add ───────────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public ApplicationNoteResponse addNote(Long applicationId, Long employerId,
                                           AddApplicationNoteRequest req)
            throws ResourceNotFoundException, ApplicationException {
        Application application = applicationService
                .getApplicationEntity(applicationId);

        assertEmployer(application, employerId);

        ApplicationNote note = ApplicationNote.builder()
                .application(application)
                .addedByUserId(employerId)
                .content(req.getContent())
                .build();

        ApplicationNoteResponse response = ApplicationMapper.toNoteResponse(applicationNoteRepository.save(note));
        eventPublisher.publishNoteAdded(application);
        return response;
    }

    // ── Read ──────────────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationNoteResponse> getNotesByApplication(Long applicationId, Long employerId)
            throws ResourceNotFoundException, ApplicationException {
        Application application = applicationService.getApplicationEntity(applicationId);
        assertEmployer(application, employerId);

        return applicationNoteRepository.findByApplicationIdOrderByCreatedAtDesc(applicationId)
                .stream()
                .map(ApplicationMapper::toNoteResponse)
                .collect(Collectors.toList());
    }

    // ── Delete ────────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public void deleteNote(Long noteId, Long applicationId, Long employerId)
            throws ResourceNotFoundException, ApplicationException {
        Application application = applicationService.getApplicationEntity(applicationId);
        assertEmployer(application, employerId);

        ApplicationNote note = applicationNoteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Note not found with id: " + noteId));

        if (!note.getApplication().getId().equals(applicationId)) {
            throw new ApplicationException(
                    "Note does not belong to application with id: " + applicationId);
        }
        applicationNoteRepository.delete(note);
    }

    // ── Private utilities ─────────────────────────────────────────────────────

    //below method will verify the employer
    private void assertEmployer(Application application, Long employerId) throws ApplicationException {
        if (!application.getEmployerId().equals(employerId)) {
            throw new ApplicationException("you are not the employer for this application");
        }
    }
}
