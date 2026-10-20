package com.aimbitious.job.client;

import com.aimbitious.job.dto.CompanyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "AIMBITIOUS-JOB-PORTAL-COMPANY-SERVICE")
public interface CompanyClient {

    @GetMapping("/api/companies/{id}")
    CompanyResponse getCompanyById(
            @PathVariable Long id
    );

    @GetMapping("/api/companies/my")
    CompanyResponse getMyCompany(
            @RequestHeader("X-User-Id")
            Long ownerId);
}
