package com.revrecon.backend.controller;

import com.revrecon.backend.dto.BillingRecordRequest;
import com.revrecon.backend.dto.BillingRecordResponse;
import com.revrecon.backend.exception.DuplicateEventException;
import com.revrecon.backend.exception.InvalidBillingPeriodException;
import com.revrecon.backend.handler.GlobalExceptionHandler;
import com.revrecon.backend.model.BillingRecordStatus;
import com.revrecon.backend.service.BillingRecordService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BillingController.class)
@Import(GlobalExceptionHandler.class)
public class BillingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BillingRecordService billingRecordService;

    @Test
    void register_shouldReturn201_whenRequestIsValid() throws Exception {
        // Arrange
        String requestBody = """
            {
                "customerId": 1,
                "idempotencyKey": "evt-123",
                "periodStart": "2026-04-20T10:00:00Z",
                "periodEnd": "2026-04-25T11:00:00Z",
                "amount": 10.00,
                "status": "DRAFT"
            }
        """;

        // Act
        BillingRecordResponse response = new BillingRecordResponse();
        response.setId(1L);
        response.setCustomerId(1L);
        response.setIdempotencyKey("evt-123");
        response.setAmount(BigDecimal.valueOf(10.00));
        response.setCreatedAt(Instant.parse("2026-04-25T10:01:00Z"));
        response.setUpdatedAt(Instant.parse("2026-04-25T10:01:00Z"));
        response.setPeriodStart(Instant.parse("2026-04-20T10:00:00Z"));
        response.setPeriodEnd(Instant.parse("2026-04-25T11:00:00Z"));
        response.setStatus(BillingRecordStatus.DRAFT);

        when(billingRecordService.postBillingRecords(any(BillingRecordRequest.class))).thenReturn(response);

        // Assert
        mockMvc.perform(post("/api/billing")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.idempotencyKey").value("evt-123"))
            .andExpect(jsonPath("$.customerId").value(1))
            .andExpect(jsonPath("$.amount").value(10.00))
            .andExpect(jsonPath("$.createdAt").value("2026-04-25T10:01:00Z"))
            .andExpect(jsonPath("$.updatedAt").value("2026-04-25T10:01:00Z"))
            .andExpect(jsonPath("$.periodStart").value("2026-04-20T10:00:00Z"))
            .andExpect(jsonPath("$.periodEnd").value("2026-04-25T11:00:00Z"))
            .andExpect(jsonPath("$.status").value("DRAFT"));
    }

    @Test
    void register_shouldReturn400_whenRequestIsInvalid() throws Exception {
        // Arrange
        String requestBody = """
            {
                "customerId": 1,
                "idempotencyKey": "",
                "periodStart": "2026-04-20T10:00:00Z",
                "periodEnd": "2026-04-25T11:00:00Z",
                "amount": 10.00,
                "status": "DRAFT"
            }
        """;

        // Act
        mockMvc.perform(post("/api/billing")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"))
                .andExpect(jsonPath("$.message").value("idempotencyKey must not be blank"));

        // Assert
        verifyNoInteractions(billingRecordService);
    }

    @Test
    void register_shouldReturn409_whenBillingRecordIsDuplicate() throws Exception {
        // Arrange
        String requestBody = """
            {
                "customerId": 1,
                "idempotencyKey": "evt-123",
                "periodStart": "2026-04-20T10:00:00Z",
                "periodEnd": "2026-04-25T11:00:00Z",
                "amount": 10.00,
                "status": "DRAFT"
            }
        """;

        // Act
        when(billingRecordService.postBillingRecords(any(BillingRecordRequest.class)))
                .thenThrow(new DuplicateEventException("evt-123"));

        // Assert
        mockMvc.perform(post("/api/billing")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("DUPLICATE_EVENT"))
                .andExpect(jsonPath("$.message").value("Event with idempotency_key already exists: evt-123"));
    }

    @Test
    void register_shouldReturn400_whenBillingPeriodIsInvalid() throws Exception {
        // Arrange
        String requestBody = """
            {
                "customerId": 1,
                "idempotencyKey": "evt-123",
                "periodStart": "2026-04-25T10:00:00Z",
                "periodEnd": "2026-04-20T11:00:00Z",
                "amount": 10.00,
                "status": "DRAFT"
            }
        """;

        // Act
        when(billingRecordService.postBillingRecords(any(BillingRecordRequest.class)))
                .thenThrow(new InvalidBillingPeriodException());

        // Assert
        mockMvc.perform(post("/api/billing")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_BILLING_PERIOD"))
                .andExpect(jsonPath("$.message").value("periodStart must be before periodEnd"));
    }
}

