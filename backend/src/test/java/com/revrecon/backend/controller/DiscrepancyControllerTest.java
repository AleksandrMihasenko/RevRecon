package com.revrecon.backend.controller;

import com.revrecon.backend.dto.DiscrepancyResponse;
import com.revrecon.backend.exception.InvalidBillingPeriodException;
import com.revrecon.backend.handler.GlobalExceptionHandler;
import com.revrecon.backend.model.DiscrepancyType;
import com.revrecon.backend.service.DiscrepancyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DiscrepancyController.class)
@Import(GlobalExceptionHandler.class)
public class DiscrepancyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DiscrepancyService discrepancyService;

    @Test
    void getDiscrepancies_shouldReturn200_whenRequestIsValid() throws Exception {
        // Arrange
        Long customerId = 1L;
        Instant periodStart = Instant.parse("2026-04-01T00:00:00Z");
        Instant periodEnd = Instant.parse("2026-04-02T00:00:00Z");
        List<DiscrepancyResponse> response = List.of(
                new DiscrepancyResponse(customerId, DiscrepancyType.UNBILLED_USAGE, periodStart, periodEnd, "TEST")
        );

        // Act
        when(discrepancyService.findDiscrepancies(customerId, periodStart, periodEnd)).thenReturn(response);

        // Assert
        mockMvc.perform(get("/api/discrepancies")
                .param("customerId", customerId.toString())
                .param("periodStart", periodStart.toString())
                .param("periodEnd", periodEnd.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerId").value(customerId))
                .andExpect(jsonPath("$[0].type").value(DiscrepancyType.UNBILLED_USAGE.name()))
                .andExpect(jsonPath("$[0].periodStart").value(periodStart.toString()))
                .andExpect(jsonPath("$[0].periodEnd").value(periodEnd.toString()))
                .andExpect(jsonPath("$[0].explanation").value("TEST"));
    }

    @Test
    void getDiscrepancies_shouldReturnEmptyList_whenNoDiscrepanciesFound() throws Exception {
        // Arrange
        Long customerId = 1L;
        Instant periodStart = Instant.parse("2026-04-01T00:00:00Z");
        Instant periodEnd = Instant.parse("2026-04-02T00:00:00Z");
        List<DiscrepancyResponse> response = List.of();

        // Act
        when(discrepancyService.findDiscrepancies(customerId, periodStart, periodEnd)).thenReturn(response);

        // Assert
        mockMvc.perform(get("/api/discrepancies")
                        .param("customerId", customerId.toString())
                        .param("periodStart", periodStart.toString())
                        .param("periodEnd", periodEnd.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getDiscrepancies_shouldReturn400_whenPeriodIsInvalid() throws Exception {
        // Arrange
        Long customerId = 1L;
        Instant periodStart = Instant.parse("2026-04-04T00:00:00Z");
        Instant periodEnd = Instant.parse("2026-04-02T00:00:00Z");

        when(discrepancyService.findDiscrepancies(customerId, periodStart, periodEnd))
                .thenThrow(new InvalidBillingPeriodException());

        // Act
        mockMvc.perform(get("/api/discrepancies")
                        .param("customerId", customerId.toString())
                        .param("periodStart", periodStart.toString())
                        .param("periodEnd", periodEnd.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_BILLING_PERIOD"));
    }
}
