package com.home.calories.repository;

import com.home.calories.model.MealHistory;
import com.home.calories.model.MealHistoryFilter;
import com.home.calories.model.MealHistoryInsert;
import com.home.calories.model.MealHistoryUpdate;

import java.util.List;

public interface MealHistoryRepository {

    List<MealHistory> findAllByDate(MealHistoryFilter filter);

    MealHistory insert(MealHistoryInsert insert);

    MealHistory update(MealHistoryUpdate update);

    void delete(Long mealHistoryId);

}
