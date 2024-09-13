package com.grocery.orders.config.exception;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;


@Getter
@Builder
public class ErrorResponse {
    private ErrorType type;
    private String message;
    private Map<String, String> fields;
}
