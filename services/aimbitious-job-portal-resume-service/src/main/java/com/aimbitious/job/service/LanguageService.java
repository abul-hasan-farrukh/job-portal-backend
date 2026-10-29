package com.aimbitious.job.service;

import com.aimbitious.job.dto.response.LanguageResponse;
import com.aimbitious.job.exception.ResourceNotFoundException;
import com.aimbitious.job.payload.AddLanguageRequest;

import java.util.List;

public interface LanguageService {

    LanguageResponse addLanguage(Long resumeId, Long candidateId, AddLanguageRequest req)
            throws ResourceNotFoundException;

    List<LanguageResponse> getLanguages(Long resumeId) throws ResourceNotFoundException;

    LanguageResponse updateLanguage(Long languageId, Long resumeId, Long candidateId,
                                    AddLanguageRequest req) throws ResourceNotFoundException;

    void deleteLanguage(Long languageId, Long resumeId, Long candidateId)
            throws ResourceNotFoundException;
}
