package com.home.calories.api;

import com.home.calories.model.baseProduct.BaseProductFilter;
import com.home.calories.model.dish.DishFilter;
import com.home.calories.model.mealHistory.MealHistoryFilter;
import com.home.calories.openapi.api.ApiApiDelegate;
import com.home.calories.openapi.model.*;
import com.home.calories.service.BaseProductService;
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

    private final BaseProductService baseProductService;
    private final DishService dishService;
    private final MealHistoryService mealHistoryService;
    private final SuggestService suggestService;

    @Override
    public ResponseEntity<PageOfBaseProductDto> getPageOfBaseProducts(
            @Nullable String name,
            Integer page,
            Integer size,
            @Nullable String sort
    ) {
        var pageable = PageableBuilder.of(page, size).sortOrIdAsc(sort).build();
        return ResponseEntity.ok(baseProductService.page(new BaseProductFilter(name), pageable));
    }

    @Override
    public ResponseEntity<BaseProductDto> getBaseProductById(Long baseProductId) {
        return ResponseEntity.ok(baseProductService.findByIdOrThrow(baseProductId));
    }

    @Override
    public ResponseEntity<BaseProductDto> createBaseProduct(CreateBaseProductRequest createBaseProductRequest) {
        return new ResponseEntity<>(baseProductService.create(createBaseProductRequest), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<BaseProductDto> updateBaseProductById(Long baseProductId, UpdateBaseProductRequest updateBaseProductRequest) {
        return ResponseEntity.ok(baseProductService.update(baseProductId, updateBaseProductRequest));
    }

    @Override
    public ResponseEntity<Void> deleteBaseProductById(Long baseProductId) {
        baseProductService.delete(baseProductId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<PageOfDishDto> getPageOfDishes(
            String name,
            Integer page,
            Integer size,
            @Nullable String sort
    ) {
        var pageable = PageableBuilder.of(page, size).sortOrIdAsc(sort).build();
        return ResponseEntity.ok(dishService.page(new DishFilter(name), pageable));
    }

    @Override
    public ResponseEntity<DishDto> getDishById(Long dishId) {
        return ResponseEntity.ok(dishService.findByIdOrThrow(dishId));
    }

    @Override
    public ResponseEntity<DishDto> createDish(CreateDishDto createDishDto) {
        return new ResponseEntity<>(dishService.createDish(createDishDto), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<DishDto> updateDishById(Long dishId, UpdateDishDto updateDishDto) {
        return ResponseEntity.ok(dishService.updateDish(dishId, updateDishDto));
    }

    @Override
    public ResponseEntity<Void> deleteDishById(Long dishId) {
        dishService.deleteDish(dishId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<PortionDto> addPortionToDish(Long dishId, CreatePortionDto createPortionDto) {
        return new ResponseEntity<>(dishService.createPortion(dishId, createPortionDto), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<PortionDto> updatePortionById(Long dishId, Long portionId, UpdatePortionDto updatePortionDto) {
        return ResponseEntity.ok(dishService.updatePortion(dishId, portionId, updatePortionDto));
    }

    @Override
    public ResponseEntity<Void> deletePortionById(Long dishId, Long portionId) {
        dishService.deletePortion(dishId, portionId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<MealHistoryListDto> getMealHistoryList(LocalDate date) {
        return ResponseEntity.ok(mealHistoryService.getMealHistory(new MealHistoryFilter(date)));
    }

    @Override
    public ResponseEntity<MealHistoryDto> createMealHistory(CreateMealHistoryDto createMealHistoryDto) {
        return new ResponseEntity<>(mealHistoryService.createMealHistory(createMealHistoryDto), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<MealHistoryDto> updateMealHistoryById(Long mealHistoryId, UpdateMealHistoryDto updateMealHistoryDto) {
        return ResponseEntity.ok(mealHistoryService.updateMealHistory(mealHistoryId, updateMealHistoryDto));
    }

    @Override
    public ResponseEntity<Void> deleteMealHistoryById(Long mealHistoryId) {
        mealHistoryService.deleteMealHistory(mealHistoryId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<SuggestDto> suggestEntity(String name, EntityTypeDto type) {
        var suggest = suggestService.suggest(name, type);
        return ResponseEntity.ok(suggest);
    }

}
