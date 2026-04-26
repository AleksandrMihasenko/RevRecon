package com.revrecon.backend.dto;

import com.revrecon.backend.model.BillingRecordStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillingRecordResponse {
    private Long id;
    private String idempotencyKey;
    private Long customerId;
    private Instant periodStart;
    private Instant periodEnd;
    private BigDecimal amount;
    private BillingRecordStatus status;
    private Instant createdAt;
    private Instant updatedAt;
}
