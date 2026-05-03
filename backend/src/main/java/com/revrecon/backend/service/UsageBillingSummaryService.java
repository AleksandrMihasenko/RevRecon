package com.revrecon.backend.service;

import com.revrecon.backend.dto.UsageBillingSummaryResponse;
import com.revrecon.backend.dto.UsageByMetricResponse;
import com.revrecon.backend.exception.InvalidBillingPeriodException;
import com.revrecon.backend.model.UsageMetricTotal;
import com.revrecon.backend.repository.BillingRecordRepository;
import com.revrecon.backend.repository.UsageEventRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
public class UsageBillingSummaryService {
    private final BillingRecordRepository billingRecordRepository;
    private final UsageEventRepository usageEventRepository;

    public UsageBillingSummaryService(BillingRecordRepository billingRecordRepository, UsageEventRepository usageEventRepository) {
        this.billingRecordRepository = billingRecordRepository;
        this.usageEventRepository = usageEventRepository;
    }

    public UsageBillingSummaryResponse getUsageBillingSummary(Long customerId, Instant periodStart, Instant periodEnd) {
        validatePeriods(periodStart, periodEnd);

        BigDecimal billedTotal = billingRecordRepository.getBilledTotal(customerId, periodStart, periodEnd);
        List<UsageMetricTotal> usageTotals = usageEventRepository.getUsageTotalsByMetric(customerId, periodStart, periodEnd);

        List<UsageByMetricResponse> usageByMetric = usageTotals
                .stream()
                .map(usage -> new UsageByMetricResponse(usage.getMetric(), usage.getTotalQuantity()))
                .toList();

        return new UsageBillingSummaryResponse(customerId, periodStart, periodEnd, usageByMetric, billedTotal);
    }

    private void validatePeriods(Instant periodStart, Instant periodEnd) {
        if (periodStart.isAfter(periodEnd)) {
            throw new InvalidBillingPeriodException();
        }
    }
}
