package com.aimbitious.job.payload;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class JobDescriptionRequest {

    @NotNull(message = "Job title is required")
    private String title;

    private List<String> skills;

    private String experienceLevel;

    private String jobType;

    private String workMode;

    private String category;

    private String additionalContext;
}
