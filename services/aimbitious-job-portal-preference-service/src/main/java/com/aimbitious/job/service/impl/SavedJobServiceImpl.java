package com.aimbitious.job.service.impl;

import com.aimbitious.job.dto.response.SavedJobResponse;
import com.aimbitious.job.exception.ApplicationException;
import com.aimbitious.job.exception.ResourceNotFoundException;
import com.aimbitious.job.mapper.PreferenceMapper;
import com.aimbitious.job.model.SavedJob;
import com.aimbitious.job.payload.SaveJobRequest;
import com.aimbitious.job.repository.SavedJobRepository;
import com.aimbitious.job.service.SavedJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor

public class SavedJobServiceImpl implements SavedJobService {

    private final SavedJobRepository savedJobRepository;

    @Override
    @Transactional
    public SavedJobResponse saveJob(Long candidateId, SaveJobRequest req) throws ApplicationException {
        if (savedJobRepository.existsByCandidateIdAndJobId(candidateId, req.getJobId())) {
            throw new ApplicationException("Job is already saved");
        }

        SavedJob savedJob = SavedJob.builder()
                .candidateId(candidateId)
                .jobId(req.getJobId())
                .companyId(req.getCompanyId())
                .notes(req.getNotes())
                .build();

        return PreferenceMapper.toSavedJobResponse(savedJobRepository.save(savedJob));
    }

    @Override
    @Transactional
    public void unsaveJob(Long candidateId, Long savedJobId) throws ResourceNotFoundException {
        SavedJob savedJob = savedJobRepository.findById(savedJobId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Saved job not found with id: " + savedJobId));
        if (!savedJob.getCandidateId().equals(candidateId)) {
            throw new ResourceNotFoundException("Saved job not found with id: " + savedJobId);
        }
        savedJobRepository.delete(savedJob);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SavedJobResponse> getMySavedJobs(Long candidateId) {
        return savedJobRepository.findByCandidateIdOrderBySavedAtDesc(candidateId)
                .stream().map(PreferenceMapper::toSavedJobResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isSaved(Long candidateId, Long jobId) {
        return savedJobRepository.existsByCandidateIdAndJobId(candidateId, jobId);
    }
}
