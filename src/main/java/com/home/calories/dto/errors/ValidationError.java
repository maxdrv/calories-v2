package com.home.calories.dto.errors;

import lombok.Data;

@Data
public class ValidationError {

    private String code;
    private String message;

}
