package com.aimbitious.job.repository;

import com.aimbitious.job.domain.ParseStatus;
import com.aimbitious.job.model.ResumeParseJob;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResumeParseJobRepository extends JpaRepository<ResumeParseJob, Long> {

    List<ResumeParseJob> findByCandidateIdOrderByCreatedAtDesc(Long candidateId);

    List<ResumeParseJob> findByStatus(ParseStatus status);
}
