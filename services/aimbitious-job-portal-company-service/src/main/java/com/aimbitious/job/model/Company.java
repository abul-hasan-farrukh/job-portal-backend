package com.aimbitious.job.model;


import com.aimbitious.job.domain.CompanySize;
import com.aimbitious.job.domain.CompanyStatus;
import com.aimbitious.job.domain.CompanyType;
import com.aimbitious.job.domain.IndustryType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "companies")

public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(unique = true)
    private String slug;

    private String tagline;

    private String description;

    private String logoUrl;

    private String coverImageUrl;

    private String website;

    private Integer foundedYear;

    private String email;
    private String phone;

    @Enumerated(EnumType.STRING)
    private CompanySize companySize;

    @Enumerated(EnumType.STRING)
    private CompanyType companyType;

    @Enumerated(EnumType.STRING)
    private IndustryType industryType;

    @Enumerated(EnumType.STRING)
    private CompanyStatus status;

    private boolean isVerified = false;

    @Column(unique = true)
    private String registrationNumber;

    @Column(unique = true, nullable = false)
    private Long ownerId;

    @ElementCollection(fetch = FetchType.EAGER) //this annotation helps to create a separate table for SocialLinks
    @Builder.Default
    private List<SocialLink> socialLinks = new ArrayList<>();

    private Boolean active = true;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime updatedAt;

}

