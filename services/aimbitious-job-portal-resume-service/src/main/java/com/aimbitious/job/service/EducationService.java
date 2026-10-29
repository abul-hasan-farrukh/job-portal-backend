package com.aimbitious.job.service;

import com.aimbitious.job.dto.response.EducationResponse;
import com.aimbitious.job.exception.ResourceNotFoundException;
import com.aimbitious.job.payload.AddEducationRequest;

import java.util.List;

public interface EducationService {

    EducationResponse addEducation(Long resumeId, Long candidateId, AddEducationRequest req)
            throws ResourceNotFoundException;

    List<EducationResponse> getEducations(Long resumeId) throws ResourceNotFoundException;

    EducationResponse updateEducation(Long educationId, Long resumeId, Long candidateId,
                                      AddEducationRequest req) throws ResourceNotFoundException;

    void deleteEducation(Long educationId, Long resumeId, Long candidateId)
            throws ResourceNotFoundException;
}
