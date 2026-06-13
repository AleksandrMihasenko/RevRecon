package com.revrecon.backend.dto;

import com.revrecon.backend.model.DiscrepancyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscrepancyResponse {
    private Long customerId;
    private DiscrepancyType type;
    private Instant periodStart;
    private Instant periodEnd;
    private String explanation;
}
