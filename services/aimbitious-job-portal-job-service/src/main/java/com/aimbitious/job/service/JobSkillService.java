package com.aimbitious.job.service;

import com.aimbitious.job.domain.SkillCategory;
import com.aimbitious.job.dto.response.JobSkillResponse;
import com.aimbitious.job.exception.JobException;
import com.aimbitious.job.exception.ResourceNotFoundException;
import com.aimbitious.job.model.JobSkill;
import com.aimbitious.job.payload.BulkJobSkillRequest;
import com.aimbitious.job.payload.BulkJobSkillResponse;
import com.aimbitious.job.payload.JobSkillRequest;

import java.util.List;
import java.util.Set;

public interface JobSkillService {

    JobSkillResponse createSkill(JobSkillRequest req) throws JobException;

    BulkJobSkillResponse createSkillsBulk(BulkJobSkillRequest req);

    List<JobSkillResponse> getAllSkills();

    List<JobSkillResponse> getSkillsByCategory(SkillCategory category);

    List<JobSkillResponse> searchSkills(String keyword);

    JobSkillResponse getSkillById(Long id) throws ResourceNotFoundException;

    JobSkillResponse updateSkill(Long id, JobSkillRequest req)
            throws ResourceNotFoundException, JobException;

    void deleteSkill(Long id) throws ResourceNotFoundException;

    /** Used internally to load skills by IDs for job creation. */
    Set<JobSkill> getSkillEntitiesByIds(Set<Long> ids) throws ResourceNotFoundException;
}
