package com.aimbitious.job.service;


import com.aimbitious.job.dto.PersonalInfoResponse;
import com.aimbitious.job.dto.ResumeResponse;
import com.aimbitious.job.model.Resume;
import com.aimbitious.job.payload.CreateResumeRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ResumeService {

    ResumeResponse createResume(Long candidateId, CreateResumeRequest req);

    ResumeResponse getResumeById(Long resumeId, Long candidateId) throws Exception;

    List<ResumeResponse> getMyResumes(Long candidateId);

    ResumeResponse updatePersonalInfo(Long resumeId, Long candidateId, PersonalInfoResponse req) throws Exception;

    ResumeResponse updateSummary(Long resumeId, Long candidateId, String summary) throws Exception;

    ResumeResponse setDefaultResume(Long resumeId, Long candidateId) throws Exception;

    void deleteResume(Long resumeId, Long candidateId) throws Exception;

    Resume getResumeEntity(Long resumeId) throws Exception;

}
