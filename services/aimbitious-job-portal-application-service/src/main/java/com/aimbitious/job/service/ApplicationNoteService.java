package com.aimbitious.job.service;

import com.aimbitious.job.dto.response.ApplicationNoteResponse;
import com.aimbitious.job.exception.ApplicationException;
import com.aimbitious.job.exception.ResourceNotFoundException;
import com.aimbitious.job.payload.AddApplicationNoteRequest;

import java.util.List;

public interface ApplicationNoteService {

    ApplicationNoteResponse addNote(
            Long applicationId, Long employerId, AddApplicationNoteRequest req
    ) throws ResourceNotFoundException, ApplicationException;

    List<ApplicationNoteResponse> getNotesByApplication(Long applicationId, Long employerId)  throws ResourceNotFoundException, ApplicationException;

    void deleteNote(Long applicationId, Long employerId, Long noteId)  throws ResourceNotFoundException, ApplicationException;
}