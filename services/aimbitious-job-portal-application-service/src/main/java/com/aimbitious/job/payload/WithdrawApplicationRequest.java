package com.aimbitious.job.payload;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class WithdrawApplicationRequest {
    /**
     * Optional reason for withdrawing.
     * e.g. "Accepted another offer", "Role no longer relevant".
     */
    private String reason;
}
