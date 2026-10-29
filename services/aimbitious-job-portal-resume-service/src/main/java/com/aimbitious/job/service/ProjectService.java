package com.aimbitious.job.service;

import com.aimbitious.job.dto.response.ProjectResponse;
import com.aimbitious.job.exception.ResourceNotFoundException;
import com.aimbitious.job.payload.AddProjectRequest;

import java.util.List;

public interface ProjectService {

    ProjectResponse addProject(Long resumeId, Long candidateId, AddProjectRequest req)
            throws ResourceNotFoundException;

    List<ProjectResponse> getProjects(Long resumeId) throws ResourceNotFoundException;

    ProjectResponse updateProject(Long projectId, Long resumeId, Long candidateId,
                                  AddProjectRequest req) throws ResourceNotFoundException;

    void deleteProject(Long projectId, Long resumeId, Long candidateId)
            throws ResourceNotFoundException;
}
