package com.aimbitious.job.model;

import com.aimbitious.job.domain.SkillCategory;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Shared skill reference entity (e.g. "Java", "React", "AWS").
 * Skills are reused across many jobs; employers pick from this pool.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "job_skills")

public class JobSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    /** URL-friendly identifier. */
    @Column(unique = true, length = 120)
    private String slug;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SkillCategory category;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
