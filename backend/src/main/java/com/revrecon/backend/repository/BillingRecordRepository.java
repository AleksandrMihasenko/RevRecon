package com.revrecon.backend.repository;

import com.revrecon.backend.exception.DuplicateEventException;
import com.revrecon.backend.model.BillingRecord;
import com.revrecon.backend.model.BillingRecordStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class BillingRecordRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public BillingRecordRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<BillingRecord> findById(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        String sql = "SELECT id, idempotency_key, customer_id, period_start, period_end, amount, status, created_at, updated_at "
                + "FROM billing_records WHERE id = :id";

        List<BillingRecord> results = jdbcTemplate.query(sql, params, (rs, rowNum) -> new BillingRecord(
                rs.getLong("id"),
                rs.getString("idempotency_key"),
                rs.getLong("customer_id"),
                rs.getTimestamp("period_start").toInstant(),
                rs.getTimestamp("period_end").toInstant(),
                rs.getBigDecimal("amount"),
                BillingRecordStatus.valueOf(rs.getString("status").toUpperCase()),
                rs.getTimestamp("created_at").toInstant(),
                rs.getTimestamp("updated_at").toInstant()
        ));

        return results.isEmpty() ? Optional.empty() : Optional.of(results.getFirst());
    }

    public List<BillingRecord> findByCustomerId(Long customerId) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("customerId", customerId);

        String sql = "SELECT id, idempotency_key, customer_id, period_start, period_end, amount, status, created_at, updated_at "
                + "FROM billing_records WHERE customer_id = :customerId ORDER BY period_start DESC";

        return jdbcTemplate.query(sql, params, (rs, rowNum) -> new BillingRecord(
                rs.getLong("id"),
                rs.getString("idempotency_key"),
                rs.getLong("customer_id"),
                rs.getTimestamp("period_start").toInstant(),
                rs.getTimestamp("period_end").toInstant(),
                rs.getBigDecimal("amount"),
                BillingRecordStatus.valueOf(rs.getString("status").toUpperCase()),
                rs.getTimestamp("created_at").toInstant(),
                rs.getTimestamp("updated_at").toInstant()
        ));
    }

    public Optional<BillingRecord> findByCustomerIdAndPeriod(Long customerId, java.time.Instant periodStart, java.time.Instant periodEnd) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("customerId", customerId)
                .addValue("periodStart", Timestamp.from(periodStart))
                .addValue("periodEnd", Timestamp.from(periodEnd));

        String sql = "SELECT id, idempotency_key, customer_id, period_start, period_end, amount, status, created_at, updated_at "
                + "FROM billing_records "
                + "WHERE customer_id = :customerId "
                + "AND period_start = :periodStart "
                + "AND period_end = :periodEnd";

        List<BillingRecord> results = jdbcTemplate.query(sql, params, (rs, rowNum) -> new BillingRecord(
                rs.getLong("id"),
                rs.getString("idempotency_key"),
                rs.getLong("customer_id"),
                rs.getTimestamp("period_start").toInstant(),
                rs.getTimestamp("period_end").toInstant(),
                rs.getBigDecimal("amount"),
                BillingRecordStatus.valueOf(rs.getString("status").toUpperCase()),
                rs.getTimestamp("created_at").toInstant(),
                rs.getTimestamp("updated_at").toInstant()
        ));

        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public BillingRecord insert(BillingRecord record) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("idempotency_key", record.getIdempotencyKey())
                .addValue("customerId", record.getCustomerId())
                .addValue("periodStart", Timestamp.from(record.getPeriodStart()))
                .addValue("periodEnd", Timestamp.from(record.getPeriodEnd()))
                .addValue("amount", record.getAmount())
                .addValue("status", record.getStatus().name().toLowerCase());

        String sql = "INSERT INTO billing_records (idempotency_key, customer_id, period_start, period_end, amount, status) "
                + "VALUES (:idempotency_key, :customerId, :periodStart, :periodEnd, :amount, :status) RETURNING *";

        try {
            return jdbcTemplate.queryForObject(sql, params, (rs, rowNum) -> new BillingRecord(
                    rs.getLong("id"),
                    rs.getString("idempotency_key"),
                    rs.getLong("customer_id"),
                    rs.getTimestamp("period_start").toInstant(),
                    rs.getTimestamp("period_end").toInstant(),
                    rs.getBigDecimal("amount"),
                    BillingRecordStatus.valueOf(rs.getString("status").toUpperCase()),
                    rs.getTimestamp("created_at").toInstant(),
                    rs.getTimestamp("updated_at").toInstant()
            ));
        } catch (DuplicateKeyException e) {
            throw new DuplicateEventException(record.getIdempotencyKey());
        }
    }

    public void updateStatus(Long id, BillingRecordStatus status) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("status", status.name().toLowerCase());

        String sql = "UPDATE billing_records SET status = :status, updated_at = NOW() WHERE id = :id";

        jdbcTemplate.update(sql, params);
    }

    public void delete(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        String sql = "DELETE FROM billing_records WHERE id = :id";
        jdbcTemplate.update(sql, params);
    }
}
