package com.waracle.cakemanager.web;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
@JsonDeserialize(builder = RestError.RestErrorBuilder.class)
public class RestError {

    private String message;

    @JsonPOJOBuilder(withPrefix = "")
    public static class RestErrorBuilder {

    }

}