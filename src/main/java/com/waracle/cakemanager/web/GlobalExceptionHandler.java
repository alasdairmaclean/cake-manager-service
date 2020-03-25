package com.waracle.cakemanager.web;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    public static final String DUPLICATE_CAKE_ERROR_MESSAGE = "Cake with that title aleady exists";

    @ExceptionHandler
    public ResponseEntity<RestError> constraintViolationException(DataIntegrityViolationException e) {
        RestError error = RestError.builder()
                .message(DUPLICATE_CAKE_ERROR_MESSAGE)
                .build();
        return ResponseEntity.badRequest().body(error);
    }

}
