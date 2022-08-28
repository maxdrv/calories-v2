package com.home.calories.repository;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public class JdbcPortionRepository extends JdbcRepository implements PortionRepository {

    public JdbcPortionRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public int count() {
        return super.count();
    }

    @Override
    protected String table() {
        return "portion";
    }

    @Override
    protected Set<String> sortableColumns() {
        return Set.of();
    }

}
