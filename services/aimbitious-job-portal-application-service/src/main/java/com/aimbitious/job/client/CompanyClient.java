package com.aimbitious.job.client;

import com.aimbitious.job.dto.response.CompanyResponse;
import com.aimbitious.job.dto.response.CompanySummaryResponse;
import com.aimbitious.job.exception.ResourceNotFoundException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "AIMBITIOUS-JOB-PORTAL-COMPANY-SERVICE")
public interface CompanyClient {

    @GetMapping("/api/companies/my")
    CompanyResponse getMyCompany(
            @RequestHeader("X-User-Id") Long ownerId) throws ResourceNotFoundException;

    @GetMapping("/api/companies/summary/{id}")
    CompanySummaryResponse getCompanySummaryById(@PathVariable("id") Long id);
}
