package com.home.calories.repository;

import com.home.calories.model.mealHistory.MealHistory;
import com.home.calories.model.mealHistory.MealHistoryFilter;
import com.home.calories.model.mealHistory.MealHistoryInsert;
import com.home.calories.model.mealHistory.MealHistoryUpdate;

import java.util.List;
import java.util.Optional;

public interface MealHistoryRepository {

    Optional<MealHistory> findById(Long id);

    List<MealHistory> findAllByDate(MealHistoryFilter filter);

    MealHistory insert(MealHistoryInsert insert);

    MealHistory update(MealHistoryUpdate update);

    void delete(Long mealHistoryId);

}
