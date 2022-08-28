package com.home.calories.api;

import com.home.calories.model.MealHistoryFilter;
import com.home.calories.openapi.api.ApiApiDelegate;
import com.home.calories.openapi.model.*;
import com.home.calories.repository.DishFilter;
import com.home.calories.repository.MealHistoryRepository;
import com.home.calories.service.DishService;
import com.home.calories.service.MealHistoryService;
import com.home.calories.service.SuggestService;
import com.home.calories.util.PageableBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ApiDelegateImpl implements ApiApiDelegate {

    private final DishService dishService;
    private final MealHistoryService mealHistoryService;
    private final SuggestService suggestService;

    @Override
    public ResponseEntity<PageOfDishDto> pageDishes(
            String name,
            Integer page,
            Integer size,
            @Nullable String sort
    ) {
        var pageable = PageableBuilder.of(page, size).sortOrIdAsc(sort).build();
        return ResponseEntity.ok(dishService.page(new DishFilter(name), pageable));
    }

    @Override
    public ResponseEntity<DishDto> findDishById(Long dishId) {
        return ResponseEntity.ok(dishService.findByIdOrThrow(dishId));
    }

    @Override
    public ResponseEntity<DishDto> createDish(CreateDishDto createDishDto) {
        return new ResponseEntity<>(dishService.createDish(createDishDto), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<DishDto> updateDish(Long dishId, UpdateDishDto updateDishDto) {
        return ResponseEntity.ok(dishService.updateDish(dishId, updateDishDto));
    }

    @Override
    public ResponseEntity<Void> deleteDish(Long dishId) {
        dishService.deleteDish(dishId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<PortionDto> createPortion(Long dishId, CreatePortionDto createPortionDto) {
        return new ResponseEntity<>(dishService.createPortion(dishId, createPortionDto), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<PortionDto> updatePortion(Long dishId, Long portionId, UpdatePortionDto updatePortionDto) {
        return ResponseEntity.ok(dishService.updatePortion(dishId, portionId, updatePortionDto));
    }

    @Override
    public ResponseEntity<Void> deletePortion(Long dishId, Long portionId) {
        dishService.deletePortion(dishId, portionId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<MealHistoryListDto> getMealHistory(LocalDate date) {
        return ResponseEntity.ok(mealHistoryService.getMealHistory(new MealHistoryFilter(date)));
    }

    @Override
    public ResponseEntity<MealHistoryDto> createMealHistory(CreateMealHistoryDto createMealHistoryDto) {
        return new ResponseEntity<>(mealHistoryService.createMealHistory(createMealHistoryDto), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<MealHistoryDto> updateMealHistory(Long mealHistoryId, UpdateMealHistoryDto updateMealHistoryDto) {
        return ResponseEntity.ok(mealHistoryService.updateMealHistory(mealHistoryId, updateMealHistoryDto));
    }

    @Override
    public ResponseEntity<Void> deleteMealHistory(Long mealHistoryId) {
        mealHistoryService.deleteMealHistory(mealHistoryId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<SuggestDto> suggestEntity(String name, EntityTypeDto type) {
        var suggest = suggestService.suggest(name, type);
        return ResponseEntity.ok(suggest);
    }

}
