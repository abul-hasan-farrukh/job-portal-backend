package com.aimbitious.job.model;

import com.aimbitious.job.domain.SocialPlatform;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocialLink {

    private String url;
    private SocialPlatform socialPlatform;
}
