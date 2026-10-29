package com.aimbitious.job.service;

import com.aimbitious.job.dto.response.AwardResponse;
import com.aimbitious.job.exception.ResourceNotFoundException;
import com.aimbitious.job.payload.AddAwardRequest;

import java.util.List;

public interface AwardService {

    AwardResponse addAward(Long resumeId, Long candidateId, AddAwardRequest req)
            throws ResourceNotFoundException;

    List<AwardResponse> getAwards(Long resumeId) throws ResourceNotFoundException;

    AwardResponse updateAward(Long awardId, Long resumeId, Long candidateId, AddAwardRequest req)
            throws ResourceNotFoundException;

    void deleteAward(Long awardId, Long resumeId, Long candidateId) throws ResourceNotFoundException;
}