package com.aimbitious.job.service;

import com.aimbitious.job.dto.JobSkillResponse;
import com.aimbitious.job.model.JobSkill;
import com.aimbitious.job.payload.JobSkillRequest;

import java.util.List;
import java.util.Set;

public interface JobSkillService {

    JobSkillResponse createSkill(JobSkillRequest req) throws Exception;

    List<JobSkillResponse> getAllSkills();

    JobSkillResponse getSkillById(Long id) throws Exception;

    JobSkillResponse updateSkill(Long id, JobSkillRequest req) throws Exception;

    JobSkillResponse deleteSkill(Long id) throws Exception;

    Set<JobSkill> getSkillsByIds(Set<Long> ids);

}
