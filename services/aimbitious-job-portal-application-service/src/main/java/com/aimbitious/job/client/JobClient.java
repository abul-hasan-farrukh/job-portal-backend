package com.aimbitious.job.client;

import com.aimbitious.job.dto.JobResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "AIMBITIOUS-JOB-PORTAL-JOB-SERVICE")
public interface JobClient {

    @GetMapping("/api/jobs/{id}")
    JobResponse getJobById(
            @PathVariable Long id);


}
