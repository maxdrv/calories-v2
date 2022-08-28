package com.home.calories.repository;

import com.home.calories.model.Dish;
import com.home.calories.model.DishIdentity;
import com.home.calories.model.DishInsert;
import com.home.calories.model.PortionInsert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface DishRepository {

    Page<Dish> find(DishFilter filter, Pageable pageable);

    Page<Long> findIds(DishFilter filter, Pageable pageable);

    Page<DishIdentity> findIdentities(DishFilter filter, Pageable pageable);

    Optional<Dish> findById(Long id);

    Dish insert(DishInsert insert);

    void insertPortions(List<PortionInsert> portionInserts);

}
