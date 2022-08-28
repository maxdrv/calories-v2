package com.home.calories.repository;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public class JdbcDishPortionMappingRepository extends JdbcRepository implements DishPortionMappingRepository {

    public JdbcDishPortionMappingRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public int count() {
        return super.count();
    }

    @Override
    protected String table() {
        return "dish_portion_mapping";
    }

    @Override
    protected Set<String> sortableColumns() {
        return Set.of();
    }

}
