package com.home.calories.service;

import com.home.calories.mapper.MealHistoryMapper;
import com.home.calories.model.mealHistory.MealHistory;
import com.home.calories.model.mealHistory.MealHistoryFilter;
import com.home.calories.openapi.model.CreateMealHistoryDto;
import com.home.calories.openapi.model.MealHistoryDto;
import com.home.calories.openapi.model.MealHistoryListDto;
import com.home.calories.openapi.model.UpdateMealHistoryDto;
import com.home.calories.repository.MealHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MealHistoryService {

    private final MealHistoryRepository mealHistoryRepository;
    private final MealHistoryMapper mealHistoryMapper;

    public MealHistoryListDto getMealHistory(MealHistoryFilter filter) {
        List<MealHistory> allByDate = mealHistoryRepository.findAllByDate(filter);
        return new MealHistoryListDto().content(allByDate.stream().map(mealHistoryMapper::map).toList());
    }

    public MealHistoryDto createMealHistory(CreateMealHistoryDto createMealHistoryDto) {
        MealHistory created = mealHistoryRepository.insert(mealHistoryMapper.map(createMealHistoryDto));
        return mealHistoryMapper.map(created);
    }

    public MealHistoryDto updateMealHistory(Long mealHistoryId, UpdateMealHistoryDto updateMealHistoryDto) {
        MealHistory updated = mealHistoryRepository.update(mealHistoryMapper.map(mealHistoryId, updateMealHistoryDto));
        return mealHistoryMapper.map(updated);
    }

    public void deleteMealHistory(Long mealHistoryId) {
        mealHistoryRepository.delete(mealHistoryId);
    }

}
