package com.revrecon.backend.service;

import com.revrecon.backend.dto.DiscrepancyResponse;
import com.revrecon.backend.exception.InvalidBillingPeriodException;
import com.revrecon.backend.model.BillingRecord;
import com.revrecon.backend.model.DiscrepancyType;
import com.revrecon.backend.model.UsageMetricTotal;
import com.revrecon.backend.repository.BillingRecordRepository;
import com.revrecon.backend.repository.UsageEventRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class DiscrepancyService {

    private final BillingRecordRepository billingRecordRepository;
    private final UsageEventRepository usageEventRepository;

    public DiscrepancyService(BillingRecordRepository billingRecordRepository, UsageEventRepository usageEventRepository) {
        this.billingRecordRepository = billingRecordRepository;
        this.usageEventRepository = usageEventRepository;
    }

    public List<DiscrepancyResponse> findDiscrepancies(Long customerId, Instant periodStart, Instant periodEnd) {
        if (periodStart.isAfter(periodEnd)) {
            throw new InvalidBillingPeriodException();
        }

        List<UsageMetricTotal> usageTotals = usageEventRepository.getUsageTotalsByMetric(customerId, periodStart, periodEnd);
        Optional<BillingRecord> billingRecord = billingRecordRepository.findByCustomerIdAndPeriod(customerId, periodStart, periodEnd);

        if (billingRecord.isEmpty() && !usageTotals.isEmpty()) {
            return List.of(new DiscrepancyResponse(customerId, DiscrepancyType.UNBILLED_USAGE, periodStart, periodEnd, "Billing record missing for period"));
        }

        return List.of();
    }
}
