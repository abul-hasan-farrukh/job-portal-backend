package com.aimbitious.job.client;


import com.aimbitious.job.dto.response.ApiResponse;
import com.aimbitious.job.payload.ScreeningScoreRequest;
import com.aimbitious.job.payload.ScreeningScoreResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "AIMBITIOUS-JOB-PORTAL-AI-SERVICE")
public interface AiClient {
    @PostMapping("/api/ai/application/screening-score")
    ApiResponse<ScreeningScoreResponse> scoreCandidate(@RequestBody ScreeningScoreRequest request);
}
