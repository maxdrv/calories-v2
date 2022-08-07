package com.home.calories.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.calories.openapi.model.CreateBaseProductRequest;
import com.home.calories.openapi.model.UpdateBaseProductRequest;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@Component
public class ApiCaller {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    public ApiCaller(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
    }

    @SneakyThrows
    public SneakyResultActions page(String params) {
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

}