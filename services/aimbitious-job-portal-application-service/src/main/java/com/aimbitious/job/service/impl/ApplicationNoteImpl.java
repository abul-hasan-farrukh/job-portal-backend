package com.aimbitious.job.service.impl;

import com.aimbitious.job.dto.ApplicationNoteResponse;
import com.aimbitious.job.mapper.ApplicationMapper;
import com.aimbitious.job.model.Application;
import com.aimbitious.job.model.ApplicationNote;
import com.aimbitious.job.payload.AddApplicationNoteRequest;
import com.aimbitious.job.repository.ApplicationNoteRepository;
import com.aimbitious.job.service.ApplicationNoteService;
import com.aimbitious.job.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class ApplicationNoteImpl implements ApplicationNoteService {

    private final ApplicationService applicationService;
    private final ApplicationNoteRepository applicationNoteRepository;

    @Override
    public ApplicationNoteResponse addNote(Long applicationId, Long employerId, AddApplicationNoteRequest req) throws Exception {
        Application application = applicationService.getApplicationEntity(applicationId);
        assertEmployer(application, employerId);

        ApplicationNote applicationNote = ApplicationNote.builder()
                .application(application)
                .addedByUserId(employerId)
                .content(req.getContent())
                .build();

        ApplicationNote savedNote = applicationNoteRepository.save(applicationNote);
        return ApplicationMapper.toNoteResponse(savedNote);

    }

    @Override
    public List<ApplicationNoteResponse> getNotesByApplication(Long applicationId, Long employerId) {
        return applicationNoteRepository.findByApplicationId(applicationId)
                .stream().map(ApplicationMapper::toNoteResponse).toList();
    }

    @Override
    public void deleteNote(Long applicationId, Long employerId, Long noteId) throws Exception {
        Application application = applicationService.getApplicationEntity(applicationId);
        assertEmployer(application, employerId);

        ApplicationNote note = applicationNoteRepository.findById(noteId).orElseThrow(
                () -> new Exception("note does not belong to application")
        );
        applicationNoteRepository.delete(note);
    }

    //below method will verify the employer
    private void assertEmployer(Application application, Long employerId) throws Exception {

        if (!application.getEmployerId().equals(employerId)) {
            throw new Exception("you are not the employer for this application");
        }
    }
}
