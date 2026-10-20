package com.aimbitious.job.payload;

import com.aimbitious.job.domain.AiShortListStatus;
import com.aimbitious.job.domain.ApplicationStatus;
import lombok.Data;

@Data
public class CompanyApplicationFilterRequest {

    private Long jobId;

    private ApplicationStatus applicationStatus;

    private Boolean isStarred=false;

    private AiShortListStatus aiShortListStatus;

    private Integer minAiScore;

    private String sortBy;
}
