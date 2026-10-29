package com.aimbitious.job.repository;

import com.aimbitious.job.domain.SkillCategory;
import com.aimbitious.job.model.JobSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobSkillRepository extends JpaRepository<JobSkill, Long> {

    Optional<JobSkill> findBySlug(String slug);

    List<JobSkill> findByActiveTrue();

    List<JobSkill> findByCategoryAndActiveTrue(SkillCategory category);

    List<JobSkill> findByNameContainingIgnoreCaseAndActiveTrue(String keyword);

    boolean existsByName(String name);

    boolean existsBySlug(String slug);
}
