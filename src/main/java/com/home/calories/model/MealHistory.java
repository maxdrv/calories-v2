package com.home.calories.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealHistory {

    private Long id;
    private Instant createdAt;
    private Instant updatedAt;
    private Dish dish;
    private Instant consumedAt;

}
