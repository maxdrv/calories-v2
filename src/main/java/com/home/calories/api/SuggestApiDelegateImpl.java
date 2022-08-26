package com.home.calories.api;

import com.home.calories.openapi.api.ApiApiDelegate;
import com.home.calories.openapi.model.*;
import com.home.calories.repository.BaseProductFilter;
import com.home.calories.repository.DishFilter;
import com.home.calories.service.BaseProductService;
import com.home.calories.service.DishService;
import com.home.calories.util.PageableBuilder;
import lombok.RequiredArgsConstructor;
import one.util.streamex.StreamEx;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import static com.home.calories.util.PageableUtil.MAX_SIZE_PAGE;

@Component
@RequiredArgsConstructor
public class SuggestApiDelegateImpl implements ApiApiDelegate {

    private final BaseProductService baseProductService;
    private final DishService dishService;

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
    public ResponseEntity<SuggestDto> suggestEntityByName(String name) {
        var page = baseProductService.page(new BaseProductFilter(name), MAX_SIZE_PAGE);
        return ResponseEntity.ok(map(page));
    }

    private static SuggestDto map(PageOfBaseProductDto page) {
        var entities = StreamEx.of(page.getContent())
                .map(SuggestApiDelegateImpl::map)
                .toList();

        var suggest = new SuggestDto();
        suggest.setContent(entities);
        return suggest;
    }

    private static EntityDto map(BaseProductDto input) {
        var entity = new EntityDto();
        entity.setId(input.getId());
        entity.setName(input.getName());
        return entity;
    }

}
