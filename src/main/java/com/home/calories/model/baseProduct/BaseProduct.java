package com.home.calories.model.baseProduct;

import com.home.calories.model.Nutrients;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseProduct implements Nutrients {

    private Long id;
    private String name;
    private double kcal;
    private double proteins;
    private double fats;
    private double carbs;

}
