package com.revrecon.backend.service;

import com.revrecon.backend.dto.BillingRecordRequest;
import com.revrecon.backend.dto.BillingRecordResponse;
import com.revrecon.backend.exception.DuplicateEventException;
import com.revrecon.backend.exception.InvalidBillingPeriodException;
import com.revrecon.backend.model.BillingRecord;
import com.revrecon.backend.model.BillingRecordStatus;
import com.revrecon.backend.repository.BillingRecordRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BillingRecordServiceTest {
    @Test
    void postBillingRecords_shouldSaveRecord_whenRequestIsValid() {
        // Arrange
        BillingRecordRepository billingRecordRepository = mock(BillingRecordRepository.class);
        BillingRecordService billingRecordService = new BillingRecordService(billingRecordRepository);

        BillingRecordRequest billingRecordRequest = createRequest(Instant.parse("2026-04-20T10:00:00Z"), Instant.parse("2026-04-25T11:00:00Z"));
        BillingRecord savedBillingRecord = createSavedRecord(Instant.parse("2026-04-20T10:00:00Z"), Instant.parse("2026-04-25T11:00:00Z"));

        // Act
        when(billingRecordRepository.insert(any(BillingRecord.class))).thenReturn(savedBillingRecord);
        BillingRecordResponse response = billingRecordService.postBillingRecords(billingRecordRequest);

        // Assert
        assertEquals(savedBillingRecord.getId(), response.getId());
        assertEquals(savedBillingRecord.getCustomerId(), response.getCustomerId());
        assertEquals(savedBillingRecord.getIdempotencyKey(), response.getIdempotencyKey());
        assertEquals(savedBillingRecord.getPeriodStart(), response.getPeriodStart());
        assertEquals(savedBillingRecord.getPeriodEnd(), response.getPeriodEnd());
        assertEquals(savedBillingRecord.getAmount(), response.getAmount());
        assertEquals(savedBillingRecord.getStatus(), response.getStatus());
        verify(billingRecordRepository).insert(any(BillingRecord.class));
    }

    @Test
    void postBillingRecords_shouldAllowEqualPeriodBoundaries_whenStartEqualsEnd() {
        // Arrange
        BillingRecordRepository billingRecordRepository = mock(BillingRecordRepository.class);
        BillingRecordService billingRecordService = new BillingRecordService(billingRecordRepository);

        BillingRecordRequest billingRecordRequest = createRequest(Instant.parse("2026-04-20T10:00:00Z"), Instant.parse("2026-04-20T10:00:00Z"));
        BillingRecord savedBillingRecord = createSavedRecord(Instant.parse("2026-04-20T10:00:00Z"), Instant.parse("2026-04-20T10:00:00Z"));

        // Act
        when(billingRecordRepository.insert(any(BillingRecord.class))).thenReturn(savedBillingRecord);
        BillingRecordResponse response = billingRecordService.postBillingRecords(billingRecordRequest);

        // Assert
        assertEquals(savedBillingRecord.getId(), response.getId());
        assertEquals(savedBillingRecord.getCustomerId(), response.getCustomerId());
        assertEquals(savedBillingRecord.getIdempotencyKey(), response.getIdempotencyKey());
        assertEquals(savedBillingRecord.getPeriodStart(), response.getPeriodStart());
        assertEquals(savedBillingRecord.getPeriodEnd(), response.getPeriodEnd());
        assertEquals(savedBillingRecord.getAmount(), response.getAmount());
        assertEquals(savedBillingRecord.getStatus(), response.getStatus());
        verify(billingRecordRepository).insert(any(BillingRecord.class));
    }

    @Test
    void postBillingRecords_shouldThrowException_whenPeriodIsInvalid() {
        // Arrange
        BillingRecordRepository billingRecordRepository = mock(BillingRecordRepository.class);
        BillingRecordService billingRecordService = new BillingRecordService(billingRecordRepository);
        BillingRecordRequest billingRecordRequest = createRequest(Instant.parse("2026-04-25T10:00:00Z"), Instant.parse("2026-04-20T11:00:00Z"));

        // Act
        assertThrows(InvalidBillingPeriodException.class, () -> billingRecordService.postBillingRecords(billingRecordRequest));

        // Assert
        verify(billingRecordRepository, never()).insert(any(BillingRecord.class));
    }

    @Test
    void postBillingRecords_shouldThrowDuplicateEventException_whenRepositoryDetectsDuplicate() {
        // Arrange
        BillingRecordRepository billingRecordRepository = mock(BillingRecordRepository.class);
        BillingRecordService billingRecordService = new BillingRecordService(billingRecordRepository);
        BillingRecordRequest billingRecordRequest = createRequest(Instant.parse("2026-04-20T10:00:00Z"), Instant.parse("2026-04-25T11:00:00Z"));

        when(billingRecordRepository.insert(any(BillingRecord.class))).thenThrow(new DuplicateEventException("evt-123"));

        // Act
        assertThrows(DuplicateEventException.class, () -> billingRecordService.postBillingRecords(billingRecordRequest));

        // Assert
        verify(billingRecordRepository).insert(any(BillingRecord.class));
    }

    private BillingRecordRequest createRequest(Instant periodStart, Instant periodEnd) {
        BillingRecordRequest request = new BillingRecordRequest();
        request.setCustomerId(1L);
        request.setIdempotencyKey("evt-123");
        request.setPeriodStart(periodStart);
        request.setPeriodEnd(periodEnd);
        request.setAmount(new BigDecimal("10.00"));
        request.setStatus(BillingRecordStatus.DRAFT);
        return request;
    }

    private BillingRecord createSavedRecord(Instant periodStart, Instant periodEnd) {
        BillingRecord record = new BillingRecord();
        record.setId(1L);
        record.setCustomerId(1L);
        record.setIdempotencyKey("evt-123");
        record.setPeriodStart(periodStart);
        record.setPeriodEnd(periodEnd);
        record.setAmount(new BigDecimal("10.00"));
        record.setStatus(BillingRecordStatus.DRAFT);
        return record;
    }
}
