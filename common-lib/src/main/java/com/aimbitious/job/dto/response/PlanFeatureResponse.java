package com.aimbitious.job.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlanFeatureResponse {

    private String featureText;
    private Boolean highlighted;
}
