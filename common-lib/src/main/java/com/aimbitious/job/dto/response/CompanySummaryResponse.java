package com.aimbitious.job.dto.response;

import com.aimbitious.job.domain.CompanySize;
import com.aimbitious.job.domain.IndustryType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompanySummaryResponse {

    private Long id;
    private String name;
    private String slug;
    private String logoUrl;
    private String tagline;
    private IndustryType industryType;
    private CompanySize companySize;
    private Boolean verified;

    /** HQ city and country for display on job cards. */
    private String city;
    private String country;
}
