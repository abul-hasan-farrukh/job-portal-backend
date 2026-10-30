package com.aimbitious.job.model.embeddable;

import com.aimbitious.job.domain.SocialPlatform;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

/**
 * One social/web link belonging to a company or candidate.
 * Stored via @ElementCollection — no own table PK, owned by the parent.
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocialLink {

    @Enumerated(EnumType.STRING)
    private SocialPlatform platform;

    private String url;
}
