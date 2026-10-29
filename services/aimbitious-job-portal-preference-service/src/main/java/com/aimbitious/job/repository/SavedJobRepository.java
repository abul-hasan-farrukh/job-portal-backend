package com.aimbitious.job.repository;

import com.aimbitious.job.model.SavedJob;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SavedJobRepository extends JpaRepository<SavedJob, Long> {

    List<SavedJob> findByCandidateIdOrderBySavedAtDesc(Long candidateId);

    Optional<SavedJob> findByCandidateIdAndJobId(Long candidateId, Long jobId);

    boolean existsByCandidateIdAndJobId(Long candidateId, Long jobId);

    List<SavedJob> findByCandidateIdAndCompanyId(Long candidateId, Long companyId);
}
