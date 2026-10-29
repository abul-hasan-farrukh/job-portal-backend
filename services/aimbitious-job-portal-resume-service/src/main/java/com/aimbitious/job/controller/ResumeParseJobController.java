package com.aimbitious.job.controller;

import org.springframework.web.bind.annotation.PostMapping;

import com.aimbitious.job.dto.response.ResumeParseJobResponse;
import com.aimbitious.job.exception.ResourceNotFoundException;
import com.aimbitious.job.payload.ParseResumeRequest;
import com.aimbitious.job.service.ResumeParseJobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resumes/parse-jobs")
@RequiredArgsConstructor
public class ResumeParseJobController {

    private final ResumeParseJobService parseJobService;

    @PostMapping
    public ResponseEntity<ResumeParseJobResponse> submitParseJob(
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody @Valid ParseResumeRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(parseJobService.submitParseJob(candidateId, req));
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<ResumeParseJobResponse> getParseJob(
            @PathVariable Long jobId,
            @RequestHeader("X-User-Id") Long candidateId) throws ResourceNotFoundException {
        return ResponseEntity.ok(parseJobService.getParseJob(jobId, candidateId));
    }

    @GetMapping("/my")
    public ResponseEntity<List<ResumeParseJobResponse>> getMyParseJobs(
            @RequestHeader("X-User-Id") Long candidateId) {
        return ResponseEntity.ok(parseJobService.getMyParseJobs(candidateId));
    }
}
