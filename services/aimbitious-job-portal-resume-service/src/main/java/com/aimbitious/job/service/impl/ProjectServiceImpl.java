package com.aimbitious.job.service.impl;

import com.aimbitious.job.dto.response.ProjectResponse;
import com.aimbitious.job.exception.ResourceNotFoundException;
import com.aimbitious.job.mapper.ResumeMapper;
import com.aimbitious.job.model.Project;
import com.aimbitious.job.model.Resume;
import com.aimbitious.job.payload.AddProjectRequest;
import com.aimbitious.job.repository.ProjectRepository;
import com.aimbitious.job.service.ProjectService;
import com.aimbitious.job.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ResumeService resumeService;

    @Override
    @Transactional
    public ProjectResponse addProject(Long resumeId, Long candidateId, AddProjectRequest req)
            throws ResourceNotFoundException {
        Resume resume = resumeService.getResumeEntity(resumeId);
        assertOwner(resume, candidateId, resumeId);

        Project project = Project.builder()
                .resume(resume)
                .title(req.getTitle())
                .description(req.getDescription())
                .technologies(req.getTechnologies() != null ? req.getTechnologies() : List.of())
                .projectUrl(req.getProjectUrl())
                .sourceCodeUrl(req.getSourceCodeUrl())
                .startDate(req.getStartDate())
                .endDate(req.getEndDate())
                .isOngoing(Boolean.TRUE.equals(req.getIsOngoing()))
                .displayOrder(req.getDisplayOrder() != null ? req.getDisplayOrder() : 0)
                .build();

        return ResumeMapper.toProjectResponse(projectRepository.save(project));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponse> getProjects(Long resumeId) throws ResourceNotFoundException {
        resumeService.getResumeEntity(resumeId);
        return projectRepository.findByResume_IdOrderByDisplayOrderAsc(resumeId)
                .stream().map(ResumeMapper::toProjectResponse).toList();
    }

    @Override
    @Transactional
    public ProjectResponse updateProject(Long projectId, Long resumeId, Long candidateId,
                                         AddProjectRequest req) throws ResourceNotFoundException {
        Project project = getProjectEntity(projectId, resumeId);
        assertOwner(project.getResume(), candidateId, resumeId);

        project.setTitle(req.getTitle());
        project.setDescription(req.getDescription());
        if (req.getTechnologies() != null) project.setTechnologies(req.getTechnologies());
        project.setProjectUrl(req.getProjectUrl());
        project.setSourceCodeUrl(req.getSourceCodeUrl());
        project.setStartDate(req.getStartDate());
        project.setEndDate(req.getEndDate());
        project.setIsOngoing(Boolean.TRUE.equals(req.getIsOngoing()));
        if (req.getDisplayOrder() != null) project.setDisplayOrder(req.getDisplayOrder());

        return ResumeMapper.toProjectResponse(projectRepository.save(project));
    }

    @Override
    @Transactional
    public void deleteProject(Long projectId, Long resumeId, Long candidateId)
            throws ResourceNotFoundException {
        Project project = getProjectEntity(projectId, resumeId);
        assertOwner(project.getResume(), candidateId, resumeId);
        projectRepository.delete(project);
    }

    private Project getProjectEntity(Long projectId, Long resumeId) throws ResourceNotFoundException {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Project not found with id: " + projectId));
        if (!project.getResume().getId().equals(resumeId)) {
            throw new ResourceNotFoundException("Project not found with id: " + projectId);
        }
        return project;
    }

    private void assertOwner(Resume resume, Long candidateId, Long resumeId)
            throws ResourceNotFoundException {
        if (!resume.getCandidateId().equals(candidateId)) {
            throw new ResourceNotFoundException("Resume not found with id: " + resumeId);
        }
    }
}
