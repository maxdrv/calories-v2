package com.home.calories;

import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.home.calories.util.Repo;
import com.home.calories.util.WithDataBase;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BaseProductApiTest extends WithDataBase {

    @SneakyThrows
    @Test
    @ExpectedDatabase(
            value = "/repository/base_product/after/milk.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    void createBaseProduct() {
        caller.create(Repo.CREATE_MILK_REQUEST)
                .andExpect(status().isOk())
                .andExpect(content().json(
                """
                   {
                     "id": 10000,
                     "name": "milk",
                     "nutrients": {
                       "kcal": 11.0,
                       "proteins": 14.0,
                       "fats": 13.0,
                       "carbs": 12.0
                     }
                   }
                   """,
                        true
                ));
    }

    @Test
    void createSeveralBaseProducts() {

    }

    @Test
    void updateBaseProduct() {

    }

    @Test
    void deleteBaseProduct() {

    }

    void queryBaseProductPage() {

    }

}
