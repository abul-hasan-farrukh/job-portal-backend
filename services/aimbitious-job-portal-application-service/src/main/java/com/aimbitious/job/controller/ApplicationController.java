package com.aimbitious.job.controller;

import com.aimbitious.job.dto.response.ApiResponse;
import com.aimbitious.job.dto.response.ApplicationResponse;
import com.aimbitious.job.exception.ApplicationException;
import com.aimbitious.job.exception.ResourceNotFoundException;
import com.aimbitious.job.payload.CompanyApplicationFilterRequest;
import com.aimbitious.job.payload.CreateApplicationRequest;
import com.aimbitious.job.payload.UpdateApplicationStatusRequest;
import com.aimbitious.job.payload.WithdrawApplicationRequest;
import com.aimbitious.job.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/applications")

public class ApplicationController {

    private final ApplicationService applicationService;

    // ── Create ────────────────────────────────────────────────────────────────

    @PostMapping
    public ResponseEntity<ApplicationResponse> createApplication(
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody @Valid CreateApplicationRequest req)
            throws ApplicationException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(applicationService.createApplication(candidateId, req));
    }

    // ── Read ──────────────────────────────────────────────────────────────────

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponse> getApplicationById(
            @PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(applicationService.getApplicationById(id));
    }

    @GetMapping("/my")
    public ResponseEntity<List<ApplicationResponse>> getMyApplications(
            @RequestHeader("X-User-Id") Long candidateId) {
        return ResponseEntity.ok(applicationService.getMyApplications(candidateId));
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<ApplicationResponse>> getApplicationsForJob(
            @PathVariable Long jobId) {
        return ResponseEntity.ok(applicationService.getApplicationsForJob(jobId));
    }

    /**
     * Returns all applications for a company with optional filters.
     * Results are always ordered by appliedAt DESC (newest first).
     *
     * Filters: ?jobId=5 &status=REVIEWING &source=DIRECT
     *          &isRead=false &isStarred=true
     *          &appliedFrom=2025-01-01 &appliedTo=2025-03-31
     */
    @GetMapping("/company")
    public ResponseEntity<List<ApplicationResponse>> getApplicationsForCompany(
            @RequestHeader("X-User-Id") Long userId,
            @ModelAttribute CompanyApplicationFilterRequest filter) throws ResourceNotFoundException {
        return ResponseEntity.ok(applicationService.getApplicationsForCompany(
                userId, filter));
    }

    // ── Status transitions ────────────────────────────────────────────────────

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApplicationResponse> updateStatus(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long employerId,
            @RequestBody @Valid UpdateApplicationStatusRequest req)
            throws ResourceNotFoundException, ApplicationException {
        return ResponseEntity.ok(applicationService.updateStatus(id, employerId, req));
    }

    @PatchMapping("/{id}/withdraw")
    public ResponseEntity<ApplicationResponse> withdraw(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody WithdrawApplicationRequest req)
            throws ResourceNotFoundException, ApplicationException {
        return ResponseEntity.ok(applicationService.withdraw(id, candidateId, req));
    }

    // ── Tracking flags ────────────────────────────────────────────────────────

    @PatchMapping("/{id}/read")
    public ResponseEntity<ApplicationResponse> markAsRead(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long employerId)
            throws ResourceNotFoundException, ApplicationException {
        return ResponseEntity.ok(applicationService.markAsRead(id, employerId));
    }

    @PatchMapping("/{id}/star")
    public ResponseEntity<ApplicationResponse> toggleStar(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long employerId)
            throws ResourceNotFoundException, ApplicationException {
        return ResponseEntity.ok(applicationService.toggleStar(id, employerId));
    }

    // ── Internal (called by job-service after a job is edited) ───────────────

    @PostMapping("/internal/jobs/{jobId}/mark-stale")
    public ResponseEntity<Void> markScreeningsStale(@PathVariable Long jobId) {
        applicationService.markScreeningsStaleForJob(jobId);
        return ResponseEntity.noContent().build();
    }

    // ── Delete ────────────────────────────────────────────────────────────────

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteApplication(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long candidateId)
            throws ResourceNotFoundException, ApplicationException {
        applicationService.deleteApplication(id, candidateId);
        return ResponseEntity.ok(new ApiResponse("Application deleted successfully", true));
    }
}
