package com.revrecon.backend.controller;

import com.revrecon.backend.controller.UsageEventController;
import com.revrecon.backend.dto.UsageEventRequest;
import com.revrecon.backend.dto.UsageEventResponse;
import com.revrecon.backend.exception.DuplicateEventException;
import com.revrecon.backend.handler.GlobalExceptionHandler;
import com.revrecon.backend.service.UsageEventService;
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

@WebMvcTest(UsageEventController.class)
@Import(GlobalExceptionHandler.class)
public class UsageEventControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsageEventService usageEventService;

    @Test
    void register_shouldReturn201_whenRequestIsValid() throws Exception {
        // Arrange
        String requestBody = """
            {
                "customerId": 1,
                "idempotencyKey": "evt-123",
                "metric": "api_calls",
                "quantity": 10,
                "timestamp": "2026-04-25T10:00:00Z"
            }
        """;

        // Act
        UsageEventResponse response = new UsageEventResponse();
        response.setId(1L);
        response.setCustomerId(1L);
        response.setIdempotencyKey("evt-123");
        response.setMetric("api_calls");
        response.setQuantity(new BigDecimal("10"));
        response.setTimestamp(Instant.parse("2026-04-25T10:00:00Z"));
        response.setCreatedAt(Instant.parse("2026-04-25T10:01:00Z"));
        response.setUpdatedAt(Instant.parse("2026-04-25T10:01:00Z"));

        when(usageEventService.postEvents(any(UsageEventRequest.class))).thenReturn(response);

        // Assert
        mockMvc.perform(post("/api/usage-events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.idempotencyKey").value("evt-123"))
            .andExpect(jsonPath("$.customerId").value(1))
            .andExpect(jsonPath("$.metric").value("api_calls"))
            .andExpect(jsonPath("$.quantity").value(10))
            .andExpect(jsonPath("$.timestamp").value("2026-04-25T10:00:00Z"));
    }

    @Test
    void register_shouldReturn400_whenRequestIsInvalid() throws Exception {
        String requestBody = """
            {
            "customerId": 1,
            "idempotencyKey": "evt-123",
            "metric": "",
            "quantity": 10,
            "timestamp": "2026-04-25T10:00:00Z"
            }
        """;

        // Act
        mockMvc.perform(post("/api/usage-events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"))
                .andExpect(jsonPath("$.message").value("metric must not be blank"));

        // Assert
        verifyNoInteractions(usageEventService);
    }

    @Test
    void register_shouldReturn409_whenEventIsDuplicate() throws Exception {
        // Arrange
        String requestBody = """
            {
                "customerId": 1,
                "idempotencyKey": "evt-123",
                "metric": "api_calls",
                "quantity": 10,
                "timestamp": "2026-04-25T10:00:00Z"
            }
        """;

        // Act
        when(usageEventService.postEvents(any(UsageEventRequest.class)))
                .thenThrow(new DuplicateEventException("evt-123"));

        // Assert
        mockMvc.perform(post("/api/usage-events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("DUPLICATE_EVENT"))
                .andExpect(jsonPath("$.message").value("Event with idempotency_key already exists: evt-123"));
    }
}
