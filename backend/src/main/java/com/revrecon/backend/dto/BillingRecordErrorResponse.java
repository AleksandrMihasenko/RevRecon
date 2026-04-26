package com.revrecon.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillingRecordErrorResponse {
    private String code;
    private String message;
    private Instant timestamp;
}
