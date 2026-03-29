package com.revrecon.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {
    @Id
    private Long id;
    private Long customerId;
    private Long planId;
    private Integer discount;
    private Instant startDate;
    private Instant endDate;
    private SubscriptionStatus status;
    private Instant createdAt;
    private Instant updatedAt;
}
