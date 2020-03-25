package com.waracle.cakemanager.web;

import com.waracle.cakemanager.exception.CakeRequestValidationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    public static final String DUPLICATE_CAKE_ERROR_MESSAGE = "Cake with that title aleady exists";

    @ExceptionHandler
    public ResponseEntity<List<RestError>> constraintViolationException(DataIntegrityViolationException e) {
        RestError error = RestError.builder()
                .message(DUPLICATE_CAKE_ERROR_MESSAGE)
                .build();
        return ResponseEntity.badRequest().body(List.of(error));
    }

    @ExceptionHandler
    public ResponseEntity<List<RestError>> constraintViolationException(CakeRequestValidationException e) {
        List<RestError> errors = e.getErrors().stream()
                .map(this::toRestError)
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(errors);
    }

    private RestError toRestError(FieldError fieldError) {
        return RestError.builder()
                .field(fieldError.getField())
                .message(fieldError.getDefaultMessage())
                .build();
    }

}
