package com.revrecon.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsageEventRequest {
    private Long customerId;
    private String idempotencyKey;
    private String metric;
    private BigDecimal quantity;
    private Instant timestamp;
}
