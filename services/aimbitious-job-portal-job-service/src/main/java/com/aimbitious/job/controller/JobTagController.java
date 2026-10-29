package com.aimbitious.job.controller;

import com.aimbitious.job.dto.response.ApiResponse;
import com.aimbitious.job.dto.response.JobTagResponse;
import com.aimbitious.job.exception.JobException;
import com.aimbitious.job.exception.ResourceNotFoundException;
import com.aimbitious.job.payload.BulkJobTagRequest;
import com.aimbitious.job.payload.BulkJobTagResponse;
import com.aimbitious.job.payload.JobTagRequest;
import com.aimbitious.job.service.JobTagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/job-tags")

public class JobTagController {
    private final JobTagService tagService;

    @PostMapping
    public ResponseEntity<JobTagResponse> createTag(
            @RequestBody @Valid JobTagRequest req) throws JobException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tagService.createTag(req));
    }

    @PostMapping("/bulk")
    public ResponseEntity<BulkJobTagResponse> createTagsBulk(
            @RequestBody @Valid BulkJobTagRequest req) {
        return ResponseEntity.status(HttpStatus.MULTI_STATUS)
                .body(tagService.createTagsBulk(req));
    }

    @GetMapping
    public ResponseEntity<List<JobTagResponse>> getAllTags() {
        return ResponseEntity.ok(tagService.getAllTags());
    }

    @GetMapping("/search")
    public ResponseEntity<List<JobTagResponse>> searchTags(
            @RequestParam String keyword) {
        return ResponseEntity.ok(tagService.searchTags(keyword));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobTagResponse> getTagById(
            @PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(tagService.getTagById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobTagResponse> updateTag(
            @PathVariable Long id,
            @RequestBody @Valid JobTagRequest req)
            throws ResourceNotFoundException, JobException {
        return ResponseEntity.ok(tagService.updateTag(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteTag(
            @PathVariable Long id) throws ResourceNotFoundException {
        tagService.deleteTag(id);
        return ResponseEntity.ok(new ApiResponse("Tag deleted successfully", true));
    }
}
