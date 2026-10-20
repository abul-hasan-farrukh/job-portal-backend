package com.aimbitious.job.service;

import com.aimbitious.job.dto.ProjectResponse;
import com.aimbitious.job.payload.AddProjectRequest;

import java.util.List;

public interface ProjectService {

    ProjectResponse addProject(Long resumeId, Long candidateId, AddProjectRequest req) throws Exception;

    List<ProjectResponse> getAllProjects(Long resumeId);

    ProjectResponse updateProject(
            Long projectId,
            Long resumeId,
            Long candidateId,
            AddProjectRequest req
    ) throws Exception;

    void deleteProject(Long projectId, Long resumeId, Long candidateId) throws Exception;
}
