package com.grocery.orders.domain;

import com.grocery.orders.domain.enums.ProductStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.List;

@Getter
@Setter
public class OrderItem {

    private String id;
    private String productId;
    private String productName;
    private BigInteger price;
    private int qtd;
    private BigInteger discountPrice;

    private List<OrderItemAppliedPromotion> appliedPromotions;
    private BigInteger totalPrice;

    private ProductStatus productStatus;
}
