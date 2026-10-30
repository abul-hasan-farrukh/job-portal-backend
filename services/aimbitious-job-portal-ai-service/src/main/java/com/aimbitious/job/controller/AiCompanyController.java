package com.aimbitious.job.controller;


import com.aimbitious.job.dto.response.ApiResponse;
import com.aimbitious.job.payload.AiTextResponse;
import com.aimbitious.job.payload.CompanyDescriptionRequest;
import com.aimbitious.job.payload.CompanyTaglineRequest;
import com.aimbitious.job.payload.CompanyTaglineResponse;
import com.aimbitious.job.service.CompanyAiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai/company")
@RequiredArgsConstructor
public class AiCompanyController {

    private final CompanyAiService companyAiService;

    /**
     * Phase 1: Generate company description for employer profile.
     * Used in CompanyProfile form.
     * POST /api/ai/company/describe
     */
    @PostMapping("/describe")
    public ResponseEntity<ApiResponse<AiTextResponse>> generateCompanyDescription(
            @Valid @RequestBody CompanyDescriptionRequest request) throws Exception {
        AiTextResponse response = companyAiService.generateCompanyDescription(request);
        return ResponseEntity.ok(ApiResponse.success("Company description generated", response));
    }

    /**
     * Phase 1: Generate 3 tagline options for the company.
     * Used in CompanyProfile form.
     * POST /api/ai/company/taglines
     */
    @PostMapping("/taglines")
    public ResponseEntity<ApiResponse<CompanyTaglineResponse>> generateTaglines(
            @Valid @RequestBody CompanyTaglineRequest request) throws Exception {
        CompanyTaglineResponse response = companyAiService.generateCompanyTaglines(request);
        return ResponseEntity.ok(ApiResponse.success("Taglines generated", response));
    }
}
