package com.aimbitious.job.service;

import com.aimbitious.job.dto.response.ResumeSkillResponse;
import com.aimbitious.job.exception.ResourceNotFoundException;
import com.aimbitious.job.payload.AddResumeSkillRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public interface ResumeSkillService {

    ResumeSkillResponse addSkill(Long resumeId, Long candidateId, AddResumeSkillRequest req)
            throws ResourceNotFoundException;

    List<ResumeSkillResponse> getSkills(Long resumeId) throws ResourceNotFoundException;

    ResumeSkillResponse updateSkill(Long skillId, Long resumeId, Long candidateId,
                                    AddResumeSkillRequest req) throws ResourceNotFoundException;

    void deleteSkill(Long skillId, Long resumeId, Long candidateId) throws ResourceNotFoundException;
}
