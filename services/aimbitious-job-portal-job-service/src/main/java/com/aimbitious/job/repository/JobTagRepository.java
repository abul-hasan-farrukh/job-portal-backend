package com.aimbitious.job.repository;

import com.aimbitious.job.model.JobTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobTagRepository extends JpaRepository<JobTag, Long> {

    boolean existsByName(String name);
    boolean existsBySlug(String slug);
}
