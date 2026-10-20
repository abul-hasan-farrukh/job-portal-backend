package com.aimbitious.job.service;

import com.aimbitious.job.domain.ApplicationStatus;
import com.aimbitious.job.dto.ApplicationResponse;
import com.aimbitious.job.model.Application;
import com.aimbitious.job.payload.CompanyApplicationFilterRequest;
import com.aimbitious.job.payload.CreateApplicationRequest;
import com.aimbitious.job.payload.WithdrawApplicationRequest;

import java.util.List;

public interface ApplicationService {

    ApplicationResponse createApplication(
            Long candidateId,
            CreateApplicationRequest req
    ) throws Exception;

    ApplicationResponse getApplicationById(Long id) throws Exception;

    List<ApplicationResponse> getMyApplications(Long candidateId);

    List<ApplicationResponse> getApplicationsForJob(Long jobId);

    List<ApplicationResponse> getApplicationsForCompany(Long userId,
                                                        CompanyApplicationFilterRequest request);

    ApplicationResponse updateStatus(Long applicationId,
                                     Long employerId,
                                     ApplicationStatus status) throws Exception;

    ApplicationResponse withdraw(Long applicationId, Long candidateId,
                                 WithdrawApplicationRequest req) throws Exception;

    ApplicationResponse toggleStar(Long applicationId, Long employerId) throws Exception;

    void deleteApplication(Long applicationId, Long candidateId) throws Exception;

    Application getApplicationEntity(Long id) throws Exception;
}
