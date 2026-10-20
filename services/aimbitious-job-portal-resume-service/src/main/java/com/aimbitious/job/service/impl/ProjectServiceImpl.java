package com.aimbitious.job.service.impl;

import com.aimbitious.job.dto.ProjectResponse;
import com.aimbitious.job.mapper.ResumeMapper;
import com.aimbitious.job.model.Project;
import com.aimbitious.job.model.Resume;
import com.aimbitious.job.payload.AddProjectRequest;
import com.aimbitious.job.repository.ProjectRepository;
import com.aimbitious.job.service.ProjectService;
import com.aimbitious.job.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private ResumeService resumeService;
    private final ProjectRepository projectRepository;


    @Override
    public ProjectResponse addProject(Long resumeId, Long candidateId, AddProjectRequest req) throws Exception {
        Resume resume = resumeService.getResumeEntity(resumeId);
        assertOwner(resume, candidateId);

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

        Project savedProject = projectRepository.save(project);
        return ResumeMapper.toProjectResponse(savedProject);
    }

    @Override
    public List<ProjectResponse> getAllProjects(Long resumeId) {
        return projectRepository.findByResume_IdOrderByDisplayOrderAsc(resumeId)
                .stream().map(ResumeMapper::toProjectResponse).toList();
    }

    @Override
    public ProjectResponse updateProject(Long projectId, Long resumeId, Long candidateId, AddProjectRequest req) throws Exception {
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new Exception("project does not exist")
        );

        assertOwner(project.getResume(), candidateId);

        project.setTitle(req.getTitle());
        project.setDescription(req.getDescription());

        if (req.getTechnologies() != null)
            project.setTechnologies(req.getTechnologies());

        project.setProjectUrl(req.getProjectUrl());
        project.setSourceCodeUrl(req.getSourceCodeUrl());
        project.setStartDate(req.getStartDate());
        project.setEndDate(req.getEndDate());
        project.setIsOngoing(Boolean.TRUE.equals(req.getIsOngoing()));

        if (req.getDisplayOrder() != null)
            project.setDisplayOrder(req.getDisplayOrder());


        return ResumeMapper.toProjectResponse((projectRepository.save(project)));
    }

    @Override
    public void deleteProject(Long projectId, Long resumeId, Long candidateId) throws Exception {
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new Exception("project does not exist")
        );
        assertOwner(project.getResume(), candidateId);
        projectRepository.delete(project);
    }

    private void assertOwner(Resume resume, Long candidateId) throws Exception {
        if (!resume.getCandidateId().equals(candidateId)) {
            throw new Exception("resume not found");
        }
    }
}
