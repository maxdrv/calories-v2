package com.home.calories.model;

import java.time.Instant;

public record MealHistoryUpdate(Long mealHistoryId, Long dishId, Instant consumedAt) {
}
