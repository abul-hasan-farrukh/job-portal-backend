package com.aimbitious.job.client;

import com.aimbitious.job.dto.response.JobResponse;
import com.aimbitious.job.dto.response.JobSummaryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "AIMBITIOUS-JOB-PORTAL-JOB-SERVICE")
public interface JobClient {

    @GetMapping("/api/jobs/{id}")
    JobResponse getJobById(@PathVariable("id") Long id);

    @GetMapping("/api/jobs/{id}/summary")
    JobSummaryResponse getJobSummaryById(@PathVariable("id") Long id);

}
