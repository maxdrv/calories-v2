package com.home.calories.model.mealHistory;

import java.time.Instant;

public record MealHistoryRecord(
        Long id,
        Instant createdAt,
        Instant updatedAt,
        Long dishId,
        Instant consumedAt
) {
}
