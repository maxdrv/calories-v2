package com.home.calories.model.portion;

import com.home.calories.model.Nutrients;
import com.home.calories.model.baseProduct.BaseProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Portion implements Nutrients {

    private Long id;
    private Integer grams;
    private BaseProduct baseProduct;

    @Override
    public double getKcal() {
        return calc(baseProduct.getKcal());
    }

    @Override
    public double getProteins() {
        return calc(baseProduct.getProteins());
    }

    @Override
    public double getFats() {
        return calc(baseProduct.getFats());
    }

    @Override
    public double getCarbs() {
        return calc(baseProduct.getCarbs());
    }

    private double calc(double amountPer100g) {
        if (this.grams == 0 || amountPer100g == 0) {
            return 0;
        }
        return amountPer100g * this.grams / 100;
    }

}
