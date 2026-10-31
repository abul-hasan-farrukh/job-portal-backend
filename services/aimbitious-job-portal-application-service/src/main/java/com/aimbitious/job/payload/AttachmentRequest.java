package com.aimbitious.job.payload;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttachmentRequest {

    private String fileUrl;

    private String fileName;

    private String fileType;

    private Long fileSizeBytes;
}
