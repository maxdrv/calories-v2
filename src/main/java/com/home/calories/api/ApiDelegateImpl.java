package com.home.calories.api;

import com.home.calories.openapi.api.ApiApiDelegate;
import com.home.calories.openapi.model.*;
import com.home.calories.repository.DishFilter;
import com.home.calories.service.DishService;
import com.home.calories.service.SuggestService;
import com.home.calories.util.PageableBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApiDelegateImpl implements ApiApiDelegate {

    private final DishService dishService;
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
        return ApiApiDelegate.super.findDishById(dishId);
    }

    @Override
    public ResponseEntity<DishDto> createDish(CreateDishDto createDishDto) {
        return ApiApiDelegate.super.createDish(createDishDto);
    }

    @Override
    public ResponseEntity<DishDto> updateDish(Long dishId, UpdateDishDto updateDishDto) {
        return ApiApiDelegate.super.updateDish(dishId, updateDishDto);
    }

    @Override
    public ResponseEntity<Void> deleteDish(Long dishId) {
        return ApiApiDelegate.super.deleteDish(dishId);
    }

    @Override
    public ResponseEntity<PortionDto> createPortion(Long dishId, CreatePortionDto createPortionDto) {
        return ApiApiDelegate.super.createPortion(dishId, createPortionDto);
    }

    @Override
    public ResponseEntity<PortionDto> updatePortion(Long dishId, Long portionId, UpdatePortionDto updatePortionDto) {
        return ApiApiDelegate.super.updatePortion(dishId, portionId, updatePortionDto);
    }

    @Override
    public ResponseEntity<Void> deletePortion(Long dishId, Long portionId) {
        return ApiApiDelegate.super.deletePortion(dishId, portionId);
    }

    @Override
    public ResponseEntity<SuggestDto> suggestEntity(String name, EntityTypeDto type) {
        var suggest = suggestService.suggest(name, type);
        return ResponseEntity.ok(suggest);
    }

}
