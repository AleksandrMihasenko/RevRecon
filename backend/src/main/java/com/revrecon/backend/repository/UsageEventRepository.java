package com.revrecon.backend.repository;

import com.revrecon.backend.exception.DuplicateEventException;
import com.revrecon.backend.model.UsageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class UsageEventRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public UsageEventRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<UsageEvent> findById(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        String sql = "SELECT id, idempotency_key, customer_id, metric, quantity, timestamp, created_at, updated_at "
                + "FROM usage_events WHERE id = :id";

        List<UsageEvent> results = jdbcTemplate.query(sql, params, (rs, rowNum) -> new UsageEvent(
                rs.getLong("id"),
                rs.getString("idempotency_key"),
                rs.getLong("customer_id"),
                rs.getString("metric"),
                rs.getBigDecimal("quantity"),
                rs.getTimestamp("timestamp").toInstant(),
                rs.getTimestamp("created_at").toInstant(),
                rs.getTimestamp("updated_at").toInstant()
        ));

        return results.isEmpty() ? Optional.empty() : Optional.of(results.getFirst());
    }

    public Optional<UsageEvent> findByIdempotencyKey(String idempotencyKey) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("idempotencyKey", idempotencyKey);

        String sql = "SELECT id, idempotency_key, customer_id, metric, quantity, timestamp, created_at, updated_at "
                + "FROM usage_events WHERE idempotency_key = :idempotencyKey";

        List<UsageEvent> results = jdbcTemplate.query(sql, params, (rs, rowNum) -> new UsageEvent(
                rs.getLong("id"),
                rs.getString("idempotency_key"),
                rs.getLong("customer_id"),
                rs.getString("metric"),
                rs.getBigDecimal("quantity"),
                rs.getTimestamp("timestamp").toInstant(),
                rs.getTimestamp("created_at").toInstant(),
                rs.getTimestamp("updated_at").toInstant()
        ));

        return results.isEmpty() ? Optional.empty() : Optional.of(results.getFirst());
    }

    public List<UsageEvent> findByCustomerIdAndPeriod(Long customerId, java.time.Instant periodStart, java.time.Instant periodEnd) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("customerId", customerId)
                .addValue("periodStart", Timestamp.from(periodStart))
                .addValue("periodEnd", Timestamp.from(periodEnd));

        String sql = "SELECT id, idempotency_key, customer_id, metric, quantity, timestamp, created_at, updated_at "
                + "FROM usage_events "
                + "WHERE customer_id = :customerId "
                + "AND timestamp >= :periodStart "
                + "AND timestamp < :periodEnd";

        return jdbcTemplate.query(sql, params, (rs, rowNum) -> new UsageEvent(
                rs.getLong("id"),
                rs.getString("idempotency_key"),
                rs.getLong("customer_id"),
                rs.getString("metric"),
                rs.getBigDecimal("quantity"),
                rs.getTimestamp("timestamp").toInstant(),
                rs.getTimestamp("created_at").toInstant(),
                rs.getTimestamp("updated_at").toInstant()
        ));
    }

    public UsageEvent insert(UsageEvent event) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("idempotencyKey", event.getIdempotencyKey())
                .addValue("customerId", event.getCustomerId())
                .addValue("metric", event.getMetric())
                .addValue("quantity", event.getQuantity())
                .addValue("timestamp", Timestamp.from(event.getTimestamp()));

        String sql = "INSERT INTO usage_events (idempotency_key, customer_id, metric, quantity, timestamp) "
                + "VALUES (:idempotencyKey, :customerId, :metric, :quantity, :timestamp) "
                + "RETURNING *";

        try {
            return jdbcTemplate.queryForObject(sql, params, (rs, rowNum) -> new UsageEvent(
                    rs.getLong("id"),
                    rs.getString("idempotency_key"),
                    rs.getLong("customer_id"),
                    rs.getString("metric"),
                    rs.getBigDecimal("quantity"),
                    rs.getTimestamp("timestamp").toInstant(),
                    rs.getTimestamp("created_at").toInstant(),
                    rs.getTimestamp("updated_at").toInstant()
            ));
        } catch (DuplicateKeyException e) {
            throw new DuplicateEventException(event.getIdempotencyKey());
        }
    }
}
