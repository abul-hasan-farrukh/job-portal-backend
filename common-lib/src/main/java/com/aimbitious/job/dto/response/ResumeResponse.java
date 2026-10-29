package com.aimbitious.job.dto.response;

import com.aimbitious.job.domain.ResumeTemplate;
import com.aimbitious.job.domain.ResumeVisibility;
import com.aimbitious.job.model.embeddable.PersonalInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResumeResponse {


    private Long id;
    private Long candidateId;
    private String title;

    private ResumeTemplate template;
    private ResumeVisibility visibility;

    private Boolean isDefault;
    private PersonalInfo personalInfo;

    private String summary;
    private String uploadedFileUrl;
    private String uploadedFileName;
    private Integer completionScore;

    private Boolean active;
    private LocalDateTime lastViewedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<WorkExperienceResponse> workExperiences;
    private List<EducationResponse> educations;
    private List<ResumeSkillResponse> skills;
    private List<ProjectResponse> projects;
    private List<CertificationResponse> certifications;
    private List<AwardResponse> awards;
    private List<LanguageResponse> languages;

}
