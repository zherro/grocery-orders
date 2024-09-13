package com.grocery.orders.domain;

import com.grocery.orders.domain.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.List;

@Getter
@Setter
public class Order {

    private String id;
    private String customerId;
    private String customerName;
    private OrderStatus status;
    private BigInteger orderTotalDiscount;
    private BigInteger orderTotalPrice;
    private List<OrderItem> products;
}
