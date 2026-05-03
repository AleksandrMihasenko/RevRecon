package com.revrecon.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsageBillingSummaryResponse {
    private Long customerId;
    private Instant periodStart;
    private Instant periodEnd;
    private List<UsageByMetricResponse> usageByMetric;
    private BigDecimal billedTotal;
}
