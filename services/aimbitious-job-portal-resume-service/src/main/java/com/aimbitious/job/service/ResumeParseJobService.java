package com.aimbitious.job.service;

import com.aimbitious.job.dto.response.ResumeParseJobResponse;
import com.aimbitious.job.exception.ResourceNotFoundException;
import com.aimbitious.job.payload.ParseResumeRequest;

import java.util.List;

public interface ResumeParseJobService {
    ResumeParseJobResponse submitParseJob(Long candidateId, ParseResumeRequest req);

    ResumeParseJobResponse getParseJob(Long jobId, Long candidateId) throws ResourceNotFoundException;

    List<ResumeParseJobResponse> getMyParseJobs(Long candidateId);
}
