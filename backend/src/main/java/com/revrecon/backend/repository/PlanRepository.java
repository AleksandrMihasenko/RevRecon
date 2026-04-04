package com.revrecon.backend.repository;

import com.revrecon.backend.model.Plan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PlanRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public PlanRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Plan> findById(Long planId) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("planId", planId);
        String sql = "SELECT id, name, prices, created_at, updated_at FROM plans WHERE id = :planId";

        return jdbcTemplate.queryForObject(sql, params, (rs, rowNum) -> Optional.of(new Plan(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("prices"),
                rs.getTimestamp("created_at").toInstant(),
                rs.getTimestamp("updated_at").toInstant()
        )));
    }

    public List<Plan> findAll() {
        MapSqlParameterSource params = new MapSqlParameterSource();
        String sql = "SELECT id, name, prices, created_at, updated_at FROM plans";

        return jdbcTemplate.query(
                sql,
                params,
                (rs, rowNum) -> new Plan(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("prices"),
                        rs.getTimestamp("created_at").toInstant(),
                        rs.getTimestamp("updated_at").toInstant()
                )
        );
    }

    public void save(Plan plan) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", plan.getName())
                .addValue("prices", plan.getPrices());

        String sql = "INSERT INTO plans (name, prices) VALUES (:name, :prices)";

        jdbcTemplate.update(sql, params);
    }

    public void delete(Long planId) {
        MapSqlParameterSource params = new MapSqlParameterSource("planId", planId);

        String sql = "DELETE FROM plans WHERE id = :planId";

        jdbcTemplate.update(sql, params);
    }
}
