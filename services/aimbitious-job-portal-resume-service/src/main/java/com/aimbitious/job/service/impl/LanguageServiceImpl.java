package com.aimbitious.job.service.impl;

import com.aimbitious.job.dto.LanguageResponse;
import com.aimbitious.job.mapper.ResumeMapper;
import com.aimbitious.job.model.Language;
import com.aimbitious.job.model.Project;
import com.aimbitious.job.model.Resume;
import com.aimbitious.job.payload.AddLanguageRequest;
import com.aimbitious.job.repository.LanguageRepository;
import com.aimbitious.job.service.LanguageService;
import com.aimbitious.job.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LanguageServiceImpl implements LanguageService {

    private final ResumeService resumeService;
    private final LanguageRepository languageRepository;

    @Override
    public LanguageResponse addLanguage(Long resumeId, Long candidateId, AddLanguageRequest req) throws Exception {
        Resume resume = resumeService.getResumeEntity(resumeId);
        assertOwner(resume, candidateId);

        Language lang = Language.builder()
                .resume(resume)
                .languageName(req.getLanguageName())
                .proficiency(req.getProficiency())
                .displayOrder(req.getDisplayOrder() != null ? req.getDisplayOrder() : 0)
                .build();

        Language savedLanguage = languageRepository.save(lang);
        return ResumeMapper.toLanguageResponse(savedLanguage);
    }

    @Override
    public List<LanguageResponse> getLanguages(Long resumeId) {
        return languageRepository.findByResume_IdOrderByDisplayOrderAsc(resumeId)
                .stream().map(ResumeMapper::toLanguageResponse).toList();

    }

    @Override
    public LanguageResponse updateLanguage(Long languageId, Long resumeId, Long candidateId, AddLanguageRequest req) throws Exception {
        Language lang = languageRepository.findById(languageId).orElseThrow(
                () -> new Exception("language does not exist")
        );

        assertOwner(lang.getResume(), candidateId);

        lang.setLanguageName(req.getLanguageName());
        lang.setProficiency(req.getProficiency());

        if (req.getDisplayOrder() != null)
            lang.setDisplayOrder(req.getDisplayOrder());

        return ResumeMapper.toLanguageResponse(
                languageRepository.save(lang)
        );
    }

    @Override
    public void deleteLanguage(Long languageId, Long resumeId, Long candidateId) throws Exception {
        Language lang = languageRepository.findById(languageId).orElseThrow(
                () -> new Exception("language does not exist")
        );

        assertOwner(lang.getResume(), candidateId);
        languageRepository.delete(lang);
    }

    private void assertOwner(Resume resume, Long candidateId) throws Exception {
        if (!resume.getCandidateId().equals(candidateId)) {
            throw new Exception("resume not found");
        }
    }
}
