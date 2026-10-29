package com.aimbitious.job.service.impl;

import com.aimbitious.job.dto.response.JobCategoryResponse;
import com.aimbitious.job.exception.JobException;
import com.aimbitious.job.exception.ResourceNotFoundException;
import com.aimbitious.job.mapper.JobMapper;
import com.aimbitious.job.model.JobCategory;
import com.aimbitious.job.payload.BulkJobCategoryFailure;
import com.aimbitious.job.payload.BulkJobCategoryRequest;
import com.aimbitious.job.payload.BulkJobCategoryResponse;
import com.aimbitious.job.payload.JobCategoryRequest;
import com.aimbitious.job.repository.JobCategoryRepository;
import com.aimbitious.job.service.JobCategoryService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobCategoryServiceImpl implements JobCategoryService {

    private final JobCategoryRepository categoryRepository;

    // ── Create ────────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public JobCategoryResponse createCategory(JobCategoryRequest req)
            throws JobException, ResourceNotFoundException {
        if (categoryRepository.existsByName(req.getName())) {
            throw new JobException("Category with name '" + req.getName() + "' already exists");
        }

        JobCategory parent = null;
        if (req.getParentId() != null) {
            parent = getCategoryEntityById(req.getParentId());
        }

        String slug = generateUniqueSlug(req.getName());

        JobCategory category = JobCategory.builder()
                .name(req.getName())
                .slug(slug)
                .description(req.getDescription())
                .iconUrl(req.getIconUrl())
                .parent(parent)
                .build();

        return JobMapper.toCategoryResponse(categoryRepository.save(category),
                true);
    }

    @Override
    public BulkJobCategoryResponse createCategoriesBulk(BulkJobCategoryRequest req) {
        List<JobCategoryResponse>    succeeded = new ArrayList<>();
        List<BulkJobCategoryFailure> failed    = new ArrayList<>();

        List<JobCategoryRequest> categories = req.getCategories();
        for (int i = 0; i < categories.size(); i++) {
            JobCategoryRequest catReq = categories.get(i);
            try {
                succeeded.add(createCategory(catReq));
            } catch (Exception e) {
                failed.add(BulkJobCategoryFailure.builder()
                        .index(i)
                        .name(catReq.getName())
                        .error(e.getMessage())
                        .build());
            }
        }

        return BulkJobCategoryResponse.builder()
                .totalRequested(categories.size())
                .totalSucceeded(succeeded.size())
                .totalFailed(failed.size())
                .succeeded(succeeded)
                .failed(failed)
                .build();
    }

    // ── Read ──────────────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<JobCategoryResponse> getAllCategories() {
        return categoryRepository.findByActiveTrue().stream()
                .map(c -> JobMapper.toCategoryResponse(c, false))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobCategoryResponse> getRootCategories() {
        return categoryRepository.findByParentIsNullAndActiveTrue().stream()
                .map(c -> JobMapper.toCategoryResponse(c, true))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public JobCategoryResponse getCategoryById(Long id) throws ResourceNotFoundException {
        return JobMapper.toCategoryResponse(
                getCategoryEntityById(id), true);
    }

    @Override
    @Transactional(readOnly = true)
    public JobCategoryResponse getCategoryBySlug(String slug) throws ResourceNotFoundException {
        JobCategory category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with slug: " + slug));
        return JobMapper.toCategoryResponse(category, true);
    }

    // ── Update ────────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public JobCategoryResponse updateCategory(Long id, JobCategoryRequest req)
            throws ResourceNotFoundException, JobException {
        JobCategory category = getCategoryEntityById(id);

        if (!category.getName().equals(req.getName())
                && categoryRepository.existsByName(req.getName())) {
            throw new JobException("Category with name '" + req.getName() + "' already exists");
        }

        JobCategory parent = null;
        if (req.getParentId() != null) {
            if (req.getParentId().equals(id)) {
                throw new JobException("A category cannot be its own parent");
            }
            parent = getCategoryEntityById(req.getParentId());
        }

        category.setName(req.getName());
        category.setDescription(req.getDescription());
        category.setIconUrl(req.getIconUrl());
        category.setParent(parent);

        return JobMapper.toCategoryResponse(
                categoryRepository.save(category), true);
    }

    // ── Delete ────────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public void deleteCategory(Long id) throws ResourceNotFoundException {
        JobCategory category = getCategoryEntityById(id);
        category.setActive(false);
        categoryRepository.save(category);
    }

    // ── Internal ──────────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public JobCategory getCategoryEntityById(Long id) throws ResourceNotFoundException {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Job category not found with id: " + id));
    }

    // ── Private utilities ─────────────────────────────────────────────────────

    private String generateUniqueSlug(String name) {
        String base = name.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .trim()
                .replaceAll("[\\s-]+", "-");
        if (!categoryRepository.existsBySlug(base)) {
            return base;
        }
        int counter = 1;
        while (categoryRepository.existsBySlug(base + "-" + counter)) {
            counter++;
        }
        return base + "-" + counter;
    }
}
