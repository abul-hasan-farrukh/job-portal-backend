package com.aimbitious.job.mapper;

import com.aimbitious.job.dto.*;
import com.aimbitious.job.model.*;

import java.util.List;

public class ResumeMapper {

    public static PersonalInfoResponse toPersonalInfoResponse(PersonalInfo personalInfo) {

        if(personalInfo == null ) return null;

        return PersonalInfoResponse.builder()
                .firstName(personalInfo.getFirstName())
                .lastName(personalInfo.getLastName())
                .email(personalInfo.getEmail())
                .phone(personalInfo.getPhone())
                .headline(personalInfo.getHeadline())
                .country(personalInfo.getCountry())
                .profileImage(personalInfo.getProfileImage())
                .city(personalInfo.getCity())
                .githubUrl(personalInfo.getGithubUrl())
                .linkedinUrl(personalInfo.getLinkedinUrl())
                .portfolioUrl(personalInfo.getPortfolioUrl())
                .websiteUrl(personalInfo.getWebsiteUrl())
                .build();
    }

    public static ResumeResponse toResponse(Resume resume,
                                            List<WorkExperienceResponse> workExperiences,
                                            List<EducationResponse> educationResponses,
                                            List<ResumeSkillResponse> resumeSkillResponses,
                                            List<ProjectResponse> projectResponses,
                                            List<LanguageResponse> languageResponses,
                                            List<CertificationResponse> certificationResponses,
                                            List<AwardResponse> awardResponses) {

        if (resume == null) return null;

        return ResumeResponse.builder()
                .id(resume.getId())
                .candidateId(resume.getCandidateId())
                .title(resume.getTitle())
                .template(resume.getTemplate())
                .visibility(resume.getVisibility())
                .isDefault(resume.getIsDefault())
                .personalInfo(ResumeMapper.toPersonalInfoResponse(resume.getPersonalInfo()))
                .summary(resume.getSummary())
                .completionScore(resume.getCompletionScore())
                .createdAt(resume.getCreatedAt())
                .updatedAt(resume.getUpdatedAt())
                .workExperiences(workExperiences)
                .educations(educationResponses)
                .skills(resumeSkillResponses)
                .projects(projectResponses)
                .languages(languageResponses)
                .certifications(certificationResponses)
                .awards(awardResponses)
                .build();
    }

    //mapper for resumeskill
    public static ResumeSkillResponse toSkillResponse(ResumeSkill skill) {
        if (skill == null) return null;

        return ResumeSkillResponse.builder()
                .id(skill.getId())
                .skillName(skill.getSkillName())
                .proficiencyLevel(skill.getProficiencyLevel())
                .yearsOfExperience(skill.getYearsOfExperience())
                .displayOrder(skill.getDisplayOrder())
                .build();
    }

    //mapper for education
    public static EducationResponse toEducationResponse(Education edu) {

        if (edu == null) return null;

        return EducationResponse.builder()
                .id(edu.getId())
                .institutionName(edu.getInstitutionName())
                .degree(edu.getDegree())
                .fieldOfStudy(edu.getFieldOfStudy())
                .grade(edu.getGrade())
                .startDate(edu.getStartDate())
                .endDate(edu.getEndDate())
                .isCurrentlyStudying(edu.getIsCurrentlyStudying())
                .description(edu.getDescription())
                .displayOrder(edu.getDisplayOrder())
                .build();
    }

    public static ProjectResponse toProjectResponse(Project project) {
        if (project == null) return null;

        return ProjectResponse.builder()
                .id(project.getId())
                .title(project.getTitle())
                .description(project.getDescription())
                .technologies(project.getTechnologies())
                .projectUrl(project.getProjectUrl())
                .sourceCodeUrl(project.getSourceCodeUrl())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .isOngoing(project.getIsOngoing())
                .displayOrder(project.getDisplayOrder())
                .build();
    }

    public static LanguageResponse toLanguageResponse(Language lang) {
        if (lang == null) return null;

        return LanguageResponse.builder()
                .id(lang.getId())
                .languageName(lang.getLanguageName())
                .proficiency(lang.getProficiency())
                .displayOrder(lang.getDisplayOrder())
                .build();
    }

    public static CertificationResponse toCertificationResponse(Certification cert) {
        if (cert == null) return null;
        return CertificationResponse.builder()
                .id(cert.getId())
                .name(cert.getName())
                .issuingOrganization(cert.getIssuingOrganization())
                .issueDate(cert.getIssueDate())
                .expiryDate(cert.getExpiryDate())
                .credentialId(cert.getCredentialId())
                .credentialUrl(cert.getCredentialUrl())
                .displayOrder(cert.getDisplayOrder())
                .build();
    }

    public static AwardResponse toAwardResponse(Award award) {
        if (award == null) return null;
        return AwardResponse.builder()
                .id(award.getId())
                .title(award.getTitle())
                .issuedBy(award.getIssuedBy())
                .awardDate(award.getAwardDate())
                .description(award.getDescription())
                .displayOrder(award.getDisplayOrder())
                .build();
    }
}
