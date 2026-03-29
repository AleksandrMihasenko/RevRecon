package com.revrecon.backend.repository;

import com.revrecon.backend.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class CustomerRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public CustomerRepository(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public Optional<Customer> findById(Long userId) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", userId);

        String sql = "SELECT id, name, created_at, updated_at FROM customers"
                + " WHERE id = :userId";

        return jdbcTemplate.queryForObject(
                sql,
                params,
                (rs, rowNum) -> Optional.of(new Customer(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getTimestamp("created_at").toInstant(),
                        rs.getTimestamp("updated_at").toInstant()
                ))
        );
    }

    public List<Customer> findAll() {
        MapSqlParameterSource params = new MapSqlParameterSource();

        String sql = "SELECT id, name, created_at, updated_at FROM customers"
                + " ORDER BY created_at DESC, id DESC";

        return jdbcTemplate.query(
                sql,
                params,
                (rs, rowNum) -> new Customer(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getTimestamp("created_at").toInstant(),
                        rs.getTimestamp("updated_at").toInstant()
                )
        );
    }

    public void save(Customer customer) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", customer.getName());

        String sql = "INSERT INTO customers (name) VALUES (:name)";

        jdbcTemplate.update(sql, params);
    }

    public void delete(Long userId) {
        MapSqlParameterSource params = new MapSqlParameterSource("userId", userId);

        String sql = "DELETE FROM customers WHERE id = :userId";

        jdbcTemplate.update(sql, params);
    }
}
