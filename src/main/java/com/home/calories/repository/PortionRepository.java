package com.home.calories.repository;

import com.home.calories.model.Portion;
import com.home.calories.model.PortionInsert;
import com.home.calories.model.PortionUpdate;

import java.util.List;
import java.util.Optional;

public interface PortionRepository {

    int count();

    Optional<Portion> findById(Long portionId);

    void insertPortions(List<PortionInsert> portionInserts);

    Portion insertPortion(PortionInsert portionInsert);

    Portion updatePortion(PortionUpdate portionUpdate);

    default void deletePortion(Long dishId, Long portionId) {
        deletePortions(dishId, List.of(portionId));
    }

    void deletePortions(Long dishId, List<Long> portionsIds);

}
