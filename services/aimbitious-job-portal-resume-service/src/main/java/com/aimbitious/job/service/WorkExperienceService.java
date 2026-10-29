package com.aimbitious.job.service;

import com.aimbitious.job.dto.response.WorkExperienceResponse;
import com.aimbitious.job.exception.ResourceNotFoundException;
import com.aimbitious.job.model.WorkExperience;
import com.aimbitious.job.payload.AddWorkExperienceRequest;

import java.util.List;

public interface WorkExperienceService {

    WorkExperienceResponse addWorkExperience(Long resumeId,
                                             Long candidateId,
                                             AddWorkExperienceRequest req)
            throws ResourceNotFoundException;

    List<WorkExperienceResponse> getWorkExperiences(Long resumeId) throws ResourceNotFoundException;

    WorkExperienceResponse updateWorkExperience(
            Long experienceId, Long resumeId, Long candidateId,
            AddWorkExperienceRequest req) throws ResourceNotFoundException;

    void deleteWorkExperience(Long experienceId, Long resumeId,
                              Long candidateId)
            throws ResourceNotFoundException;
}

