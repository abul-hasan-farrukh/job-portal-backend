package com.aimbitious.job.dto.response;

import com.aimbitious.job.domain.PaymentGateway;
import com.aimbitious.job.domain.PaymentMethod;
import com.aimbitious.job.domain.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)

public class TransactionResponse {

    private Long id;
    private String transactionNumber;
    private String orderNumber;

    private Long companyId;

    private BigDecimal amount;
    private String currency;

    private PaymentGateway gateway;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;

    /** Gateway payment ID — useful for support lookups. */
    private String gatewayPaymentId;

    private String failureReason;
    private LocalDateTime paidAt;
    private LocalDateTime createdAt;
}
