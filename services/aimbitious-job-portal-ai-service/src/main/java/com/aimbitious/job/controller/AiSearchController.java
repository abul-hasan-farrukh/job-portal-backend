package com.aimbitious.job.controller;

import com.aimbitious.job.payload.JobAlertSuggestRequest;
import com.aimbitious.job.payload.JobAlertSuggestResponse;
import com.aimbitious.job.payload.SearchEnhanceRequest;
import com.aimbitious.job.payload.SearchEnhanceResponse;
import com.aimbitious.job.service.SearchAiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor

public class AiSearchController {
    private final SearchAiService searchAiService;

    @PostMapping("/enhance")
    public ResponseEntity<SearchEnhanceResponse> enhanceSearch(
            @Valid @RequestBody SearchEnhanceRequest request
    ) throws Exception {
        return ResponseEntity.ok(searchAiService.enhanceSearch(request));
    }

    @PostMapping("/alert-suggestion")
    public ResponseEntity<JobAlertSuggestResponse> suggestAlertCriteria(
            @Valid @RequestBody JobAlertSuggestRequest request
    ) throws Exception {
        return ResponseEntity.ok(searchAiService.suggestJobAlertCriteria(request));
    }
}
