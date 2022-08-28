package com.home.calories.mapper;

import com.home.calories.model.Dish;
import com.home.calories.model.DishInsert;
import com.home.calories.model.DishUpdate;
import com.home.calories.openapi.model.CreateDishDto;
import com.home.calories.openapi.model.DishDto;
import com.home.calories.openapi.model.UpdateDishDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = PortionMapper.class
)
public interface DishMapper {

    DishDto map(Dish dish);

    DishInsert map(CreateDishDto dto);

    DishUpdate map(Long dishId, UpdateDishDto dto);

}
