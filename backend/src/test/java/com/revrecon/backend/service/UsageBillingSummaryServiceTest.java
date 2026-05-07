package com.revrecon.backend.service;

import com.revrecon.backend.dto.UsageBillingSummaryResponse;
import com.revrecon.backend.model.UsageMetricTotal;
import com.revrecon.backend.repository.BillingRecordRepository;
import com.revrecon.backend.repository.UsageEventRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UsageBillingSummaryServiceTest {
    @Test
    void getUsageBillingSummary_shouldReturnUsageAndBillingTotals_whenPeriodIsValid() {
        // Arrange
        BillingRecordRepository billingRecordRepository = mock(BillingRecordRepository.class);
        UsageEventRepository usageEventRepository = mock(UsageEventRepository.class);
        UsageBillingSummaryService usageBillingSummaryService = new UsageBillingSummaryService(billingRecordRepository, usageEventRepository);
        Long customerId = 1L;
        Instant periodStart = Instant.parse("2026-04-01T00:00:00Z");
        Instant periodEnd = Instant.parse("2026-05-01T00:00:00Z");

        when(billingRecordRepository.getBilledTotal(customerId, periodStart, periodEnd))
            .thenReturn(new BigDecimal("42.75"));
        when(usageEventRepository.getUsageTotalsByMetric(customerId, periodStart, periodEnd))
            .thenReturn(List.of(
                new UsageMetricTotal("api_calls", new BigDecimal("150")),
                new UsageMetricTotal("storage_gb", new BigDecimal("25.5"))
            ));
        UsageBillingSummaryResponse response = usageBillingSummaryService.getUsageBillingSummary(customerId, periodStart, periodEnd);

        // Assert
        assertNotNull(response);
        assertEquals(customerId, response.getCustomerId());
        assertEquals("api_calls", response.getUsageByMetric().get(0).getMetric());
        assertEquals("storage_gb", response.getUsageByMetric().get(1).getMetric());

        assertEquals(new BigDecimal("150"), response.getUsageByMetric().get(0).getTotalQuantity());
        assertEquals(new BigDecimal("25.5"), response.getUsageByMetric().get(1).getTotalQuantity());

        assertEquals(periodStart, response.getPeriodStart());
        assertEquals(periodEnd, response.getPeriodEnd());
        assertEquals(new BigDecimal("42.75"), response.getBilledTotal());

        assertEquals(2, response.getUsageByMetric().size());

        verify(billingRecordRepository).getBilledTotal(customerId, periodStart, periodEnd);
        verify(usageEventRepository).getUsageTotalsByMetric(customerId, periodStart, periodEnd);
    }
}
