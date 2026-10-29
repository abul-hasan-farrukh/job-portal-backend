package com.aimbitious.job.service;

import com.aimbitious.job.dto.response.JobTagResponse;
import com.aimbitious.job.exception.JobException;
import com.aimbitious.job.exception.ResourceNotFoundException;
import com.aimbitious.job.model.JobTag;
import com.aimbitious.job.payload.BulkJobTagRequest;
import com.aimbitious.job.payload.BulkJobTagResponse;
import com.aimbitious.job.payload.JobTagRequest;

import java.util.List;
import java.util.Set;

public interface JobTagService {

    JobTagResponse createTag(JobTagRequest req) throws JobException;

    BulkJobTagResponse createTagsBulk(BulkJobTagRequest req);

    List<JobTagResponse> getAllTags();

    List<JobTagResponse> searchTags(String keyword);

    JobTagResponse getTagById(Long id) throws ResourceNotFoundException;

    JobTagResponse updateTag(Long id, JobTagRequest req)
            throws ResourceNotFoundException, JobException;

    void deleteTag(Long id) throws ResourceNotFoundException;

    /** Used internally to load tags by IDs for job creation. */
    Set<JobTag> getTagEntitiesByIds(Set<Long> ids) throws ResourceNotFoundException;
}
