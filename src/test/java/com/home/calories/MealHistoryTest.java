package com.home.calories;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.home.calories.openapi.model.CreateMealHistoryDto;
import com.home.calories.util.WithDataBase;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;

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

}
