package com.aimbitious.job.repository;

import com.aimbitious.job.model.ApplicationNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationNoteRepository extends JpaRepository<ApplicationNote, Long> {

    List<ApplicationNote> findByApplicationIdOrderByCreatedAtDesc(Long applicationId);

    void deleteByApplicationId(Long applicationId);
}
