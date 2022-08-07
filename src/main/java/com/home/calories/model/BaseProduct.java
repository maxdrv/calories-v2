package com.home.calories.model;

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
