package com.aimbitious.job.repository;

import com.aimbitious.job.model.Award;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AwardRepository extends JpaRepository<Award, Long> {

    List<Award> findByResume_IdOrderByDisplayOrderAsc(Long resumeId);
}