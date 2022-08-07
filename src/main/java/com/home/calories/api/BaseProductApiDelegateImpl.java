package com.home.calories.api;

import com.home.calories.openapi.api.BaseProductsApiDelegate;
import com.home.calories.openapi.model.BaseProductDto;
import com.home.calories.openapi.model.CreateBaseProductRequest;
import com.home.calories.openapi.model.PageOfBaseProductDto;
import com.home.calories.openapi.model.UpdateBaseProductRequest;
import com.home.calories.repository.BaseProductFilter;
import com.home.calories.service.BaseProductService;
import com.home.calories.util.PageableUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BaseProductApiDelegateImpl implements BaseProductsApiDelegate {

    private final BaseProductService baseProductService;

    @Override
    public ResponseEntity<PageOfBaseProductDto> pageProducts(
            @Nullable String name,
            Integer page,
            Integer size,
            @Nullable String sort
    ) {
        var sortLiteral = sort == null ? "id,asc" : sort;
        var pageRequest = PageRequest.of(
                page,
                size,
                PageableUtil.parseParameterIntoSort(sortLiteral)
        );

        var filter = new BaseProductFilter();
        filter.setName(name);

        return ResponseEntity.ok(baseProductService.page(filter, pageRequest));
    }


    @Override
    public ResponseEntity<BaseProductDto> baseProductById(Long baseProductId) {
        return ResponseEntity.ok(baseProductService.findByIdOrThrow(baseProductId));
    }

    @Override
    public ResponseEntity<BaseProductDto> createBaseProduct(CreateBaseProductRequest request) {
        return ResponseEntity.ok(baseProductService.create(request));
    }

    @Override
    public ResponseEntity<BaseProductDto> updateBaseProduct(Long baseProductId, UpdateBaseProductRequest request) {
        return ResponseEntity.ok(baseProductService.update(baseProductId, request));
    }

    @Override
    public ResponseEntity<Void> deleteBaseProduct(Long baseProductId) {
        baseProductService.delete(baseProductId);
        return ResponseEntity.ok().build();
    }
}
