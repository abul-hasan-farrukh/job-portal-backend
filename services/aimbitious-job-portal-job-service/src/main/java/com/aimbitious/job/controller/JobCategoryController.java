package com.aimbitious.job.controller;

import com.aimbitious.job.dto.response.ApiResponse;
import com.aimbitious.job.dto.response.JobCategoryResponse;
import com.aimbitious.job.exception.JobException;
import com.aimbitious.job.exception.ResourceNotFoundException;
import com.aimbitious.job.payload.BulkJobCategoryRequest;
import com.aimbitious.job.payload.BulkJobCategoryResponse;
import com.aimbitious.job.payload.JobCategoryRequest;
import com.aimbitious.job.service.JobCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/job-categories")

public class JobCategoryController {

    private final JobCategoryService categoryService;

    @PostMapping
    public ResponseEntity<JobCategoryResponse> createCategory(
            @RequestBody @Valid JobCategoryRequest req)
            throws JobException, ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoryService.createCategory(req));
    }

    @PostMapping("/bulk")
    public ResponseEntity<BulkJobCategoryResponse> createCategoriesBulk(
            @RequestBody @Valid BulkJobCategoryRequest req) {
        return ResponseEntity.status(HttpStatus.MULTI_STATUS)
                .body(categoryService.createCategoriesBulk(req));
    }

    @GetMapping
    public ResponseEntity<List<JobCategoryResponse>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/root")
    public ResponseEntity<List<JobCategoryResponse>> getRootCategories() {
        return ResponseEntity.ok(categoryService.getRootCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobCategoryResponse> getCategoryById(
            @PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<JobCategoryResponse> getCategoryBySlug(
            @PathVariable String slug) throws ResourceNotFoundException {
        return ResponseEntity.ok(categoryService.getCategoryBySlug(slug));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobCategoryResponse> updateCategory(
            @PathVariable Long id,
            @RequestBody @Valid JobCategoryRequest req)
            throws ResourceNotFoundException, JobException {
        return ResponseEntity.ok(categoryService.updateCategory(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCategory(
            @PathVariable Long id) throws ResourceNotFoundException {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(new ApiResponse("Category deleted successfully", true));
    }
}
