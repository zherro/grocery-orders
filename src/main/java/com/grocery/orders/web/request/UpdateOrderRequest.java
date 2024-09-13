package com.grocery.orders.web.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateOrderRequest {
    private List<OrderItemRequest> products;
}
