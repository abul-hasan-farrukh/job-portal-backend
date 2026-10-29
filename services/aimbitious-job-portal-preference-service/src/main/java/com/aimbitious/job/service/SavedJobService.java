package com.aimbitious.job.service;


import com.aimbitious.job.dto.response.SavedJobResponse;
import com.aimbitious.job.exception.ApplicationException;
import com.aimbitious.job.exception.ResourceNotFoundException;
import com.aimbitious.job.payload.SaveJobRequest;

import java.util.List;

public interface SavedJobService {

    SavedJobResponse saveJob(Long candidateId, SaveJobRequest req) throws ApplicationException;

    void unsaveJob(Long candidateId, Long savedJobId) throws ResourceNotFoundException;

    List<SavedJobResponse> getMySavedJobs(Long candidateId);

    boolean isSaved(Long candidateId, Long jobId);

}
