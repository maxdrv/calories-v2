package com.home.calories.mapper;

import com.home.calories.model.BaseProduct;
import com.home.calories.openapi.model.BaseProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface BaseProductMapper {

    @Mapping(target = "nutrients.kcal", source = "kcal")
    @Mapping(target = "nutrients.proteins", source = "proteins")
    @Mapping(target = "nutrients.fats", source = "fats")
    @Mapping(target = "nutrients.carbs", source = "carbs")
    BaseProductDto map(BaseProduct baseProduct);

}
