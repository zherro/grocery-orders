package com.grocery.orders.domain;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.List;

@Getter
@Setter
public class OrderItem {

    private Long id;
    private String productId;
    private BigInteger price;
    private BigInteger discountPrice;

    private List<OrderItemAppliedPromotion> appliedPromotions;
    private BigInteger totalPrice;
}
