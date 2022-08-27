package com.home.calories.service;

import com.home.calories.mapper.DishMapper;
import com.home.calories.model.Dish;
import com.home.calories.openapi.model.DishDto;
import com.home.calories.openapi.model.PageOfDishDto;
import com.home.calories.repository.DishFilter;
import com.home.calories.repository.DishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DishService {

    private final DishRepository dishRepository;
    private final DishMapper dishMapper;

    public PageOfDishDto page(DishFilter filter, Pageable pageable) {
        Page<Dish> page = dishRepository.find(filter, pageable);
        return new PageOfDishDto()
                .size(page.getSize())
                .number(page.getNumber())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .content(page.getContent().stream().map(dishMapper::map).toList());
    }

    public DishDto findByIdOrThrow(Long id) {
        return dishRepository.findById(id)
                .map(dishMapper::map)
                .orElseThrow(() -> new IllegalArgumentException("Dish does not exists " + id));
    }

}
