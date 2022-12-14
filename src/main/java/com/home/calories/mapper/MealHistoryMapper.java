package com.home.calories.mapper;

import com.home.calories.model.mealHistory.MealHistory;
import com.home.calories.model.mealHistory.MealHistoryInsert;
import com.home.calories.model.mealHistory.MealHistoryUpdate;
import com.home.calories.openapi.model.CreateMealHistoryDto;
import com.home.calories.openapi.model.MealHistoryDto;
import com.home.calories.openapi.model.UpdateMealHistoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = {DishMapper.class, DateTimeMapper.class}
)
public interface MealHistoryMapper {

    @Mapping(target = "dishId", source = "dish.id")
    @Mapping(target = "dishName", source = "dish.name")
    @Mapping(target = "nutrients.kcal", source = "dish.kcal")
    @Mapping(target = "nutrients.proteins", source = "dish.proteins")
    @Mapping(target = "nutrients.fats", source = "dish.fats")
    @Mapping(target = "nutrients.carbs", source = "dish.carbs")
    MealHistoryDto map(MealHistory mealHistory);

    MealHistoryInsert map(CreateMealHistoryDto createDto);

    MealHistoryUpdate map(Long mealHistoryId, UpdateMealHistoryDto updateDto);

}
