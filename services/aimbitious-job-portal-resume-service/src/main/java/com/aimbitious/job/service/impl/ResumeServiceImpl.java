package com.aimbitious.job.service.impl;

import com.aimbitious.job.dto.*;
import com.aimbitious.job.mapper.ResumeMapper;
import com.aimbitious.job.mapper.WorkExperienceMapper;
import com.aimbitious.job.model.PersonalInfo;
import com.aimbitious.job.model.Resume;
import com.aimbitious.job.payload.CreateResumeRequest;
import com.aimbitious.job.repository.*;
import com.aimbitious.job.service.AwardService;
import com.aimbitious.job.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ResumeServiceImpl implements ResumeService {

    private final ResumeRepository resumeRepository;
    private final WorkExperienceRepository workExperienceRepository;
    private final ResumeSkillRepository resumeSkillRepository;
    private final ProjectRepository projectRepository;
    private final LanguageRepository languageRepository;
    private final EducationRepository educationRepository;
    private final CertificationRepository certificationRepository;
    private final AwardRepository awardRepository;

    @Override
    public ResumeResponse createResume(Long candidateId, CreateResumeRequest req) {
        if (Boolean.TRUE.equals(req.getIsDefault())) {
            resumeRepository.findByCandidateIdAndIsDefaultTrue(candidateId)
                    .ifPresent(existing -> {
                        existing.setIsDefault(false);
                        resumeRepository.save(existing);
                    });
        }

        Resume resume = Resume.builder()
                .candidateId(candidateId)
                .title(req.getTitle())
                .template(req.getTemplate())
                .visibility(req.getVisibility())
                .isDefault(Boolean.TRUE.equals(req.getIsDefault()))
                .isActive(true)
                .build();
        Resume saved = resumeRepository.save(resume);

        return buildFullResponse(saved);
    }

    @Override
    public ResumeResponse getResumeById(Long resumeId, Long candidateId) throws Exception {
        Resume resume = getResumeEntity(resumeId);
        assertOwner(resume, candidateId);
        return buildFullResponse(resume);
    }

    private void assertOwner(Resume resume, Long candidateId) throws Exception {
        if(!resume.getCandidateId().equals(candidateId)){
          throw new Exception("resume not found with id");
        }
    }

    @Override
    public List<ResumeResponse> getMyResumes(Long candidateId) {
        return resumeRepository.findByCandidateIdAndIsActiveTrue(candidateId)
                .stream().map(this::buildFullResponse).toList();
    }

    @Override
    public ResumeResponse updatePersonalInfo(Long resumeId, Long candidateId, PersonalInfoResponse req) throws Exception{
        Resume resume = getResumeEntity(resumeId);
        assertOwner(resume, candidateId);

        PersonalInfo info = resume.getPersonalInfo();
        if (info == null) info = new PersonalInfo();

        if (req.getFirstName() != null)
            info.setFirstName(req.getFirstName());

        if (req.getLastName() != null)
            info.setLastName(req.getLastName());

        if (req.getHeadline() != null)
            info.setHeadline(req.getHeadline());

        if (req.getEmail() != null)
            info.setEmail(req.getEmail());

        if (req.getPhone() != null)
            info.setPhone(req.getPhone());

        if (req.getCity() != null)
            info.setCity(req.getCity());

        if (req.getCountry() != null)
            info.setCountry(req.getCountry());

        if (req.getLinkedinUrl() != null)
            info.setLinkedinUrl(req.getLinkedinUrl());

        if (req.getGithubUrl() != null)
            info.setGithubUrl(req.getGithubUrl());

        if (req.getPortfolioUrl() != null)
            info.setPortfolioUrl(req.getPortfolioUrl());

        if (req.getWebsiteUrl() != null)
            info.setWebsiteUrl(req.getWebsiteUrl());

        if(req.getProfileImage() != null)
            info.setProfileImage(req.getProfileImage());

        resume.setPersonalInfo(info);
        Resume updated = resumeRepository.save(resume);

        return buildFullResponse(updated);
    }

    @Override
    public ResumeResponse updateSummary(Long resumeId, Long candidateId, String summary) throws Exception {
        Resume resume = getResumeEntity(resumeId);
        assertOwner(resume, candidateId);

        resume.setSummary(summary);
        Resume updated = resumeRepository.save(resume);

        return buildFullResponse(updated);
    }

    @Override
    public ResumeResponse setDefaultResume(Long resumeId, Long candidateId) throws Exception {
        Resume resume = getResumeEntity(resumeId);
        assertOwner(resume, candidateId);

        resumeRepository.findByCandidateIdAndIsDefaultTrue(candidateId)
                .ifPresent(existing -> {
                    existing.setIsDefault(false);
                    resumeRepository.save(existing);
                });

        resume.setIsDefault(true);
        Resume updated = resumeRepository.save(resume);

        return buildFullResponse(updated);
    }

    @Override
    public void deleteResume(Long resumeId, Long candidateId) throws Exception {
        Resume resume = getResumeEntity(resumeId);
        assertOwner(resume, candidateId);
        resume.setIsActive(false);
        resume.setIsDefault(false);
        resumeRepository.save(resume);
    }

    @Override
    public Resume getResumeEntity(Long resumeId) throws Exception {
        return resumeRepository.findById(resumeId).orElseThrow(
                () -> new Exception("resume not found with id" + resumeId)
        );
    }

    private ResumeResponse buildFullResponse(Resume resume){

        Long resumeId = resume.getId();

        List<WorkExperienceResponse> workExperienceResponses =
                workExperienceRepository.findByResume_IdOrderByDisplayOrderAsc(resumeId)
                        .stream().map(WorkExperienceMapper::toWorkExperienceResponse).toList();

        List<ResumeSkillResponse> resumeSkillResponses =
                resumeSkillRepository.findByResume_IdOrderByDisplayOrderAsc(resumeId)
                        .stream().map(ResumeMapper::toSkillResponse).toList();

        List<EducationResponse> educationResponses =
                educationRepository.findByResume_IdOrderByDisplayOrderAsc(resumeId)
                        .stream().map(ResumeMapper::toEducationResponse).toList();

        List<ProjectResponse> projectResponses =
                projectRepository.findByResume_IdOrderByDisplayOrderAsc(resumeId)
                        .stream().map(ResumeMapper::toProjectResponse).toList();

        List<LanguageResponse> languageResponses =
                languageRepository.findByResume_IdOrderByDisplayOrderAsc(resumeId)
                        .stream().map(ResumeMapper::toLanguageResponse).toList();

        List<CertificationResponse> certificationResponses =
                certificationRepository.findByResume_IdOrderByDisplayOrderAsc(resumeId)
                        .stream().map(ResumeMapper::toCertificationResponse).toList();

        List<AwardResponse> awardResponses =
                awardRepository.findByResume_IdOrderByDisplayOrderAsc(resumeId)
                        .stream().map(ResumeMapper::toAwardResponse).toList();

        return ResumeMapper.toResponse(resume,
                workExperienceResponses,
                educationResponses,
                resumeSkillResponses,
                projectResponses,
                languageResponses,
                certificationResponses,
                awardResponses);
    }
}
