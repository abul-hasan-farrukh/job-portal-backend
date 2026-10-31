package com.aimbitious.job.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.aimbitious.job.domain.OrderStatus;
import com.aimbitious.job.domain.PaymentGateway;
import com.aimbitious.job.domain.PaymentPurpose;
import com.aimbitious.job.domain.SubscriptionPlan;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Returned to the frontend after order creation.
 * Frontend uses gatewayOrderId to open the gateway checkout widget.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentOrderResponse {

    private Long id;
    private String orderNumber;
    private Long companyId;

    private BigDecimal amount;
    private String currency;

    private PaymentPurpose purpose;
    private SubscriptionPlan subscriptionPlan;

    private PaymentGateway gateway;

    /** Gateway order ID — passed to the checkout widget. */
    private String gatewayOrderId;

    private OrderStatus status;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;
}
