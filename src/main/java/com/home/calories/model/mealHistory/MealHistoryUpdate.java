package com.home.calories.model.mealHistory;

import java.time.Instant;

public record MealHistoryUpdate(Long mealHistoryId, Long dishId, Instant consumedAt) {
}
