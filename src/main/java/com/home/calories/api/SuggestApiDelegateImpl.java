package com.home.calories.api;

import com.home.calories.openapi.api.ApiApiDelegate;
import com.home.calories.openapi.model.BaseProductDto;
import com.home.calories.openapi.model.EntityDto;
import com.home.calories.openapi.model.PageOfBaseProductDto;
import com.home.calories.openapi.model.SuggestDto;
import com.home.calories.repository.BaseProductFilter;
import com.home.calories.service.BaseProductService;
import lombok.RequiredArgsConstructor;
import one.util.streamex.StreamEx;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SuggestApiDelegateImpl implements ApiApiDelegate {

    private final BaseProductService baseProductService;

    @Override
    public ResponseEntity<SuggestDto> suggestEntityByName(String name) {
        var pageRequest = PageRequest.of(0, Integer.MAX_VALUE, Sort.by("id"));

        var filter = new BaseProductFilter();
        filter.setName(name);

        var page =baseProductService.page(filter, pageRequest);
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
