package com.aimbitious.job.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.aimbitious.job.domain.SubscriptionPlan;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Full plan configuration — used on the pricing page.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlanConfigResponse {

    private Long id;
    private SubscriptionPlan plan;
    private String displayName;
    private String description;

    // Pricing
    private BigDecimal monthlyPrice;
    private BigDecimal yearlyPrice;
    private String currency;

    // Limits (-1 = unlimited)
    private Integer maxJobPostings;
    private Integer maxFeaturedJobs;
    private Integer maxResumeViews;
    private Integer jobPostingDurationDays;

    // Misc
    private String supportLevel;
    private Integer trialDays;
    private Boolean popular;
    private Boolean active;

    private List<PlanFeatureResponse> features;

}
