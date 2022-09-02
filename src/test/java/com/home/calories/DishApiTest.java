package com.home.calories;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.home.calories.openapi.model.*;
import com.home.calories.util.WithDataBase;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DishApiTest extends WithDataBase {

    @Test
    public void emptyPageOfDishes() {
        caller.getPageOfDishes("")
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
        caller.getPageOfDishes("?name=pro")
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

    @DatabaseSetup("/repository/dish/before/demo_dish.xml")
    @Test
    void getDishById() {
        caller.getDishById(1L)
                .andExpect(status().isOk())
                .andExpect(content().json("""
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
                        }""", true));
    }

    @DatabaseSetup("/repository/base_product/before/base_products_demo.xml")
    @ExpectedDatabase(
            value = "/repository/dish/after/demo_dish_created.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    @Test
    void createDish() {
        var portions = new ArrayList<CreatePortionDto>();
        portions.add(new CreatePortionDto().grams(30).baseProductId(1L));
        portions.add(new CreatePortionDto().grams(400).baseProductId(2L));

        var createDto = new CreateDishDto().name("protein 400ml").portions(portions);

        caller.createDish(createDto)
                .andExpect(status().isCreated())
                .andExpect(content().json("""
                        {
                            "id": 10000,
                            "name": "protein 400ml",
                            "portions": [
                                {
                                    "id": 10000,
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
                                    "id": 10001,
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
                        }""", true));
    }

    @DatabaseSetup("/repository/base_product/before/base_products_demo.xml")
    @ExpectedDatabase(
            value = "/repository/dish/after/empty_dish_created.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    @Test
    void createDishWithoutPortions() {
        var createDto = new CreateDishDto().name("empty dish").portions(Lists.emptyList());

        caller.createDish(createDto)
                .andExpect(status().isCreated())
                .andExpect(content().json("""
                        {
                            "id": 10000,
                            "name": "empty dish",
                            "portions": []
                        }""", true));
    }

    @DatabaseSetup("/repository/base_product/before/base_products_demo.xml")
    @Test
    void updateDishName() {
        var createDto = new CreateDishDto().name("empty dish").portions(Lists.emptyList());

        var createdId = caller.createDish(createDto)
                .andExpect(status().isCreated())
                .andExpect(content().json("""
                        {
                            "id": 10000,
                            "name": "empty dish",
                            "portions": []
                        }""", true))
                .andReturnAs(DishDto.class)
                .getId();

        caller.updateDishById(createdId, new UpdateDishDto().name("new name for empty dish"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "id": 10000,
                            "name": "new name for empty dish",
                            "portions": []
                        }""", true));
    }

    @DatabaseSetup("/repository/base_product/before/base_products_demo.xml")
    @ExpectedDatabase(
            value = "/repository/base_product/before/base_products_demo.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    @Test
    void deleteDishWithoutPortions() {
        var createDto = new CreateDishDto().name("empty dish").portions(Lists.emptyList());

        var createdId = caller.createDish(createDto)
                .andExpect(status().isCreated())
                .andExpect(content().json("""
                        {
                            "id": 10000,
                            "name": "empty dish",
                            "portions": []
                        }""", true))
                .andReturnAs(DishDto.class)
                .getId();

        caller.deleteDishById(createdId)
                .andExpect(status().isOk());

        caller.getDishById(createdId)
                .andExpect(status().isNotFound());
    }

    @DatabaseSetup("/repository/base_product/before/base_products_demo.xml")
    @ExpectedDatabase(
            value = "/repository/base_product/before/base_products_demo.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    @Test
    void deleteDishWithPortions() {
        var portions = new ArrayList<CreatePortionDto>();
        portions.add(new CreatePortionDto().grams(30).baseProductId(1L));
        portions.add(new CreatePortionDto().grams(400).baseProductId(2L));

        var createDto = new CreateDishDto().name("protein 400ml").portions(portions);

        var createdId = caller.createDish(createDto)
                .andExpect(status().isCreated())
                .andReturnAs(DishDto.class)
                .getId();

        assertThat(dishPortionMappingRepository.count()).isEqualTo(2);
        assertThat(portionRepository.count()).isEqualTo(2);

        caller.deleteDishById(createdId)
                .andExpect(status().isOk());

        assertThat(dishPortionMappingRepository.count()).isEqualTo(0);
        assertThat(portionRepository.count()).isEqualTo(0);

        caller.getDishById(createdId)
                .andExpect(status().isNotFound());
    }

    @DatabaseSetup("/repository/base_product/before/base_products_demo.xml")
    @ExpectedDatabase(
            value = "/repository/base_product/before/base_products_demo.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    @Test
    void createDishAndAddPortion() {
        var portions = new ArrayList<CreatePortionDto>();
        portions.add(new CreatePortionDto().grams(30).baseProductId(1L));

        var createDishDto = new CreateDishDto().name("protein 400ml").portions(portions);
        var createdDishId = caller.createDish(createDishDto)
                .andExpect(status().isCreated())
                .andReturnAs(DishDto.class)
                .getId();

        var createPortionDto = new CreatePortionDto().grams(400).baseProductId(2L);
        caller.addPortionToDish(createdDishId, createPortionDto)
                .andExpect(status().isCreated())
                .andExpect(content().json("""
                        {
                            "id": 10001,
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
                        """));

        caller.getDishById(createdDishId)
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "id": 10000,
                            "name": "protein 400ml",
                            "portions": [
                                {
                                    "id": 10000,
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
                                    "id": 10001,
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
                        }""", true));
    }

    @DatabaseSetup("/repository/base_product/before/base_products_demo.xml")
    @Test
    void createDishAndUpdatePortion() {
        var portions = new ArrayList<CreatePortionDto>();
        portions.add(new CreatePortionDto().grams(30).baseProductId(1L));

        var createDishDto = new CreateDishDto().name("protein 400ml").portions(portions);
        var createdDishDto = caller.createDish(createDishDto)
                .andExpect(status().isCreated())
                .andReturnAs(DishDto.class);

        var updatePortionDto = new UpdatePortionDto().grams(400).baseProductId(2L);
        caller.updatePortionById(
                        createdDishDto.getId(),
                        createdDishDto.getPortions().stream().findFirst().orElseThrow().getId(),
                        updatePortionDto
                )
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "id": 10000,
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
                        """));
    }

    @DatabaseSetup("/repository/base_product/before/base_products_demo.xml")
    @ExpectedDatabase(
            value = "/repository/dish/after/create_dish_and_delete_portion.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    @Test
    void createDishAndDeletePortion() {
        var portions = new ArrayList<CreatePortionDto>();
        portions.add(new CreatePortionDto().grams(30).baseProductId(1L));
        portions.add(new CreatePortionDto().grams(400).baseProductId(2L));

        var createDto = new CreateDishDto().name("protein 400ml").portions(portions);

        var createdDishDto = caller.createDish(createDto)
                .andExpect(status().isCreated())
                .andReturnAs(DishDto.class);

        assertThat(dishPortionMappingRepository.count()).isEqualTo(2);
        assertThat(portionRepository.count()).isEqualTo(2);

        caller.deletePortionById(
                        createdDishDto.getId(),
                        createdDishDto.getPortions().stream()
                                .filter(portion -> portion.getBaseProduct().getId() == 1L)
                                .findFirst().orElseThrow().getId()
                )
                .andExpect(status().isOk());

        assertThat(dishPortionMappingRepository.count()).isEqualTo(1);
        assertThat(portionRepository.count()).isEqualTo(1);

        caller.getDishById(createdDishDto.getId())
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "id": 10000,
                            "name": "protein 400ml",
                            "portions": [
                                {
                                    "id": 10001,
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
                        }""", true));
    }

}
