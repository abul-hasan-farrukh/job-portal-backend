package com.aimbitious.job.payload;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SearchEnhanceRequest {

    @NotNull( message = "query is required to search jobs")
    private String query;
}
