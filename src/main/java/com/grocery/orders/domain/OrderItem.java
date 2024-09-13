package com.grocery.orders.domain;

import com.grocery.orders.domain.enums.ProductStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class OrderItem {

    private String id;
    private String productId;
    private String productName;
    private BigInteger price;
    private long qty;
    private BigInteger discountPrice;

    private List<OrderItemAppliedPromotion> appliedPromotions;
    private BigInteger totalPrice;

    private ProductStatus productStatus;

    public BigInteger getPrice() {
        return Optional.ofNullable(price).orElse(BigInteger.ZERO);
    }

    public BigInteger getDiscountPrice() {
        return Optional.ofNullable(discountPrice).orElse(BigInteger.ZERO);
    }

    public BigInteger getTotalPrice() {
        return Optional.ofNullable(totalPrice).orElse(BigInteger.ZERO);
    }
}
