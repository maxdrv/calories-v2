package com.home.calories.service;

import com.home.calories.model.BaseProduct;
import com.home.calories.openapi.model.*;
import com.home.calories.repository.BaseProductFilter;
import com.home.calories.repository.BaseProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BaseProductService {

    private final BaseProductRepository baseProductRepository;

    public PageOfBaseProductDto page(BaseProductFilter filter, Pageable pageable) {
        Page<BaseProduct> page = baseProductRepository.find(filter, pageable);
        return new PageOfBaseProductDto()
                .size(page.getSize())
                .number(page.getNumber())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .content(page.getContent().stream().map(BaseProductService::map).toList());
    }

    public BaseProductDto findByIdOrThrow(Long id) {
        return baseProductRepository.findById(id)
                .map(BaseProductService::map)
                .orElseThrow(() -> new IllegalArgumentException("Product does not exists " + id));
    }

    public BaseProductDto create(CreateBaseProductRequest request) {
        var created = baseProductRepository.insert(map(request));
        return map(created);
    }

    public BaseProductDto update(Long baseProductId, UpdateBaseProductRequest request) {
        var created = baseProductRepository.insert(map(baseProductId, request));
        return map(created);
    }

    public void delete(Long baseProductId) {
        baseProductRepository.deleteById(baseProductId);
    }

    private static BaseProductDto map(BaseProduct entity) {
        return new BaseProductDto()
                .id(entity.getId())
                .name(entity.getName())
                .nutrients(
                        new NutrientsDto()
                                .kcal(entity.getKcal())
                                .proteins(entity.getProteins())
                                .fats(entity.getFats())
                                .carbs(entity.getCarbs())
                );
    }

    private static BaseProduct map(CreateBaseProductRequest request) {
        var entity = new BaseProduct();
        entity.setName(request.getName());
        entity.setKcal(request.getNutrients().getKcal());
        entity.setProteins(request.getNutrients().getProteins());
        entity.setFats(request.getNutrients().getFats());
        entity.setCarbs(request.getNutrients().getCarbs());
        return entity;
    }

    private static BaseProduct map(Long id, UpdateBaseProductRequest request) {
        var entity = new BaseProduct();
        entity.setId(id);
        entity.setName(request.getName());
        entity.setKcal(request.getNutrients().getKcal());
        entity.setProteins(request.getNutrients().getProteins());
        entity.setFats(request.getNutrients().getFats());
        entity.setCarbs(request.getNutrients().getCarbs());
        return entity;
    }

}
