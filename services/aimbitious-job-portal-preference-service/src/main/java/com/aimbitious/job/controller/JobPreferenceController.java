package com.aimbitious.job.controller;

import com.aimbitious.job.dto.response.JobPreferenceResponse;
import com.aimbitious.job.payload.UpdateJobPreferenceRequest;
import com.aimbitious.job.service.JobPreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/preferences")
@RequiredArgsConstructor
public class JobPreferenceController {

    private final JobPreferenceService preferenceService;

    @GetMapping
    public ResponseEntity<JobPreferenceResponse> getOrCreatePreference(
            @RequestHeader("X-User-Id") Long candidateId) {
        return ResponseEntity.ok(preferenceService.getOrCreatePreference(candidateId));
    }

    @PutMapping
    public ResponseEntity<JobPreferenceResponse> updatePreference(
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody UpdateJobPreferenceRequest req) {
        return ResponseEntity.ok(preferenceService.updatePreference(candidateId, req));
    }
}
