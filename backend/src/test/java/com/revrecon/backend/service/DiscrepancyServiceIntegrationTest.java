package com.revrecon.backend.service;

import com.revrecon.backend.dto.DiscrepancyResponse;
import com.revrecon.backend.model.DiscrepancyType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
public class DiscrepancyServiceIntegrationTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16.4");

    @Autowired
    DiscrepancyService discrepancyService;

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @BeforeEach
    void clearDatabase() {
        jdbcTemplate.getJdbcTemplate().execute("TRUNCATE TABLE customers, usage_events, billing_records RESTART IDENTITY CASCADE");
    }

    @Test
    void findDiscrepancies_shouldReturnUnbilledUsage_whenUsageExistsButBillingRecordIsMissing() {
        // Arrange
        Long customerId = jdbcTemplate.queryForObject(
                "INSERT INTO customers (name) VALUES (:name) RETURNING id",
                Map.of("name", "Test Customer"),
                Long.class
        );
        jdbcTemplate.update(
                "INSERT INTO usage_events (idempotency_key, customer_id, metric, quantity, timestamp) VALUES (:idempotencyKey, :customerId, :metric, :quantity, :timestamp)",
                Map.of(
                        "idempotencyKey", "usage-2026-04-001",
                        "customerId", customerId,
                        "metric", "api_calls",
                        "quantity", new BigDecimal("1200"),
                        "timestamp", Timestamp.from(Instant.parse("2026-04-15T00:00:00Z"))
                )
        );
        Instant periodStart = Instant.parse("2026-04-01T00:00:00Z");
        Instant periodEnd = Instant.parse("2026-05-01T00:00:00Z");

        // Act
        List<DiscrepancyResponse> discrepancies = discrepancyService.findDiscrepancies(customerId, periodStart, periodEnd);

        // Assert
        assertEquals(1, discrepancies.size());
        assertEquals(customerId, discrepancies.get(0).getCustomerId());
        assertEquals(DiscrepancyType.UNBILLED_USAGE, discrepancies.get(0).getType());
        assertEquals(periodStart, discrepancies.get(0).getPeriodStart());
        assertEquals(periodEnd, discrepancies.get(0).getPeriodEnd());
    }
}
