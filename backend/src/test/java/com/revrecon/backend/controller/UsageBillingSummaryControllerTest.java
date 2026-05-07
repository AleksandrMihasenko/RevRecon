package com.revrecon.backend.controller;

import com.revrecon.backend.dto.UsageBillingSummaryResponse;
import com.revrecon.backend.dto.UsageByMetricResponse;
import com.revrecon.backend.handler.GlobalExceptionHandler;
import com.revrecon.backend.service.UsageBillingSummaryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsageBillingSummaryController.class)
@Import(GlobalExceptionHandler.class)
public class UsageBillingSummaryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsageBillingSummaryService usageBillingSummaryService;

    @Test
    void getUsageBillingSummary_shouldReturn200_whenRequestIsValid() throws Exception {
        // Arrange
        UsageBillingSummaryResponse response = new UsageBillingSummaryResponse(
                1L,
                Instant.parse("2026-04-01T00:00:00Z"),
                Instant.parse("2026-05-01T00:00:00Z"),
                List.of(
                        new UsageByMetricResponse("api_calls", new BigDecimal("150")),
                        new UsageByMetricResponse("storage_gb", new BigDecimal("25.5"))
                ),
                new BigDecimal("42.75")
        );

        // Act
        when(usageBillingSummaryService.getUsageBillingSummary(
                eq(1L),
                eq(Instant.parse("2026-04-01T00:00:00Z")),
                eq(Instant.parse("2026-05-01T00:00:00Z"))))
                .thenReturn(response);
        // Assert
        mockMvc.perform(get("/api/usage-billing-summary")
                    .param("customerId", "1")
                    .param("periodStart", "2026-04-01T00:00:00Z")
                    .param("periodEnd", "2026-05-01T00:00:00Z"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.periodStart").value("2026-04-01T00:00:00Z"))
                .andExpect(jsonPath("$.periodEnd").value("2026-05-01T00:00:00Z"))
                .andExpect(jsonPath("$.usageByMetric[0].metric").value("api_calls"))
                .andExpect(jsonPath("$.usageByMetric[0].totalQuantity").value(150))
                .andExpect(jsonPath("$.usageByMetric[1].metric").value("storage_gb"))
                .andExpect(jsonPath("$.usageByMetric[1].totalQuantity").value(25.5))
                .andExpect(jsonPath("$.billedTotal").value(42.75));
    }
}
