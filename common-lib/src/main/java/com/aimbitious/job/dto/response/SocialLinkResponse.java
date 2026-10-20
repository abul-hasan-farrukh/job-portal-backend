package com.aimbitious.job.dto;

import com.aimbitious.job.domain.SocialPlatform;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocialLinkResponse {

    private String url;
    private SocialPlatform socialPlatform;
}
