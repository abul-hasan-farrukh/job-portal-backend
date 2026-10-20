package com.aimbitious.job.payload;

import com.aimbitious.job.domain.ExperienceLevel;
import com.aimbitious.job.domain.JobStatus;
import com.aimbitious.job.domain.JobType;
import com.aimbitious.job.domain.WorkMode;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobSearchRequest {

    private String keyword;

    private Long categoryId;

    private List<Long> skillsIds;

    private List<Long> tagIds;

    private Long companyId;

    /** Matches city, state, or country (case-insensitive LIKE). */
    private String location;

    /** Salary overlap — job's max salary must be >= minSalary. */
    private BigDecimal minSalary;

    /** Salary overlap — job's min salary must be <= maxSalary. */
    private BigDecimal maxSalary;

    private JobType jobType;

    private WorkMode workMode;

    private ExperienceLevel experienceLevel;

    /** Defaults to OPEN in the service when null. */
    private JobStatus status;

    private Integer minOpenings;

    private Integer maxOpenings;
}
