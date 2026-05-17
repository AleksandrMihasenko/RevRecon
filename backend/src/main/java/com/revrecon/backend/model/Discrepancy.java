package com.revrecon.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Discrepancy {
    private Long customerId;
    private DiscrepancyType type;
    private Instant periodStart;
    private Instant periodEnd;
    private String explanation;
}
