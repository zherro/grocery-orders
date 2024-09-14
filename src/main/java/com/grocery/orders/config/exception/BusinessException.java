package com.grocery.orders.config.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    public BusinessException(final String msg) {
        super(msg);
        message = msg;
    }

    private final String message;
}
