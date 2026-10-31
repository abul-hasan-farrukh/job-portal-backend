package com.aimbitious.job.domain;

public enum PaymentStatus {
    PENDING,             // created, awaiting gateway confirmation
    PROCESSING,          // gateway is processing
    SUCCESS,             // payment captured successfully
    FAILED,              // payment declined or error
    REFUNDED,            // fully refunded
    PARTIALLY_REFUNDED   // partial refund issued
}
