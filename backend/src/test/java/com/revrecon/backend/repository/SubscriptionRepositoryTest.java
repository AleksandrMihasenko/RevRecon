package com.revrecon.backend.repository;

import com.revrecon.backend.model.Subscription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@SpringBootTest
@Testcontainers
public class SubscriptionRepositoryTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16.4");

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @BeforeEach
    void clearDatabase() {
        namedParameterJdbcTemplate.getJdbcTemplate().execute("TRUNCATE TABLE customers, plans, subscriptions RESTART IDENTITY CASCADE");
    }

    @Test
    void findApplicableForPeriod_shouldReturnSubscription_whenItCoversEntireBillingPeriod() {
        // Arrange
        Instant periodStart = Instant.parse("2026-04-01T00:00:00Z");
        Instant periodEnd = Instant.parse("2026-05-01T00:00:00Z");
        Long customerId = namedParameterJdbcTemplate.queryForObject(
                "INSERT INTO customers (name) VALUES (:name) RETURNING id",
                Map.of("name", "Test Customer"),
                Long.class
        );

        Long planId = namedParameterJdbcTemplate.queryForObject(
                "INSERT INTO plans (name, prices) VALUES (:name, CAST(:prices AS jsonb)) RETURNING id",
                Map.of("name", "Test Plan", "prices", "{\"api_calls\": 0.01}"),
                Long.class
        );

        namedParameterJdbcTemplate.update(
                "INSERT INTO subscriptions (customer_id, plan_id, start_date, end_date) VALUES (:customerId, :planId, :startDate, null)",
                Map.of("customerId", customerId, "planId", planId, "startDate", Timestamp.from(Instant.parse("2026-03-15T00:00:00Z")))
        );

        // Act
        Optional<Subscription> result = subscriptionRepository.findApplicableForPeriod(customerId, periodStart, periodEnd);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(customerId, result.get().getCustomerId());
        assertEquals(planId, result.get().getPlanId());
    }

    @Test
    void findApplicableForPeriod_shouldReturnEmpty_whenNoSingleSubscriptionCoversEntirePeriod() {
        // Arrange
        Instant periodStart = Instant.parse("2026-04-01T00:00:00Z");
        Instant periodEnd = Instant.parse("2026-05-01T00:00:00Z");
        Long customerId = namedParameterJdbcTemplate.queryForObject(
                "INSERT INTO customers (name) VALUES (:name) RETURNING id",
                Map.of("name", "Test Customer"),
                Long.class
        );

        Long planId = namedParameterJdbcTemplate.queryForObject(
                "INSERT INTO plans (name, prices) VALUES (:name, CAST(:prices AS jsonb)) RETURNING id",
                Map.of("name", "Test Plan", "prices", "{\"api_calls\": 0.01}"),
                Long.class
        );

        namedParameterJdbcTemplate.update(
                "INSERT INTO subscriptions (customer_id, plan_id, start_date, end_date) VALUES (:customerId, :planId, :startDate, null)",
                Map.of("customerId", customerId, "planId", planId, "startDate", Timestamp.from(Instant.parse("2026-04-15T00:00:00Z")))
        );

        // Act
        Optional<Subscription> result = subscriptionRepository.findApplicableForPeriod(customerId, periodStart, periodEnd);

        // Assert
        assertTrue(result.isEmpty());
    }
}
