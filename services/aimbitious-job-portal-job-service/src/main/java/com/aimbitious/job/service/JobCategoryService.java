package com.aimbitious.job.service;

import com.aimbitious.job.dto.response.JobCategoryResponse;
import com.aimbitious.job.exception.JobException;
import com.aimbitious.job.exception.ResourceNotFoundException;
import com.aimbitious.job.model.JobCategory;
import com.aimbitious.job.payload.BulkJobCategoryRequest;
import com.aimbitious.job.payload.BulkJobCategoryResponse;
import com.aimbitious.job.payload.JobCategoryRequest;

import java.util.List;

public interface JobCategoryService {

    JobCategoryResponse createCategory(JobCategoryRequest req)
            throws JobException, ResourceNotFoundException;

    BulkJobCategoryResponse createCategoriesBulk(BulkJobCategoryRequest req);

    List<JobCategoryResponse> getAllCategories();

    List<JobCategoryResponse> getRootCategories();

    JobCategoryResponse getCategoryById(Long id) throws ResourceNotFoundException;

    JobCategoryResponse getCategoryBySlug(String slug) throws ResourceNotFoundException;

    JobCategoryResponse updateCategory(Long id, JobCategoryRequest req)
            throws ResourceNotFoundException, JobException;

    void deleteCategory(Long id) throws ResourceNotFoundException;

    /** Used internally. */
    JobCategory getCategoryEntityById(Long id) throws ResourceNotFoundException;
}
