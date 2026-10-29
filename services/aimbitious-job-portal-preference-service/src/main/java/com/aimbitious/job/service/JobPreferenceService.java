package com.aimbitious.job.service;

import com.aimbitious.job.dto.response.JobPreferenceResponse;
import com.aimbitious.job.payload.UpdateJobPreferenceRequest;

public interface JobPreferenceService {

    JobPreferenceResponse getOrCreatePreference(Long candidateId);

    JobPreferenceResponse updatePreference(Long candidateId, UpdateJobPreferenceRequest req);
}
