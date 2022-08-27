package com.home.calories.api;

import com.home.calories.openapi.api.ApiApiDelegate;
import com.home.calories.openapi.model.EntityTypeDto;
import com.home.calories.openapi.model.PageOfDishDto;
import com.home.calories.openapi.model.SuggestDto;
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
    public ResponseEntity<SuggestDto> suggestEntity(String name, EntityTypeDto type) {
        var suggest = suggestService.suggest(name, type);
        return ResponseEntity.ok(suggest);
    }

}
