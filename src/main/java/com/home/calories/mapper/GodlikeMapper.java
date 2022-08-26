package com.home.calories.mapper;

import com.home.calories.model.BaseProduct;
import com.home.calories.model.Dish;
import com.home.calories.model.Portion;
import com.home.calories.openapi.model.BaseProductDto;
import com.home.calories.openapi.model.DishDto;
import com.home.calories.openapi.model.NutrientsDto;
import com.home.calories.openapi.model.PortionDto;
import org.springframework.stereotype.Component;

import java.util.List;

// TODO move to mapstruct and separate on several specialized classes
// TODO move here other mapping functions
@Component
public class GodlikeMapper {

    public BaseProductDto map(BaseProduct entity) {
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

    public DishDto map(Dish entity) {
        return new DishDto()
                .id(entity.getId())
                .name(entity.getName())
                .portions(mapPortionList(entity.getPortions()));
    }

    private List<PortionDto> mapPortionList(List<Portion> portionList) {
        return portionList.stream().map(this::map).toList();
    }

    private PortionDto map(Portion entity) {
        return new PortionDto()
                .id(entity.getId())
                .grams(entity.getGrams())
                .baseProduct(map(entity.getBaseProduct()));
    }

}
