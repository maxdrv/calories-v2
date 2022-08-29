package com.home.calories.repository;

import com.home.calories.model.dish.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DishRepository {

    Page<Dish> find(DishFilter filter, Pageable pageable);

    Page<Long> findIds(DishFilter filter, Pageable pageable);

    Page<DishIdentity> findIdentities(DishFilter filter, Pageable pageable);

    Optional<Dish> findById(Long id);

    Map<Long, Dish> findByIds(List<Long> ids);

    Dish insert(DishInsert insert);

    Dish update(DishUpdate update);

    void deleteDish(Long dishId);

}
