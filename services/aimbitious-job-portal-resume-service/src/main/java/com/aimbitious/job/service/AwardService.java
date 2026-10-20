package com.aimbitious.job.service;

import com.aimbitious.job.dto.AwardResponse;
import com.aimbitious.job.payload.AddAwardRequest;

import java.util.List;

public interface AwardService {

    AwardResponse addAward(Long resumeId, Long candidateId, AddAwardRequest req)
            throws Exception;

    List<AwardResponse> getAwards(Long resumeId) throws Exception;

    AwardResponse updateAward(Long awardId, Long resumeId, Long candidateId, AddAwardRequest req)
            throws Exception;

    void deleteAward(Long awardId, Long resumeId, Long candidateId) throws Exception;
}