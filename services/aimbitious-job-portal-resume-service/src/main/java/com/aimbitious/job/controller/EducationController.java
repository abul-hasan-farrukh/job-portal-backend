package com.aimbitious.job.controller;


import com.aimbitious.job.dto.ApiResponse;
import com.aimbitious.job.dto.EducationResponse;
import com.aimbitious.job.payload.AddEducationRequest;
import com.aimbitious.job.service.EducationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/resumes/{resumeId}/educations")

public class EducationController {
    private final EducationService educationService;

    @PostMapping
    public ResponseEntity<EducationResponse> addEducation(
            @PathVariable("resumeId") Long resumeId,
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody @Valid AddEducationRequest req
    ) throws Exception {

        return ResponseEntity.ok(
                educationService.addEducation(resumeId, candidateId, req)
        );
    }

    @GetMapping
    public ResponseEntity<List<EducationResponse>> getEducations(
            @PathVariable Long resumeId) throws Exception {
        return ResponseEntity.ok(
                educationService.getEducations(resumeId)
        );
    }

    @PutMapping("/{educationId}")
    public ResponseEntity<EducationResponse> updateEducation(
            @PathVariable Long resumeId,
            @PathVariable Long educationId,
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody @Valid AddEducationRequest req) throws Exception {

        return ResponseEntity.ok(
                educationService.updateEducation(educationId, resumeId, candidateId, req));
    }

    @DeleteMapping("/{educationId}")
    public ResponseEntity<ApiResponse> deleteEducation(
            @PathVariable Long resumeId,
            @PathVariable Long educationId,
            @RequestHeader("X-User-Id") Long candidateId) throws Exception {

        educationService.deleteEducation(educationId, resumeId, candidateId);

        return ResponseEntity.ok(
                new ApiResponse("Education deleted successfully", true));
    }
}
