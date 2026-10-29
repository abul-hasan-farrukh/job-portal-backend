package com.aimbitious.job.service;

import com.aimbitious.job.dto.response.CertificationResponse;
import com.aimbitious.job.exception.ResourceNotFoundException;
import com.aimbitious.job.payload.AddCertificationRequest;

import java.util.List;

public interface CertificationService {

    CertificationResponse addCertification(Long resumeId, Long candidateId, AddCertificationRequest req)
            throws ResourceNotFoundException;

    List<CertificationResponse> getCertifications(Long resumeId) throws ResourceNotFoundException;

    CertificationResponse updateCertification(Long certificationId, Long resumeId, Long candidateId,
                                              AddCertificationRequest req) throws ResourceNotFoundException;

    void deleteCertification(Long certificationId, Long resumeId, Long candidateId)
            throws ResourceNotFoundException;
}
