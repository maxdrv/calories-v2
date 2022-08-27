package com.home.calories.mapper;

import com.home.calories.model.Dish;
import com.home.calories.openapi.model.DishDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = PortionMapper.class
)
public interface DishMapper {

    DishDto map(Dish dish);

}
