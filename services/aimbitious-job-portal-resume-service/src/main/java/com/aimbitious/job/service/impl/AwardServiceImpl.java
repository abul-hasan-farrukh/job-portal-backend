package com.aimbitious.job.service.impl;

import com.aimbitious.job.dto.AwardResponse;
import com.aimbitious.job.mapper.ResumeMapper;
import com.aimbitious.job.model.Award;
import com.aimbitious.job.model.Resume;
import com.aimbitious.job.payload.AddAwardRequest;
import com.aimbitious.job.repository.AwardRepository;
import com.aimbitious.job.service.AwardService;
import com.aimbitious.job.service.ResumeService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AwardServiceImpl implements AwardService {

    private final AwardRepository awardRepository;
    private final ResumeService resumeService;

    @Override
    @Transactional
    public AwardResponse addAward(Long resumeId, Long candidateId, AddAwardRequest req)
            throws Exception {
        Resume resume = resumeService.getResumeEntity(resumeId);
        assertOwner(resume, candidateId, resumeId);

        Award award = Award.builder()
                .resume(resume)
                .title(req.getTitle())
                .issuedBy(req.getIssuedBy())
                .awardDate(req.getAwardDate())
                .description(req.getDescription())
                .displayOrder(req.getDisplayOrder() != null ? req.getDisplayOrder() : 0)
                .build();

        return ResumeMapper.toAwardResponse(awardRepository.save(award));
    }


    @Override
    @Transactional(readOnly = true)
    public List<AwardResponse> getAwards(Long resumeId) throws Exception {
        resumeService.getResumeEntity(resumeId);
        return awardRepository.findByResume_IdOrderByDisplayOrderAsc(resumeId)
                .stream().map(ResumeMapper::toAwardResponse).toList();
    }

    @Override
    @Transactional
    public AwardResponse updateAward(Long awardId, Long resumeId, Long candidateId,
                                     AddAwardRequest req) throws Exception {
        Award award = getAwardEntity(awardId, resumeId);
        assertOwner(award.getResume(), candidateId, resumeId);

        award.setTitle(req.getTitle());
        award.setIssuedBy(req.getIssuedBy());
        award.setAwardDate(req.getAwardDate());
        award.setDescription(req.getDescription());
        if (req.getDisplayOrder() != null) award.setDisplayOrder(req.getDisplayOrder());

        return ResumeMapper.toAwardResponse(awardRepository.save(award));
    }

    @Override
    @Transactional
    public void deleteAward(Long awardId, Long resumeId, Long candidateId)
            throws Exception {
        Award award = getAwardEntity(awardId, resumeId);
        assertOwner(award.getResume(), candidateId, resumeId);
        awardRepository.delete(award);
    }

    private Award getAwardEntity(Long awardId, Long resumeId) throws Exception {
        Award award = awardRepository.findById(awardId)
                .orElseThrow(() -> new Exception(
                        "Award not found with id: " + awardId));
        if (!award.getResume().getId().equals(resumeId)) {
            throw new Exception("Award not found with id: " + awardId);
        }
        return award;
    }

    private void assertOwner(Resume resume, Long candidateId, Long resumeId)
            throws Exception {
        if (!resume.getCandidateId().equals(candidateId)) {
            throw new Exception("Resume not found with id: " + resumeId);
        }
    }
}

