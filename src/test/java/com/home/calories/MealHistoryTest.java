package com.home.calories;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.home.calories.openapi.model.CreateMealHistoryDto;
import com.home.calories.openapi.model.MealHistoryDto;
import com.home.calories.openapi.model.UpdateMealHistoryDto;
import com.home.calories.util.WithDataBase;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MealHistoryTest extends WithDataBase {

    @MockBean
    Clock clock;

    @DatabaseSetup("/repository/dish/before/demo_dish.xml")
    @Test
    void createMealHistoryRecord() {
        var offsetDateTime = OffsetDateTime.parse("1970-01-01T03:16:40+03:00");

        doReturn(offsetDateTime.toInstant()).when(clock).instant();
        doReturn(ZoneId.systemDefault()).when(clock).getZone();

        var create = new CreateMealHistoryDto().dishId(1L);
        caller.createMealHistory(create)
                .andExpect(status().isCreated())
                .andExpect(content().json("""
                        {
                            "id": 10000,
                            "createdAt": "1970-01-01T03:16:40+03:00",
                            "updatedAt": "1970-01-01T03:16:40+03:00",
                            "dishId": 1,
                            "dishName": "protein 400ml",
                            "consumedAt": "1970-01-01T03:16:40+03:00",
                            "nutrients": {
                                "kcal": 89.0,
                                "proteins": 62.9,
                                "fats": 53.2,
                                "carbs": 51.0
                            }
                        }
                        """));
    }

    @DatabaseSetup(value = {
            "/repository/dish/before/demo_dish.xml",
            "/repository/dish/before/demo_dish_2.xml"
    })
    @Test
    void updateMealHistoryRecord() {
        var offsetDateTime = OffsetDateTime.parse("1970-01-01T03:16:40+03:00");

        doReturn(offsetDateTime.toInstant()).when(clock).instant();
        doReturn(ZoneId.systemDefault()).when(clock).getZone();

        var create = new CreateMealHistoryDto().dishId(1L);
        var createdId = caller.createMealHistory(create)
                .andExpect(status().isCreated())
                .andReturnAs(MealHistoryDto.class)
                .getId();

        var newOffsetDateTime = OffsetDateTime.parse("1970-01-01T15:16:40+03:00");
        doReturn(newOffsetDateTime.toInstant()).when(clock).instant();
        doReturn(ZoneId.systemDefault()).when(clock).getZone();

        var update = new UpdateMealHistoryDto().dishId(2L).consumedAt(OffsetDateTime.parse("1970-01-01T16:16:40+03:00"));
        caller.updateMealHistoryById(createdId, update)
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "id": 10000,
                            "createdAt": "1970-01-01T03:16:40+03:00",
                            "updatedAt": "1970-01-01T15:16:40+03:00",
                            "dishId": 2,
                            "dishName": "turkey fillet in bread crumbs",
                            "consumedAt": "1970-01-01T16:16:40+03:00",
                            "nutrients": {
                                "kcal": 345.3,
                                "proteins": 74.985,
                                "fats": 6.03,
                                "carbs": 22.014000000000003
                            }
                        }
                        """));
    }

    @DatabaseSetup("/repository/dish/before/demo_dish.xml")
    @Test
    void deleteMealHistoryRecord() {
        var offsetDateTime = OffsetDateTime.parse("1970-01-01T03:16:40+03:00");

        doReturn(offsetDateTime.toInstant()).when(clock).instant();
        doReturn(ZoneId.systemDefault()).when(clock).getZone();

        var create = new CreateMealHistoryDto().dishId(1L);
        var createdId = caller.createMealHistory(create)
                .andExpect(status().isCreated())
                .andReturnAs(MealHistoryDto.class)
                .getId();

        assertThat(mealHistoryRepository.findById(createdId)).isNotEmpty();

        caller.deleteMealHistoryById(createdId).andExpect(status().isOk());
        assertThat(mealHistoryRepository.findById(createdId)).isEmpty();
    }

    @DatabaseSetup("/repository/dish/before/demo_dish.xml")
    @Test
    void emptySearch() {
        caller.getMealHistoryList("?date=1970-01-01")
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "content": []
                        }
                        """));
    }

    @DatabaseSetup("/repository/dish/before/demo_dish.xml")
    @Test
    void findOneMealHistory() {
        var offsetDateTime = OffsetDateTime.parse("1970-01-01T03:16:40+03:00");

        doReturn(offsetDateTime.toInstant()).when(clock).instant();
        doReturn(ZoneId.systemDefault()).when(clock).getZone();

        var create = new CreateMealHistoryDto().dishId(1L);
        caller.createMealHistory(create)
                .andExpect(status().isCreated());

        caller.getMealHistoryList("?date=1970-01-01")
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "content": [
                                {
                                    "id": 10000,
                                    "createdAt": "1970-01-01T03:16:40+03:00",
                                    "updatedAt": "1970-01-01T03:16:40+03:00",
                                    "dishId": 1,
                                    "dishName": "protein 400ml",
                                    "consumedAt": "1970-01-01T03:16:40+03:00",
                                    "nutrients": {
                                        "kcal": 89.0,
                                        "proteins": 62.9,
                                        "fats": 53.2,
                                        "carbs": 51.0
                                    }
                                }
                            ]
                        }
                        """));
    }

    @DatabaseSetup(value = {
            "/repository/dish/before/demo_dish.xml",
            "/repository/dish/before/demo_dish_2.xml"
    })
    @Test
    void findSeveralMealHistory() {
        var offsetDateTime = OffsetDateTime.parse("1970-01-01T03:16:40+03:00");

        doReturn(offsetDateTime.toInstant()).when(clock).instant();
        doReturn(ZoneId.systemDefault()).when(clock).getZone();

        var create = new CreateMealHistoryDto().dishId(1L);
        var createdId = caller.createMealHistory(create)
                .andExpect(status().isCreated())
                .andReturnAs(MealHistoryDto.class)
                .getId();

        var newOffsetDateTime = OffsetDateTime.parse("1970-01-01T15:16:40+03:00");
        doReturn(newOffsetDateTime.toInstant()).when(clock).instant();
        doReturn(ZoneId.systemDefault()).when(clock).getZone();

        var update = new UpdateMealHistoryDto().dishId(2L).consumedAt(OffsetDateTime.parse("1970-01-01T16:16:40+03:00"));
        caller.updateMealHistoryById(createdId, update)
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "id": 10000,
                            "createdAt": "1970-01-01T03:16:40+03:00",
                            "updatedAt": "1970-01-01T15:16:40+03:00",
                            "dishId": 2,
                            "dishName": "turkey fillet in bread crumbs",
                            "consumedAt": "1970-01-01T16:16:40+03:00",
                            "nutrients": {
                                "kcal": 345.3,
                                "proteins": 74.985,
                                "fats": 6.03,
                                "carbs": 22.014000000000003
                            }
                        }
                        """));
    }

}
