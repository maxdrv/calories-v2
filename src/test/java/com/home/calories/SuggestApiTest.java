package com.home.calories;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.home.calories.openapi.model.EntityTypeDto;
import com.home.calories.util.Repo;
import com.home.calories.util.WithDataBase;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SuggestApiTest extends WithDataBase {

    @Test
    void suggestBaseProductByName() {
        var request1 = Repo.CREATE_PROTEIN_REQUEST.get();
        request1.name("ABC");
        caller.createBaseProduct(request1).andExpect(status().isCreated());

        var request2 = Repo.CREATE_PROTEIN_REQUEST.get();
        request2.name("ABCD");
        caller.createBaseProduct(request2).andExpect(status().isCreated());

        var request3 = Repo.CREATE_PROTEIN_REQUEST.get();
        request3.name("ABCDE");
        caller.createBaseProduct(request3).andExpect(status().isCreated());

        caller.suggestEntity("BCD", EntityTypeDto.BASE_PRODUCT)
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "content": [
                                { "id": 10001, type: "BASE_PRODUCT", "name": "ABCD" },
                                { "id": 10002, type: "BASE_PRODUCT", "name": "ABCDE" }
                            ]
                        }
                        """));
    }

    @DatabaseSetup("/repository/dish/before/demo_dish.xml")
    @Test
    void suggestDishByName() {
        caller.suggestEntity("pro", EntityTypeDto.DISH)
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "content": [
                                { "id": 1, type: "DISH", "name": "protein 400ml" }
                            ]
                        }
                        """));
    }

}
