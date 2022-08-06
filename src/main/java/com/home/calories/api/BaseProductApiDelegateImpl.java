package com.home.calories.api;

import com.home.calories.openapi.api.BaseProductsApiDelegate;
import com.home.calories.openapi.model.BaseProductDto;
import com.home.calories.openapi.model.CreateBaseProductRequest;
import com.home.calories.openapi.model.ListOfBaseProductDto;
import com.home.calories.openapi.model.UpdateBaseProductRequest;
import com.home.calories.service.BaseProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BaseProductApiDelegateImpl implements BaseProductsApiDelegate {

    private final BaseProductService baseProductService;

    public BaseProductApiDelegateImpl(BaseProductService baseProductService) {
        this.baseProductService = baseProductService;
    }

    @Override
    public ResponseEntity<ListOfBaseProductDto> listProducts() {
        return ResponseEntity.ok(new ListOfBaseProductDto().content(baseProductService.findAll()));
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

}
