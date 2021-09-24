package com.sroka.grouptripsorganizer.dto.exception;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ExceptionDto {

    private final GlobalException globalException;

    private final List<ObjectValidationResponse> validationErrors;

    public ExceptionDto(String message) {
        this.globalException = new GlobalException(message);
        this.validationErrors = new ArrayList<>();
    }

    public ExceptionDto(String message, List<ObjectValidationResponse> validationErrors) {
        this.globalException = new GlobalException(message);
        this.validationErrors = validationErrors;
    }
}
