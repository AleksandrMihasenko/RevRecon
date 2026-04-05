package com.revrecon.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsageEventResponse {
    private Long id;
    private String idempotencyKey;
    private Long customerId;
    private String metric;
    private BigDecimal quantity;
    private Instant timestamp;
    private Instant createdAt;
    private Instant updatedAt;
}
