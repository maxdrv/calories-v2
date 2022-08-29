package com.home.calories.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.calories.openapi.model.*;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@Component
public class ApiCaller {

    private static final String DISH_URL = "/api/v1/dish";
    private static final String DISH_BY_ID_URL = "/api/v1/dish/{dishId}";
    private static final String PORTION_URL = "/api/v1/dish/{dishId}/portion";
    private static final String PORTION_BY_ID_URL = "/api/v1/dish/{dishId}/portion/{portionId}";
    private static final String MEAL_HISTORY_URL = "/api/v1/mealHistory";
    private static final String MEAL_HISTORY_BY_ID_URL = "/api/v1/mealHistory/{mealHistoryId}";
    private static final String SUGGEST_URL = "/api/v1/suggest";

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    public ApiCaller(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
    }

    @SneakyThrows
    public SneakyResultActions pageOfProducts(String params) {
        return new SneakyResultActions(
                mockMvc.perform(get("/baseProducts" + params))
        );
    }

    @SneakyThrows
    public SneakyResultActions create(CreateBaseProductRequest request) {
        return new SneakyResultActions(
                mockMvc.perform(
                        post("/baseProducts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
        );
    }

    @SneakyThrows
    public SneakyResultActions update(Long baseProductId, UpdateBaseProductRequest request) {
        return new SneakyResultActions(
                mockMvc.perform(
                        put("/baseProducts/{baseProductId}", baseProductId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
        );
    }

    @SneakyThrows
    public SneakyResultActions delete(Long baseProductId) {
        return new SneakyResultActions(
                mockMvc.perform(
                        MockMvcRequestBuilders.delete("/baseProducts/{baseProductId}", baseProductId)
                )
        );
    }

    @SneakyThrows
    public SneakyResultActions suggest(String name, EntityTypeDto type) {
        return new SneakyResultActions(
                mockMvc.perform(
                        MockMvcRequestBuilders.get(SUGGEST_URL + "?name=" + name + "&type=" + type.name())
                )
        );
    }

    @SneakyThrows
    public SneakyResultActions pageOfDishes(String params) {
        return new SneakyResultActions(
                mockMvc.perform(get(DISH_URL + params))
        );
    }

    @SneakyThrows
    public SneakyResultActions findDishById(Long dishId) {
        return new SneakyResultActions(
                mockMvc.perform(get(DISH_BY_ID_URL, dishId))
        );
    }

    @SneakyThrows
    public SneakyResultActions createDish(CreateDishDto dto) {
        return new SneakyResultActions(
                mockMvc.perform(
                        post(DISH_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
        );
    }

    @SneakyThrows
    public SneakyResultActions updateDish(Long dishId, UpdateDishDto dto) {
        return new SneakyResultActions(
                mockMvc.perform(
                        put(DISH_BY_ID_URL, dishId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
        );
    }

    @SneakyThrows
    public SneakyResultActions deleteDish(Long dishId) {
        return new SneakyResultActions(
                mockMvc.perform(
                        MockMvcRequestBuilders.delete(DISH_BY_ID_URL, dishId)
                )
        );
    }

    @SneakyThrows
    public SneakyResultActions createPortion(Long dishId, CreatePortionDto dto) {
        return new SneakyResultActions(
                mockMvc.perform(
                        post(PORTION_URL, dishId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
        );
    }

    @SneakyThrows
    public SneakyResultActions updatePortion(Long dishId, Long portionId, UpdatePortionDto dto) {
        return new SneakyResultActions(
                mockMvc.perform(
                        put(PORTION_BY_ID_URL, dishId, portionId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
        );
    }

    @SneakyThrows
    public SneakyResultActions deletePortion(Long dishId, Long portionId) {
        return new SneakyResultActions(
                mockMvc.perform(
                        MockMvcRequestBuilders.delete(PORTION_BY_ID_URL, dishId, portionId)
                )
        );
    }

    @SneakyThrows
    public SneakyResultActions getMealHistory(String params) {
        return new SneakyResultActions(
                mockMvc.perform(
                        get(MEAL_HISTORY_URL + params)
                )
        );
    }

    @SneakyThrows
    public SneakyResultActions createMealHistory(CreateMealHistoryDto dto) {
        return new SneakyResultActions(
                mockMvc.perform(
                        post(MEAL_HISTORY_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
        );
    }


    @SneakyThrows
    public SneakyResultActions updateMealHistory(Long mealHistoryId, UpdateMealHistoryDto dto) {
        return new SneakyResultActions(
                mockMvc.perform(
                        put(MEAL_HISTORY_BY_ID_URL, mealHistoryId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
        );
    }

    @SneakyThrows
    public SneakyResultActions deleteMealHistory(Long mealHistoryId) {
        return new SneakyResultActions(
                mockMvc.perform(
                        MockMvcRequestBuilders.delete(MEAL_HISTORY_BY_ID_URL, mealHistoryId)
                )
        );
    }

}
