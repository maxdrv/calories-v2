package com.home.calories.service;

import com.home.calories.mapper.GodlikeMapper;
import com.home.calories.model.BaseProduct;
import com.home.calories.model.Dish;
import com.home.calories.model.Portion;
import com.home.calories.openapi.model.PageOfDishDto;
import com.home.calories.repository.DishFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DishService {

    private final GodlikeMapper mapper;

    public PageOfDishDto page(DishFilter filter, Pageable pageable) {
        // stub
        BaseProduct milk = new BaseProduct();
        milk.setId(10000000L);
        milk.setName("milk");
        milk.setKcal(60);
        milk.setProteins(3);
        milk.setFats(3.2);
        milk.setCarbs(5);

        Portion milk200 = new Portion();
        milk200.setId(10000000L);
        milk200.setGrams(200);
        milk200.setBaseProduct(milk);

        List<Portion> portions = new ArrayList<>();
        portions.add(milk200);

        Dish dish = new Dish();
        dish.setId(10000000L);
        dish.setName("capuchino");
        dish.setPortions(portions);

        List<Dish> dishes = new ArrayList<>();
        dishes.add(dish);

        var page = new PageImpl<>(dishes, pageable, 1);
        return new PageOfDishDto()
                .size(page.getSize())
                .number(page.getNumber())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .content(page.getContent().stream().map(mapper::map).toList());
    }

}
