package com.aimbitious.job.service;

import com.aimbitious.job.dto.SavedJobResponse;
import com.aimbitious.job.mapper.PreferenceMapper;
import com.aimbitious.job.model.SavedJob;
import com.aimbitious.job.payload.SaveJobRequest;
import com.aimbitious.job.repository.SavedJobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class SavedJobServiceImpl implements SavedJobService {

    private final SavedJobRepository savedJobRepository;

    @Override
    public SavedJobResponse saveJob(Long candidateId, SaveJobRequest req) throws Exception {
        if (isSaved(candidateId, req.getJobId())) {
            throw new Exception("job already saved");
        }
        SavedJob savedJob = SavedJob.builder()
                .candidateId(candidateId)
                .jobId(req.getJobId())
                .build();
        savedJob = savedJobRepository.save(savedJob);
        return PreferenceMapper.toSavedJobResponse(savedJob);
    }

    @Override
    public void unsaveJob(Long candidateId, Long savedJobId) throws Exception {
        SavedJob savedJob = savedJobRepository.findById(savedJobId).orElseThrow(
                () -> new Exception("job not found")
        );
        if (!savedJob.getCandidateId().equals(candidateId)) {
            throw new Exception("job not saved");
        }
        savedJobRepository.delete(savedJob);
    }

    @Override
    public List<SavedJobResponse> getSavedJob(Long candidateId) {
        return savedJobRepository.findByCandidateId(candidateId)
                .stream().map(PreferenceMapper::toSavedJobResponse).toList();
    }

    @Override
    public boolean isSaved(Long candidateId, Long jobId) {
        return savedJobRepository.existsByCandidateIdAndJobId(candidateId, jobId);
    }
}
