package com.home.calories;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.home.calories.openapi.model.BaseProductDto;
import com.home.calories.openapi.model.UpdateBaseProductRequest;
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
        caller.create(Repo.CREATE_MILK_REQUEST.get())
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

    @ExpectedDatabase(
            value = "/repository/base_product/after/several_products.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    @Test
    void createSeveralBaseProducts() {
        caller.create(Repo.CREATE_MILK_REQUEST.get())
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
        caller.create(Repo.CREATE_PROTEIN_REQUEST.get())
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                           {
                             "id": 10001,
                             "name": "protein",
                             "nutrients": {
                               "kcal": 150.0,
                               "proteins": 23.0,
                               "fats": 4.0,
                               "carbs": 10.0
                             }
                           }
                           """,
                        true
                ));
    }

    @ExpectedDatabase(
            value = "/repository/base_product/after/milk_updated.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    @Test
    void updateBaseProduct() {
        var baseProduct = caller.create(Repo.CREATE_MILK_REQUEST.get())
                .andExpect(status().isOk())
                .andReturnAs(BaseProductDto.class);

        var nutrients = baseProduct.getNutrients()
                .kcal(1000.0)
                .proteins(1001.0)
                .fats(1002.0)
                .carbs(1003.0);

        caller.update(
                        baseProduct.getId(),
                        new UpdateBaseProductRequest()
                                .name(baseProduct.getName() + "_updated")
                                .nutrients(nutrients)
                ).andExpect(status().isOk())
                .andExpect(content().json(
                        """
                          {
                             "id": 10000,
                             "name": "milk_updated",
                             "nutrients": {
                               "kcal": 1000.0,
                               "proteins": 1001.0,
                               "fats": 1002.0,
                               "carbs": 1003.0
                             }
                           }
                          """,
                        true
                ));

    }

    @ExpectedDatabase(
            value = "/repository/base_product/after/products_after_delete.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    @DatabaseSetup("/repository/base_product/before/products_before_delete.xml")
    @Test
    void deleteBaseProduct() {
        caller.delete(2L).andExpect(status().isOk());
    }

    @Test
    void queryBaseProductPage() {
        for (int i = 1; i < 110; i++) {
            var request = Repo.CREATE_PROTEIN_REQUEST.get();
            request.name(request.getName() + i);
            caller.create(request).andExpect(status().isOk());
        }

        caller.page("?page=0&size=10")
                .andExpect(status().isOk());
    }

}
