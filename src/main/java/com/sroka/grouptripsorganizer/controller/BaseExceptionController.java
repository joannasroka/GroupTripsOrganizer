package com.sroka.grouptripsorganizer.controller;

import com.sroka.grouptripsorganizer.dto.exception.ExceptionDto;
import com.sroka.grouptripsorganizer.dto.exception.ObjectValidationResponse;
import com.sroka.grouptripsorganizer.exception.BadRequestException;
import com.sroka.grouptripsorganizer.exception.NotFoundException;
import com.sroka.grouptripsorganizer.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public abstract class BaseExceptionController {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ExceptionDto handleNotFoundException(Exception e) {
        return new ExceptionDto(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ExceptionDto handleBadRequestException(BadRequestException e) {
        return new ExceptionDto(e.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ExceptionDto handleAccessDeniedException(AccessDeniedException e) {
        return new ExceptionDto("error.accessDenied");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ExceptionDto handleAllException(Exception e) {
        log.error(e.getMessage());
        return new ExceptionDto("error.internalServerError");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ExceptionDto handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ObjectValidationResponse> fieldErrors = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError ->
                        new ObjectValidationResponse(fieldError.getObjectName(), fieldError.getField(), fieldError.getRejectedValue(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());

        return new ExceptionDto("error.failedValidation", fieldErrors);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public ExceptionDto handleValidationException(ValidationException e) {
        List<ObjectValidationResponse> fieldErrors = e.getErrors().getFieldErrors().stream()
                .map(fieldError ->
                        new ObjectValidationResponse(fieldError.getObjectName(), fieldError.getField(), fieldError.getRejectedValue(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());

        return new ExceptionDto("error.failedValidation", fieldErrors);
    }
}
