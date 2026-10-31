package com.aimbitious.job.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.aimbitious.job.domain.RefundStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RefundResponse {

    private Long id;
    private String refundNumber;
    private String transactionNumber;

    private Long companyId;

    private BigDecimal amount;
    private String currency;

    private String reason;
    private RefundStatus status;
    private String gatewayRefundId;

    private LocalDateTime processedAt;
    private LocalDateTime createdAt;

}
