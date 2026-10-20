package com.aimbitious.job.repository;

import com.aimbitious.job.model.Certification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CertificationRepository extends JpaRepository<Certification, Long> {

    List<Certification> findByResume_IdOrderByDisplayOrderAsc(Long resumeId);
}

