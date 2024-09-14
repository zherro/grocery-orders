package com.grocery.orders.web.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderItemRequest {

    @NotBlank(message = "Product ID is required")
    private String productId;

    @Positive(message = "Quantity must be greater than zero")
    private long qty;
}
