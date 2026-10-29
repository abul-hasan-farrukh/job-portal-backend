package com.aimbitious.job.controller;

import com.aimbitious.job.domain.SkillCategory;
import com.aimbitious.job.dto.response.ApiResponse;
import com.aimbitious.job.dto.response.JobSkillResponse;
import com.aimbitious.job.exception.JobException;
import com.aimbitious.job.exception.ResourceNotFoundException;
import com.aimbitious.job.payload.BulkJobSkillRequest;
import com.aimbitious.job.payload.BulkJobSkillResponse;
import com.aimbitious.job.payload.JobSkillRequest;
import com.aimbitious.job.service.JobSkillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/job-skills")

public class JobSkillController {

    private final JobSkillService skillService;

    @PostMapping
    public ResponseEntity<JobSkillResponse> createSkill(
            @RequestBody @Valid JobSkillRequest req) throws JobException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(skillService.createSkill(req));
    }

    @PostMapping("/bulk")
    public ResponseEntity<BulkJobSkillResponse> createSkillsBulk(
            @RequestBody @Valid BulkJobSkillRequest req) {
        return ResponseEntity.status(HttpStatus.MULTI_STATUS)
                .body(skillService.createSkillsBulk(req));
    }

    @GetMapping
    public ResponseEntity<List<JobSkillResponse>> getAllSkills() {
        return ResponseEntity.ok(skillService.getAllSkills());
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<JobSkillResponse>> getSkillsByCategory(
            @PathVariable SkillCategory category) {
        return ResponseEntity.ok(skillService.getSkillsByCategory(category));
    }

    @GetMapping("/search")
    public ResponseEntity<List<JobSkillResponse>> searchSkills(
            @RequestParam String keyword) {
        return ResponseEntity.ok(skillService.searchSkills(keyword));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobSkillResponse> getSkillById(
            @PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(skillService.getSkillById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobSkillResponse> updateSkill(
            @PathVariable Long id,
            @RequestBody @Valid JobSkillRequest req)
            throws ResourceNotFoundException, JobException {
        return ResponseEntity.ok(skillService.updateSkill(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteSkill(
            @PathVariable Long id) throws ResourceNotFoundException {
        skillService.deleteSkill(id);
        return ResponseEntity.ok(new ApiResponse("Skill deleted successfully", true));
    }
}
