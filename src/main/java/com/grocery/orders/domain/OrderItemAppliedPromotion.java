package com.grocery.orders.domain;

import com.grocery.orders.domain.enums.PromotionType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
public class OrderItemAppliedPromotion {
    private PromotionType promotionType;
    private BigInteger totalDiscount;
    private boolean enabled;
}
