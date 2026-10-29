package com.aimbitious.job.controller;

import com.aimbitious.job.dto.response.ApiResponse;
import com.aimbitious.job.dto.response.AwardResponse;
import com.aimbitious.job.exception.ResourceNotFoundException;
import com.aimbitious.job.payload.AddAwardRequest;
import com.aimbitious.job.service.AwardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resumes/{resumeId}/awards")
@RequiredArgsConstructor
public class AwardController {

    private final AwardService awardService;

    @PostMapping
    public ResponseEntity<AwardResponse> addAward(
            @PathVariable Long resumeId,
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody @Valid com.aimbitious.job.payload.AddAwardRequest req) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(awardService.addAward(resumeId, candidateId, req));
    }

    @GetMapping
    public ResponseEntity<List<AwardResponse>> getAwards(
            @PathVariable Long resumeId) throws ResourceNotFoundException {
        return ResponseEntity.ok(awardService.getAwards(resumeId));
    }

    @PutMapping("/{awardId}")
    public ResponseEntity<AwardResponse> updateAward(
            @PathVariable Long resumeId,
            @PathVariable Long awardId,
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody @Valid AddAwardRequest req) throws ResourceNotFoundException {
        return ResponseEntity.ok(awardService.updateAward(awardId, resumeId, candidateId, req));
    }

    @DeleteMapping("/{awardId}")
    public ResponseEntity<ApiResponse> deleteAward(
            @PathVariable Long resumeId,
            @PathVariable Long awardId,
            @RequestHeader("X-User-Id") Long candidateId) throws ResourceNotFoundException {
        awardService.deleteAward(awardId, resumeId, candidateId);
        return ResponseEntity.ok(new ApiResponse("Award deleted successfully", true));
    }
}
