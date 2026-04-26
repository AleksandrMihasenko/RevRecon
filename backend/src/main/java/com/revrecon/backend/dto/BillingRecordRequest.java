package com.revrecon.backend.dto;

import com.revrecon.backend.model.BillingRecordStatus;
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
public class BillingRecordRequest {
    @NotNull
    private Long customerId;
    @NotBlank
    private String idempotencyKey;
    @NotNull
    private Instant periodStart;
    @NotNull
    private Instant periodEnd;
    @NotNull
    @DecimalMin("0.0")
    private BigDecimal amount;
    @NotNull
    private BillingRecordStatus status;
}
