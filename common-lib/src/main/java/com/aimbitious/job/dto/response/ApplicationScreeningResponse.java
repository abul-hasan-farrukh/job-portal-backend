package com.aimbitious.job.dto.response;

import java.time.LocalDateTime;


import com.aimbitious.job.domain.AiShortListStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.util.List;

/**
 * AI screening result attached to an application response.
 * Populated once the background scoring job completes.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationScreeningResponse {

    private Long id;
    private Integer overallScore;
    private Integer skillsMatchScore;
    private Integer experienceMatchScore;
    private Integer educationMatchScore;
    private AiShortListStatus shortlistStatus;

    private String summary;
    private List<String> matchedSkills;
    private List<String> missingSkills;
    private List<String> strengths;
    private List<String> concerns;

    /** True if the job was edited after scoring — score may be outdated. */
    private Boolean isStale;

    private LocalDateTime screenedAt;

    /** Prompt/model version used — helps compare scores across versions. */
    private String screeningVersion;

}
