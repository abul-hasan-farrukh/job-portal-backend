package com.aimbitious.job.service;

import com.aimbitious.job.dto.response.JobAlertResponse;
import com.aimbitious.job.exception.ResourceNotFoundException;
import com.aimbitious.job.payload.CreateJobAlertRequest;
import com.aimbitious.job.payload.UpdateJobAlertRequest;

import java.util.List;

public interface JobAlertService {

    JobAlertResponse createAlert(Long candidateId, CreateJobAlertRequest req);

    JobAlertResponse getAlertById(Long alertId, Long candidateId) throws ResourceNotFoundException;

    List<JobAlertResponse> getMyAlerts(Long candidateId);

    JobAlertResponse updateAlert(Long alertId, Long candidateId, UpdateJobAlertRequest req)
            throws ResourceNotFoundException;

    void deleteAlert(Long alertId, Long candidateId) throws ResourceNotFoundException;

    JobAlertResponse toggleAlert(Long alertId, Long candidateId) throws ResourceNotFoundException;
}
