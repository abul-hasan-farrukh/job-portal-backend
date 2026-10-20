package com.aimbitious.job.mapper;

import com.aimbitious.job.dto.CompanyResponse;
import com.aimbitious.job.dto.JobResponse;
import com.aimbitious.job.dto.JobSkillResponse;
import com.aimbitious.job.dto.JobTagResponse;
import com.aimbitious.job.model.embeddable.JobLocation;
import com.aimbitious.job.model.embeddable.SalaryRange;
import com.aimbitious.job.model.Job;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class JobMapper {

    public static JobResponse toResponse(Job job, CompanyResponse companyResponse) {

        JobLocation loc = job.getLocation();
        SalaryRange sal = job.getSalaryRange();

        Set<JobSkillResponse> skills = job.getSkills()== null ?
                Collections.emptySet()
                :job.getSkills().stream().map(JobSkillMapper::toJobSkillResponse)
                .collect(Collectors.toSet());

        Set<JobTagResponse> tags = job.getTags() == null
                ? Collections.emptySet()
                : job.getTags().stream()
                .map(JobTagMapper::toTagResponse)
                .collect(Collectors.toSet());

        return JobResponse.builder()
                .id(job.getId())
                .title(job.getTitle())

                .description(job.getDescription())
                .requirements(job.getRequirements())
                .responsibilities(job.getResponsibilities())
                .benefits(job.getBenefits())
                .company(companyResponse)
                .employerId(job.getEmployerId())

                .category(JobCategoryMapper.toJobCategoryResponse(job.getCategory(), false))
                .skills(skills)
                .tags(tags)

                // location
                .address(loc != null ? loc.getAddress() : null)
                .city(loc != null ? loc.getCity() : null)
                .state(loc != null ? loc.getState() : null)
                .country(loc != null ? loc.getCountry() : null)
                .zipCode(loc != null ? loc.getZipCode() : null)


                // salary
                .minSalary(sal != null ? sal.getMinSalary() : null)
                .maxSalary(sal != null ? sal.getMaxSalary() : null)
//                .currency(sal != null ? sal.getCurrency() : null)
//                .salaryPeriod(sal != null ? sal.getPeriod() : null)
//                .salaryNegotiable(sal != null ? sal.getNegotiable() : null)
//                .salaryDisclosed(sal != null ? sal.getDisclosed() : null)

                // classification
                .jobType(job.getJobType())
                .workMode(job.getWorkMode())
                .experienceLevel(job.getExperienceLevel())
                .status(job.getStatus())

                // posting
                .openings(job.getOpenings())
                .applicationDeadline(job.getApplicationDeadline())
                .expiresAt(job.getExpiresAt())
                .active(job.getActive())

                // analytics
//                .viewCount(job.getViewCount())
//                .applicationCount(job.getApplicationCount())

                // timestamps
                .createdAt(job.getCreatedAt())
                .updatedAt(job.getUpdatedAt())
                .publishedAt(job.getPublishedAt())
                .closedAt(job.getClosedAt())
                .build();
    }
}
