package com.aimbitious.job.service;


import com.aimbitious.job.dto.response.PersonalInfoResponse;
import com.aimbitious.job.dto.response.ResumeResponse;
import com.aimbitious.job.exception.ResourceNotFoundException;
import com.aimbitious.job.model.Resume;
import com.aimbitious.job.payload.CreateResumeRequest;
import com.aimbitious.job.payload.UpdatePersonalInfoRequest;
import com.aimbitious.job.payload.UpdateResumeRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ResumeService {

    ResumeResponse createResume(Long candidateId, CreateResumeRequest req);

    ResumeResponse getResumeById(Long resumeId, Long candidateId) throws ResourceNotFoundException;

    List<ResumeResponse> getMyResumes(Long candidateId);

    ResumeResponse updatePersonalInfo(
            Long resumeId, Long candidateId,
            UpdatePersonalInfoRequest req)
            throws ResourceNotFoundException;

    ResumeResponse updateSummary(Long resumeId, Long candidateId,
                                 String summary)
            throws ResourceNotFoundException;

    ResumeResponse updateResume(Long resumeId, Long candidateId, UpdateResumeRequest req)
            throws ResourceNotFoundException;

    ResumeResponse setDefaultResume(Long resumeId, Long candidateId) throws ResourceNotFoundException;

    void deleteResume(Long resumeId, Long candidateId) throws ResourceNotFoundException;

    Resume getResumeEntity(Long resumeId) throws ResourceNotFoundException;

}
