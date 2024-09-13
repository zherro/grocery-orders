package com.grocery.orders.config.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessException extends RuntimeException {

    public BusinessException() {
        super();
    }

    public BusinessException(final String msg) {
        super(msg);
        message = msg;
    }

    private ErrorType reason;
    private String message;
}
