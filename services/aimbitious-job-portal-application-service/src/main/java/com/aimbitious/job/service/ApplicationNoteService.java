package com.aimbitious.job.service;

import com.aimbitious.job.dto.ApplicationNoteResponse;
import com.aimbitious.job.payload.AddApplicationNoteRequest;

import java.util.List;

public interface ApplicationNoteService {

    ApplicationNoteResponse addNote(
            Long applicationId, Long employerId, AddApplicationNoteRequest req
    ) throws Exception;

    List<ApplicationNoteResponse> getNotesByApplication(Long applicationId, Long employerId);

    void deleteNote(Long applicationId, Long employerId, Long noteId) throws Exception;
}
