package com.revrecon.backend.repository;

import com.revrecon.backend.model.Subscription;
import com.revrecon.backend.model.SubscriptionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public class SubscriptionRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public SubscriptionRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Subscription> findById(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        String sql = "SELECT id, customer_id, plan_id, discount, start_date, end_date, status, created_at, updated_at "
                + "FROM subscriptions WHERE id = :id";

        List<Subscription> results = jdbcTemplate.query(sql, params, (rs, rowNum) -> {
            Timestamp endDate = rs.getTimestamp("end_date");
            Instant subscriptionEndDate = endDate != null ? endDate.toInstant() : null;

            return new Subscription(
                    rs.getLong("id"),
                    rs.getLong("customer_id"),
                    rs.getLong("plan_id"),
                    rs.getInt("discount"),
                    rs.getTimestamp("start_date").toInstant(),
                    subscriptionEndDate,
                    SubscriptionStatus.valueOf(rs.getString("status").toUpperCase()),
                    rs.getTimestamp("created_at").toInstant(),
                    rs.getTimestamp("updated_at").toInstant()
            );
        });

        return results.isEmpty() ? Optional.empty() : Optional.of(results.getFirst());
    }

    public List<Subscription> findByCustomerId(Long customerId) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("customerId", customerId);

        String sql = "SELECT id, customer_id, plan_id, discount, start_date, end_date, status, created_at, updated_at "
                + "FROM subscriptions WHERE customer_id = :customerId";

        return jdbcTemplate.query(sql, params, (rs, rowNum) -> {
            Timestamp endDate = rs.getTimestamp("end_date");
            Instant subscriptionEndDate = endDate != null ? endDate.toInstant() : null;

            return new Subscription(
                    rs.getLong("id"),
                    rs.getLong("customer_id"),
                    rs.getLong("plan_id"),
                    rs.getInt("discount"),
                    rs.getTimestamp("start_date").toInstant(),
                    subscriptionEndDate,
                    SubscriptionStatus.valueOf(rs.getString("status").toUpperCase()),
                    rs.getTimestamp("created_at").toInstant(),
                    rs.getTimestamp("updated_at").toInstant()
            );
        });
    }

    public void insert(Subscription subscription) {
        Timestamp subscriptionEndDate = subscription.getEndDate() != null ? Timestamp.from(subscription.getEndDate()) : null;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("customerId", subscription.getCustomerId())
                .addValue("planId", subscription.getPlanId())
                .addValue("discount", subscription.getDiscount())
                .addValue("startDate", Timestamp.from(subscription.getStartDate()))
                .addValue("endDate", subscriptionEndDate)
                .addValue("status", subscription.getStatus().name().toLowerCase());

        String sql = "INSERT INTO subscriptions (customer_id, plan_id, discount, start_date, end_date, status) "
                + "VALUES (:customerId, :planId, :discount, :startDate, :endDate, :status)";

        jdbcTemplate.update(sql, params);
    }

    public void delete(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        String sql = "DELETE FROM subscriptions WHERE id = :id";
        jdbcTemplate.update(sql, params);
    }
}
