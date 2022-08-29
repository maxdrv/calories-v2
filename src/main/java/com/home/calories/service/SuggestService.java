package com.home.calories.service;

import com.home.calories.model.baseProduct.BaseProductFilter;
import com.home.calories.model.dish.DishFilter;
import com.home.calories.model.dish.DishIdentity;
import com.home.calories.openapi.model.BaseProductDto;
import com.home.calories.openapi.model.EntityDto;
import com.home.calories.openapi.model.EntityTypeDto;
import com.home.calories.openapi.model.SuggestDto;
import com.home.calories.repository.DishRepository;
import lombok.RequiredArgsConstructor;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Service;

import static com.home.calories.util.PageableUtil.MAX_SIZE_PAGE;

@Service
@RequiredArgsConstructor
public class SuggestService {

    private final BaseProductService baseProductService;
    private final DishRepository dishRepository;

    public SuggestDto suggest(String name, EntityTypeDto type) {
        return switch (type) {
            case BASE_PRODUCT -> suggestBaseProduct(name);
            case DISH -> suggestDish(name);
        };
    }

    private SuggestDto suggestBaseProduct(String name) {
        var page = baseProductService.page(new BaseProductFilter(name), MAX_SIZE_PAGE);

        var entities = StreamEx.of(page.getContent())
                .map(SuggestService::map)
                .toList();

        var suggest = new SuggestDto();
        suggest.setContent(entities);
        return suggest;
    }

    private static EntityDto map(BaseProductDto input) {
        var entity = new EntityDto();
        entity.setId(input.getId());
        entity.setName(input.getName());
        entity.setType(EntityTypeDto.BASE_PRODUCT);
        return entity;
    }

    private SuggestDto suggestDish(String name) {
        var page = dishRepository.findIdentities(new DishFilter(name), MAX_SIZE_PAGE);

        var entities = StreamEx.of(page.getContent())
                .map(SuggestService::map)
                .toList();

        var suggest = new SuggestDto();
        suggest.setContent(entities);
        return suggest;
    }

    private static EntityDto map(DishIdentity input) {
        var entity = new EntityDto();
        entity.setId(input.id());
        entity.setName(input.name());
        entity.setType(EntityTypeDto.DISH);
        return entity;
    }
}
