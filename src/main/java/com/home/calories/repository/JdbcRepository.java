package com.home.calories.repository;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Set;

public abstract class JdbcRepository {

    protected final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    protected abstract String table();

    protected abstract Set<String> sortableColumns();

}
