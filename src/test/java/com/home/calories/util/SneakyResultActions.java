package com.home.calories.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.ResultMatcher;

public class SneakyResultActions implements ResultActions {

    private final static ObjectMapper MAPPER;
    private final ResultActions resultActions;

    static {
        MAPPER = new ObjectMapper();
        MAPPER.registerModule(new JavaTimeModule());
    }

    public SneakyResultActions(ResultActions resultActions) {
        this.resultActions = resultActions;
    }

    @SneakyThrows
    @Override
    public SneakyResultActions andExpect(ResultMatcher matcher) {
        return new SneakyResultActions(resultActions.andExpect(matcher));
    }

    @SneakyThrows
    @Override
    public SneakyResultActions andDo(ResultHandler handler) {
        return new SneakyResultActions(resultActions.andDo(handler));
    }

    @Override
    public MvcResult andReturn() {
        return resultActions.andReturn();
    }

    @SneakyThrows
    public String andReturnAsString() {
        return resultActions.andReturn().getResponse().getContentAsString();
    }

    @SneakyThrows
    public <R> R andReturnAs(Class<R> clazz) {
        return MAPPER.readValue(andReturnAsString(), clazz);
    }

}
