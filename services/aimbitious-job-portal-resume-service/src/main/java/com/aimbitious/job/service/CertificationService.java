package com.aimbitious.job.service;

import com.aimbitious.job.dto.CertificationResponse;
import com.aimbitious.job.payload.AddCertificationRequest;

import java.util.List;

public interface CertificationService {

    CertificationResponse addCertification(Long resumeId, Long candidateId, AddCertificationRequest req)
            throws Exception;

    List<CertificationResponse> getCertifications(Long resumeId) throws Exception;

    CertificationResponse updateCertification(Long certificationId, Long resumeId, Long candidateId,
                                              AddCertificationRequest req) throws Exception;

    void deleteCertification(Long certificationId, Long resumeId, Long candidateId)
            throws Exception;
}
