package com.aimbitious.job.service;

import com.aimbitious.job.domain.CompanyStatus;
import com.aimbitious.job.domain.CompanyType;
import com.aimbitious.job.domain.IndustryType;
import com.aimbitious.job.dto.response.CompanyRequest;
import com.aimbitious.job.dto.response.CompanyResponse;
import com.aimbitious.job.dto.response.CompanySummaryResponse;
import com.aimbitious.job.exception.CompanyException;
import com.aimbitious.job.exception.ResourceNotFoundException;
import com.aimbitious.job.model.Company;

import java.util.List;

public interface CompanyService {

    CompanyResponse createCompany(Long ownerId, CompanyRequest req) throws CompanyException;

    CompanyResponse getCompanyById(Long id) throws ResourceNotFoundException;

    CompanySummaryResponse getCompanySummaryById(Long id) throws ResourceNotFoundException;

    CompanyResponse getMyCompany(Long ownerId) throws ResourceNotFoundException;

    List<CompanyResponse> getAllCompanies(
            CompanyType companyType,
            IndustryType industryType,
            CompanyStatus status);

    CompanyResponse updateCompany(Long companyId,
                                  Long ownerId, CompanyRequest req)
            throws ResourceNotFoundException, CompanyException;

    CompanyResponse verifyCompany(Long companyId) throws ResourceNotFoundException;

    CompanyResponse deactivateCompany(Long companyId) throws ResourceNotFoundException;

    void deleteCompany(Long companyId, Long ownerId) throws ResourceNotFoundException, CompanyException;

    /** Used internally by other services (e.g. location service). */
    Company getCompanyEntityById(Long id) throws ResourceNotFoundException;
}
