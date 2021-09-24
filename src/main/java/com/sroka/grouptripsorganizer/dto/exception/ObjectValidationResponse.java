package com.sroka.grouptripsorganizer.dto.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ObjectValidationResponse {
    private String object;

    private String field;

    private Object invalidValue;

    private String message;
}
