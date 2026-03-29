package com.revrecon.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillingRecord {
    @Id
    private Long id;
    private Long customerId;
    private Instant periodStart;
    private Instant periodEnd;
    private BigDecimal amount;
    private BillingRecordStatus status;
    private Instant createdAt;
    private Instant updatedAt;
}
