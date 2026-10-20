package com.aimbitious.job.service;

import com.aimbitious.job.dto.ResumeSkillResponse;
import com.aimbitious.job.payload.AddResumeSkillRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public interface ResumeSkillService {

    ResumeSkillResponse addSkill(Long resumeId, Long candidateId, AddResumeSkillRequest req) throws Exception;

    List<ResumeSkillResponse> getSkills(Long resumeId);

    ResumeSkillResponse updateSkill(
            Long skillId,
            Long resumeId,
            Long candidateId,
            AddResumeSkillRequest req
    ) throws Exception;

    void deleteSkill(Long skillId, Long resumeId, Long candidateId) throws Exception;
}
