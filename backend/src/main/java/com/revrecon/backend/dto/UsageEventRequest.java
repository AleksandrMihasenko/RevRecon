package com.revrecon.backend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsageEventRequest {
    @NotNull
    private Long customerId;
    @NotBlank
    private String idempotencyKey;
    @NotBlank
    private String metric;
    @NotNull
    @DecimalMin("0.0")
    private BigDecimal quantity;
    @NotNull
    private Instant timestamp;
}
