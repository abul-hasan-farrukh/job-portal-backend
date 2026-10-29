package com.aimbitious.job.payload;

import com.aimbitious.job.domain.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class UpdateJobPreferenceRequest {

    private Boolean isOpenToWork;
    private Integer noticePeriodDays;
    private Boolean willingToRelocate;
    private List<String> desiredJobTitles;
    private List<JobType> desiredJobTypes;
    private List<WorkMode> desiredWorkModes;
    private List<ExperienceLevel> desiredExperienceLevels;
    private List<IndustryType> desiredIndustries;
    private List<CompanySize> preferredCompanySizes;
    private List<String> preferredLocations;
    private BigDecimal minExpectedSalary;
    private BigDecimal maxExpectedSalary;
    private String expectedSalaryCurrency;
    private SalaryPeriod expectedSalaryPeriod;
}
