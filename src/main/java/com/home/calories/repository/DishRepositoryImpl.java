package com.home.calories.repository;

import com.home.calories.model.Dish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class DishRepositoryImpl implements DishRepository {

    @Override
    public Page<Dish> find(DishFilter filter, Pageable pageable) {
        return null;
    }

}
