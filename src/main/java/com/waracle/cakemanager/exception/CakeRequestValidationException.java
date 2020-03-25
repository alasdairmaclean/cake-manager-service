package com.waracle.cakemanager.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.FieldError;

import java.util.List;

@Builder(toBuilder = true)
@Getter
public class CakeRequestValidationException extends RuntimeException {

    private List<FieldError> errors;

    public CakeRequestValidationException(List<FieldError> errors) {
        super(errors.toString());
        this.errors = errors;
    }
}
