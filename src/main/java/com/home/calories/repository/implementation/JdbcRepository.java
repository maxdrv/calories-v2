package com.home.calories.repository.implementation;

import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class JdbcRepository {

    protected final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    protected int count() {
        String countSql = "SELECT count(*) FROM " + table();
        Integer totalElements = jdbcTemplate.queryForObject(countSql, Map.of(), Integer.class);
        return totalElements;
    }

    protected abstract String table();

    protected abstract Set<String> sortableColumns();

    protected String getSort(Pageable pageable) {
        var sqlSort = pageable.getSort().stream()
                .filter(order -> sortableColumns().contains(order.getProperty()))
                .map(order -> table() + "." + order.getProperty() + " " + order.getDirection().name())
                .collect(Collectors.joining(", "));

        if (sqlSort.isBlank()) {
            return "";
        } else {
            return "ORDER BY " + sqlSort;
        }
    }

}
