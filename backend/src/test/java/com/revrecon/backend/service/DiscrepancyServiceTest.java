package com.revrecon.backend.service;

import com.revrecon.backend.dto.DiscrepancyResponse;
import com.revrecon.backend.exception.InvalidBillingPeriodException;
import com.revrecon.backend.model.*;
import com.revrecon.backend.repository.BillingRecordRepository;
import com.revrecon.backend.repository.UsageEventRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

public class DiscrepancyServiceTest {
    @Test
    void findDiscrepancies_shouldReturnUnbilledUsage_whenUsageExistsButBillingRecordIsMissing() {
        Long customerId = 1L;
        Instant periodStart = Instant.parse("2026-05-01T00:00:00Z");
        Instant periodEnd = Instant.parse("2026-06-01T00:00:00Z");
        // Arrange
        BillingRecordRepository billingRecordRepository = mock(BillingRecordRepository.class);
        UsageEventRepository usageEventRepository = mock(UsageEventRepository.class);

        DiscrepancyService discrepancyService = new DiscrepancyService(billingRecordRepository, usageEventRepository);
        when(usageEventRepository.getUsageTotalsByMetric(customerId, periodStart, periodEnd)).thenReturn(List.of(new UsageMetricTotal("api_calls", new BigDecimal("1200"))));
        when(billingRecordRepository.findByCustomerIdAndPeriod(customerId, periodStart, periodEnd)).thenReturn(Optional.empty());

        // Act
        List<DiscrepancyResponse> discrepancies = discrepancyService.findDiscrepancies(customerId, periodStart, periodEnd);

        // Assert
        assertEquals(1, discrepancies.size(), "Discrepancies list size should be 1");
        assertEquals(customerId, discrepancies.get(0).getCustomerId(), "Discrepancy customer ID should match");
        assertEquals(DiscrepancyType.UNBILLED_USAGE, discrepancies.get(0).getType(), "Discrepancy type should be UNBILLED_USAGE");
        assertEquals(periodStart, discrepancies.get(0).getPeriodStart(), "Discrepancy period start should match");
        assertEquals(periodEnd, discrepancies.get(0).getPeriodEnd(), "Discrepancy period end should match");
        assertTrue(
                discrepancies.get(0).getExplanation().contains("no matching billing record"),
                "Discrepancy explanation should describe the missing matching billing record"
        );
    }

    @Test
    void findDiscrepancies_shouldReturnEmptyList_whenUsageAndBillingRecordExist() {
        Long id = 1L;
        String idempotencyKey = "billing-2026-05";
        Long customerId = 1L;
        BillingRecordStatus status = BillingRecordStatus.OPEN;
        Instant periodStart = Instant.parse("2026-05-01T00:00:00Z");
        Instant periodEnd = Instant.parse("2026-06-01T00:00:00Z");
        Instant recordCreatedAt = Instant.parse("2026-06-01T00:00:00Z");

        // Arrange
        BillingRecordRepository billingRecordRepository = mock(BillingRecordRepository.class);
        UsageEventRepository usageEventRepository = mock(UsageEventRepository.class);
        DiscrepancyService discrepancyService = new DiscrepancyService(billingRecordRepository, usageEventRepository);
        when(usageEventRepository.getUsageTotalsByMetric(customerId, periodStart, periodEnd)).thenReturn(List.of(new UsageMetricTotal("api_calls", new BigDecimal("1200"))));
        BillingRecord billingRecord = new BillingRecord(
                id,
                idempotencyKey,
                customerId,
                periodStart,
                periodEnd,
                new BigDecimal("1000"),
                status,
                recordCreatedAt,
                recordCreatedAt
        );
        when(billingRecordRepository.findByCustomerIdAndPeriod(customerId, periodStart, periodEnd)).thenReturn(Optional.of(billingRecord));

        // Act
        List<DiscrepancyResponse> discrepancies = discrepancyService.findDiscrepancies(customerId, periodStart, periodEnd);

        // Assert
        assertTrue(discrepancies.isEmpty(), "Discrepancies list should be empty");
    }

    @Test
    void findDiscrepancies_shouldReturnEmptyList_whenUsageIsEmptyAndBillingRecordExists() {
        Long id = 1L;
        String idempotencyKey = "billing-2026-05";
        Long customerId = 1L;
        BillingRecordStatus status = BillingRecordStatus.OPEN;
        Instant periodStart = Instant.parse("2026-05-01T00:00:00Z");
        Instant periodEnd = Instant.parse("2026-06-01T00:00:00Z");
        Instant recordCreatedAt = Instant.parse("2026-06-01T00:00:00Z");

        // Arrange
        BillingRecordRepository billingRecordRepository = mock(BillingRecordRepository.class);
        UsageEventRepository usageEventRepository = mock(UsageEventRepository.class);
        DiscrepancyService discrepancyService = new DiscrepancyService(billingRecordRepository, usageEventRepository);

        when(usageEventRepository.getUsageTotalsByMetric(customerId, periodStart, periodEnd)).thenReturn(List.of());
        BillingRecord billingRecord = new BillingRecord(
                id,
                idempotencyKey,
                customerId,
                periodStart,
                periodEnd,
                new BigDecimal("1000"),
                status,
                recordCreatedAt,
                recordCreatedAt
        );
        when(billingRecordRepository.findByCustomerIdAndPeriod(customerId, periodStart, periodEnd)).thenReturn(Optional.of(billingRecord));

        // Act
        List<DiscrepancyResponse> discrepancies = discrepancyService.findDiscrepancies(customerId, periodStart, periodEnd);

        // Assert
        assertTrue(discrepancies.isEmpty(), "Discrepancies list should be empty");
    }

    @Test
    void findDiscrepancies_shouldReturnEmptyList_whenUsageIsEmptyAndBillingRecordIsMissing() {
        Long customerId = 1L;
        Instant periodStart = Instant.parse("2026-05-01T00:00:00Z");
        Instant periodEnd = Instant.parse("2026-06-01T00:00:00Z");

        // Arrange
        BillingRecordRepository billingRecordRepository = mock(BillingRecordRepository.class);
        UsageEventRepository usageEventRepository = mock(UsageEventRepository.class);
        DiscrepancyService discrepancyService = new DiscrepancyService(billingRecordRepository, usageEventRepository);

        when(usageEventRepository.getUsageTotalsByMetric(customerId, periodStart, periodEnd)).thenReturn(List.of());
        when(billingRecordRepository.findByCustomerIdAndPeriod(customerId, periodStart, periodEnd)).thenReturn(Optional.empty());

        // Act
        List<DiscrepancyResponse> discrepancies = discrepancyService.findDiscrepancies(customerId, periodStart, periodEnd);

        // Assert
        assertTrue(discrepancies.isEmpty(), "Discrepancies list should be empty");
    }

    @Test
    void findDiscrepancies_shouldThrowInvalidBillingPeriodException_whenPeriodIsInvalid() {
        Long customerId = 1L;
        Instant periodStart = Instant.parse("2026-06-01T00:00:00Z");
        Instant periodEnd = Instant.parse("2026-05-01T00:00:00Z");

        // Arrange
        BillingRecordRepository billingRecordRepository = mock(BillingRecordRepository.class);
        UsageEventRepository usageEventRepository = mock(UsageEventRepository.class);
        DiscrepancyService discrepancyService = new DiscrepancyService(billingRecordRepository, usageEventRepository);

        // Act
        assertThrows(InvalidBillingPeriodException.class, () -> discrepancyService.findDiscrepancies(customerId, periodStart, periodEnd));

        // Assert
        verifyNoInteractions(usageEventRepository, billingRecordRepository);
    }
}
