package com.aimbitious.job.service;

import com.aimbitious.job.dto.response.JobRequest;
import com.aimbitious.job.dto.response.JobResponse;
import com.aimbitious.job.dto.response.JobSummaryResponse;
import com.aimbitious.job.exception.JobException;
import com.aimbitious.job.exception.ResourceNotFoundException;
import com.aimbitious.job.model.Job;
import com.aimbitious.job.payload.AiSearchRequest;
import com.aimbitious.job.payload.BulkJobRequest;
import com.aimbitious.job.payload.BulkJobResponse;
import com.aimbitious.job.payload.JobSearchRequest;

import java.util.List;

public interface JobService {

    JobResponse createJob(Long employerId, JobRequest req)
            throws ResourceNotFoundException, JobException;

    /**
     * Creates multiple jobs in one request.
     * Uses partial-success: each job is saved in its own transaction.
     * If some fail (e.g. invalid categoryId), the rest still succeed.
     * Returns a summary of succeeded and failed items.
     */
    BulkJobResponse createJobsBulk(Long employerId, BulkJobRequest req);

    JobResponse getJobById(Long id) throws ResourceNotFoundException;

    /** Lightweight summary — used by other services (e.g. application-service) via Feign. */
    JobSummaryResponse getJobSummaryById(Long id) throws ResourceNotFoundException;

    /**
     * Unified search + filter for jobs.
     * All params in {@link JobSearchRequest} are optional.
     * When no params are provided, returns all OPEN active jobs.
     */
    List<JobResponse> getJobs(JobSearchRequest req);

    /**
     * AI semantic search — placeholder for vector-embedding based search.
     * TODO: implement with embeddings when AI search is ready.
     */
    List<JobResponse> aiSearch(AiSearchRequest req);

    List<JobResponse> getJobsByCompany(Long companyId);

    List<JobResponse> getJobsByEmployer(Long employerId);

    List<JobResponse> getJobsByCategory(Long categoryId);

    JobResponse updateJob(Long jobId, Long employerId, JobRequest req)
            throws ResourceNotFoundException, JobException;

    JobResponse publishJob(Long jobId, Long employerId)
            throws ResourceNotFoundException, JobException;

    JobResponse closeJob(Long jobId, Long employerId)
            throws ResourceNotFoundException, JobException;

    void deleteJob(Long jobId, Long employerId)
            throws ResourceNotFoundException, JobException;

    void incrementViewCount(Long jobId) throws ResourceNotFoundException;

    void incrementApplicationCount(Long jobId) throws ResourceNotFoundException;

    /** Admin: returns all jobs regardless of status or active flag. */
    List<JobResponse> getAllJobsAdmin();

    /** Used internally by other services. */
    Job getJobEntityById(Long id) throws ResourceNotFoundException;

}
