package com.aimbitious.job.controller;

import com.aimbitious.job.dto.response.ApiResponse;
import com.aimbitious.job.dto.response.CertificationResponse;
import com.aimbitious.job.exception.ResourceNotFoundException;
import com.aimbitious.job.payload.AddCertificationRequest;
import com.aimbitious.job.service.CertificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resumes/{resumeId}/certifications")
@RequiredArgsConstructor
public class CertificationController {

    private final CertificationService certificationService;

    @PostMapping
    public ResponseEntity<CertificationResponse> addCertification(
            @PathVariable Long resumeId,
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody @Valid AddCertificationRequest req) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(certificationService.addCertification(resumeId, candidateId, req));
    }

    @GetMapping
    public ResponseEntity<List<CertificationResponse>> getCertifications(
            @PathVariable Long resumeId) throws ResourceNotFoundException {
        return ResponseEntity.ok(certificationService.getCertifications(resumeId));
    }

    @PutMapping("/{certificationId}")
    public ResponseEntity<CertificationResponse> updateCertification(
            @PathVariable Long resumeId,
            @PathVariable Long certificationId,
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody @Valid AddCertificationRequest req) throws ResourceNotFoundException {
        return ResponseEntity.ok(
                certificationService.updateCertification(certificationId, resumeId, candidateId, req));
    }

    @DeleteMapping("/{certificationId}")
    public ResponseEntity<ApiResponse> deleteCertification(
            @PathVariable Long resumeId,
            @PathVariable Long certificationId,
            @RequestHeader("X-User-Id") Long candidateId) throws ResourceNotFoundException {
        certificationService.deleteCertification(certificationId, resumeId, candidateId);
        return ResponseEntity.ok(new ApiResponse("Certification deleted successfully", true));
    }
}
