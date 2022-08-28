package com.home.calories.repository;

import com.home.calories.model.mealHistory.MealHistory;
import com.home.calories.model.mealHistory.MealHistoryFilter;
import com.home.calories.model.mealHistory.MealHistoryInsert;
import com.home.calories.model.mealHistory.MealHistoryUpdate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public class JdbcMealHistoryRepository extends JdbcRepository implements MealHistoryRepository {

    public JdbcMealHistoryRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public List<MealHistory> findAllByDate(MealHistoryFilter filter) {
        return null;
    }

    @Override
    public MealHistory insert(MealHistoryInsert insert) {
        return null;
    }

    @Override
    public MealHistory update(MealHistoryUpdate update) {
        return null;
    }

    @Override
    public void delete(Long mealHistoryId) {

    }

    @Override
    protected String table() {
        return "meal_history";
    }

    @Override
    protected Set<String> sortableColumns() {
        return Set.of();
    }

}
