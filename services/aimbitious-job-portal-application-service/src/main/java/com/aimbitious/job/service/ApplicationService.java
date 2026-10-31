package com.aimbitious.job.service;

import com.aimbitious.job.domain.ApplicationStatus;
import com.aimbitious.job.dto.response.ApplicationResponse;
import com.aimbitious.job.exception.ApplicationException;
import com.aimbitious.job.exception.ResourceNotFoundException;
import com.aimbitious.job.model.Application;
import com.aimbitious.job.payload.CompanyApplicationFilterRequest;
import com.aimbitious.job.payload.CreateApplicationRequest;
import com.aimbitious.job.payload.UpdateApplicationStatusRequest;
import com.aimbitious.job.payload.WithdrawApplicationRequest;

import java.util.List;

public interface ApplicationService {

    ApplicationResponse createApplication(Long candidateId,
                                          CreateApplicationRequest req)
            throws ApplicationException;

    ApplicationResponse getApplicationById(Long id) throws ResourceNotFoundException;

    List<ApplicationResponse> getMyApplications(Long candidateId);

    List<ApplicationResponse> getApplicationsForJob(Long jobId);

    List<ApplicationResponse> getApplicationsForCompany(Long companyId,
                                                        CompanyApplicationFilterRequest filter) throws ResourceNotFoundException;

    ApplicationResponse updateStatus(Long applicationId,
                                     Long employerId,
                                     UpdateApplicationStatusRequest req)
            throws ResourceNotFoundException, ApplicationException;

    ApplicationResponse withdraw(Long applicationId, Long candidateId,
                                 WithdrawApplicationRequest req)
            throws ResourceNotFoundException, ApplicationException;

    ApplicationResponse markAsRead(Long applicationId, Long employerId)
            throws ResourceNotFoundException, ApplicationException;

    ApplicationResponse toggleStar(Long applicationId, Long employerId)
            throws ResourceNotFoundException, ApplicationException;

    void deleteApplication(Long applicationId, Long candidateId)
            throws ResourceNotFoundException, ApplicationException;

    /** Used internally by interview/note services. */
    Application getApplicationEntity(Long id) throws ResourceNotFoundException;

    /** Called by job-service after a job's requirements are edited — marks all existing scores as stale. */
    void markScreeningsStaleForJob(Long jobId);
}