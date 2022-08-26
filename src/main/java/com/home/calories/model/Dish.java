package com.home.calories.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.function.ToDoubleFunction;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dish implements Nutrients {

    private Long id;
    private String name;
    private List<Portion> portions;

    @Override
    public double getKcal() {
        return calc(Portion::getKcal);
    }

    @Override
    public double getProteins() {
        return calc(Portion::getProteins);
    }

    @Override
    public double getFats() {
        return calc(Portion::getFats);
    }

    @Override
    public double getCarbs() {
        return calc(Portion::getCarbs);
    }

    private double calc(ToDoubleFunction<Portion> getPropertyToCount) {
        return this.portions.stream()
                .mapToDouble(getPropertyToCount)
                .sum();
    }
}
