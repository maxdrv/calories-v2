package com.home.calories.repository;

import com.home.calories.model.portion.Portion;
import com.home.calories.model.portion.PortionInsert;
import com.home.calories.model.portion.PortionUpdate;

import java.util.List;
import java.util.Optional;

public interface PortionRepository {

    int count();

    Optional<Portion> findById(Long portionId);

    void insertPortions(List<PortionInsert> portionInserts);

    Portion insertPortion(PortionInsert portionInsert);

    Portion updatePortion(PortionUpdate portionUpdate);

    void deletePortion(Long dishId, Long portionId);

    void deletePortionsByDish(Long dishId);

}
