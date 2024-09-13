package com.grocery.orders.web.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemRequest {

    @NotBlank(message = "Product ID is required")
    private String productId;

    @Positive(message = "Quantity must be greater than zero")
    private long qty;
}
