package com.grocery.orders.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderItemProduct {
    private OrderItem orderItem;
    private Product product;
}
