package com.aimbitious.job.model;


import com.aimbitious.job.domain.JobType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "work_experiences")

public class WorkExperience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    @Column(nullable = false, length = 150)
    private String companyName;

    private String companyLogoUrl;

    @Column(nullable = false, length = 150)
    private String jobTitle;

    @Enumerated(EnumType.STRING)
    private JobType employmentType;

    /** City/country or "Remote". */
    private String location;

    @Column(nullable = false)
    private LocalDate startDate;

    /** Null when isCurrentJob = true. */
    private LocalDate endDate;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isCurrentJob = false;

    /** Achievements and responsibilities — supports bullet-point markdown. */
    @Column(columnDefinition = "TEXT")
    private String description;

    /** Technologies / tools used in this role, e.g. ["Java", "Spring Boot", "AWS"]. */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "work_experience_technologies",
            joinColumns = @JoinColumn(name = "work_experience_id")
    )
    @Column(name = "technology")
    @Builder.Default
    private List<String> technologies = new ArrayList<>();

    /** Drag-drop position within the resume's experience section (0-based). */
    @Column(nullable = false)
    @Builder.Default
    private Integer displayOrder = 0;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
