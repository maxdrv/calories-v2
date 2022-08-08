package com.home.calories.util;

import com.home.calories.openapi.model.CreateBaseProductRequest;
import com.home.calories.openapi.model.NutrientsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Repo {

    public static final CreateBaseProductRequest CREATE_MILK_REQUEST = new CreateBaseProductRequest()
            .name("milk")
            .nutrients(
                    new NutrientsDto()
                            .proteins(14.0)
                            .fats(13.0)
                            .carbs(12.0)
                            .kcal(11.0)
            );

    public static final CreateBaseProductRequest CREATE_PROTEIN_REQUEST = new CreateBaseProductRequest()
            .name("protein")
            .nutrients(
                    new NutrientsDto()
                            .proteins(23.0)
                            .fats(4.0)
                            .carbs(10.0)
                            .kcal(150.0)
            );

}
