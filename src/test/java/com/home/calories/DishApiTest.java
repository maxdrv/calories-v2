package com.home.calories;

import com.home.calories.util.WithDataBase;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DishApiTest extends WithDataBase {

    @Test
    public void emptyPageOfDishes() {
        caller.pageOfDishes("")
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                          "size": 20,
                          "number": 0,
                          "totalElements": 0,
                          "totalPages": 0,
                          "content": []
                        }""", true));
    }

}
