package com.home.calories.service;

import com.home.calories.mapper.DishMapper;
import com.home.calories.mapper.PortionMapper;
import com.home.calories.model.Dish;
import com.home.calories.openapi.model.*;
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
    private final PortionMapper portionMapper;

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

    public DishDto createDish(CreateDishDto createDishDto) {
        Dish created = dishRepository.insert(dishMapper.map(createDishDto));

        var portionInserts = createDishDto.getPortions().stream()
                .map(createPortion -> portionMapper.map(created.getId(), createPortion))
                .toList();

        dishRepository.insertPortions(portionInserts);
        return findByIdOrThrow(created.getId());
    }

    public DishDto updateDish(Long dishId, UpdateDishDto updateDishDto) {
        Dish updated = dishRepository.update(dishMapper.map(dishId, updateDishDto));
        return findByIdOrThrow(updated.getId());
    }

    public void deleteDish(Long dishId) {

    }

    public PortionDto createPortion(Long dishId, CreatePortionDto createPortionDto) {
        return null;
    }

    public PortionDto updatePortion(Long dishId, Long portionId, UpdatePortionDto updatePortionDto) {
        return null;
    }

    public void deletePortion(Long dishId, Long portionId) {

    }
}
