package com.revrecon.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsageEvent {
    @Id
    private Long id;
    private String idempotencyKey;
    private Long customerId;
    private String metric;
    private BigDecimal quantity;
    private Instant timestamp;
    private Instant createdAt;
    private Instant updatedAt;
}
