package com.aimbitious.job.domain;

public enum OrderStatus {
    CREATED,    // order record exists, payment not yet initiated
    ATTEMPTED,  // payment initiated but not yet confirmed
    PAID,       // payment successful, subscription being activated
    EXPIRED,    // order timed out before payment
    CANCELLED   // cancelled by user or system
}
