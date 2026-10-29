package com.aimbitious.job.repository;

import com.aimbitious.job.model.JobAlert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobAlertRepository extends JpaRepository<JobAlert, Long> {

    List<JobAlert> findByCandidateIdOrderByCreatedAtDesc(Long candidateId);

    List<JobAlert> findByCandidateIdAndIsActiveTrue(Long candidateId);

    List<JobAlert> findByIsActiveTrue();

}
