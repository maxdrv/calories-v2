package com.home.calories;

import com.github.springtestdbunit.annotation.DatabaseSetup;
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

    @DatabaseSetup("/repository/dish/before/demo_dish.xml")
    @Test
    public void pageOfDishesWithSingleDish() {
        caller.pageOfDishes("?name=pro")
                .andExpect(status().isOk())
                .andExpect(content().json("""
                           {
                             "size": 20,
                             "number": 0,
                             "totalElements": 1,
                             "totalPages": 1,
                             "content": [
                               {
                                   "id": 1,
                                   "name": "protein 400ml",
                                   "portions": [
                                       {
                                           "id": 1,
                                           "grams": 30,
                                           "baseProduct": {
                                                "id": 1,
                                                "name": "protein",
                                                "nutrients": {
                                                  "kcal": 150.0,
                                                  "proteins": 23.0,
                                                  "fats": 4.0,
                                                  "carbs": 10.0
                                                }
                                           }
                                       },
                                       {
                                           "id": 2,
                                           "grams": 400,
                                           "baseProduct": {
                                                "id": 2,
                                                "name": "milk",
                                                "nutrients": {
                                                  "kcal": 11.0,
                                                  "proteins": 14.0,
                                                  "fats": 13.0,
                                                  "carbs": 12.0
                                                }
                                           }
                                       }
                                   ]
                               }
                             ]
                           }""", true));
    }

}
