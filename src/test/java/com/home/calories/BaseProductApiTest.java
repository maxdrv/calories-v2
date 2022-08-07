package com.home.calories;

import com.home.calories.openapi.model.CreateBaseProductRequest;
import com.home.calories.openapi.model.NutrientsDto;
import com.home.calories.util.WithDataBase;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BaseProductApiTest extends WithDataBase {

    @Test
    void insert() {
        caller.create(
                new CreateBaseProductRequest()
                        .name("name1")
                        .nutrients(
                                new NutrientsDto()
                                        .kcal(1.1)
                                        .proteins(1.2)
                                        .fats(1.3)
                                        .carbs(1.4)
                        )
        )
                .andExpect(status().isOk());

    }

}
