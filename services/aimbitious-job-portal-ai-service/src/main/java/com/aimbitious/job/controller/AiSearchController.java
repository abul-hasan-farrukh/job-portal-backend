package com.aimbitious.job.controller;

import com.aimbitious.job.dto.response.ApiResponse;
import com.aimbitious.job.payload.*;
import com.aimbitious.job.service.SearchAiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai/search")
public class AiSearchController {
    private final SearchAiService searchAiService;


    /**
     * Phase 3: Convert natural language search query into structured filters.
     * Called by job-service POST /api/jobs/search (which already has the stub).
     * Also called directly from the Jobs search bar in the frontend.
     * POST /api/ai/search/enhance
     */
    @PostMapping("/enhance")
    public ResponseEntity<ApiResponse<SearchEnhanceResponse>> enhanceSearch(
            @Valid @RequestBody SearchEnhanceRequest request
    ) throws Exception {
        SearchEnhanceResponse response = searchAiService.enhanceSearch(request);
        return ResponseEntity.ok(ApiResponse.success("Search enhanced", response));
    }

    /**
     * Phase 4: Calculate how well a job matches a candidate's preferences.
     * Used in the SavedJobs and Jobs listing to show match percentage.
     * POST /api/ai/search/job-match
     */
    @PostMapping("/job-match")
    public ResponseEntity<ApiResponse<JobMatchResponse>> calculateJobMatch(
            @RequestBody JobMatchRequest request) throws Exception {
        JobMatchResponse response = searchAiService.calculateJobMatch(request);
        return ResponseEntity.ok(ApiResponse.success("Job match calculated", response));
    }


    /**
     * Phase 4: Suggest optimal job alert criteria based on candidate profile.
     * Used when candidate creates a new Job Alert.
     * POST /api/ai/search/alert-suggestion
     */
    @PostMapping("/alert-suggestion")
    public ResponseEntity<ApiResponse<JobAlertSuggestResponse>> suggestAlertCriteria(
            @Valid @RequestBody JobAlertSuggestRequest request
    ) throws Exception {
        JobAlertSuggestResponse response = searchAiService.suggestJobAlertCriteria(request);
        return ResponseEntity.ok(ApiResponse.success("Alert criteria suggested", response));
    }
}
