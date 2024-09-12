package com.grocery.orders.domain;

import com.grocery.orders.domain.enums.PromotionType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
public class Promotion {
    private String id;
    private PromotionType type;
    private BigInteger amount;
    private BigInteger price;
    private Long requiredQty;
    private Long freeQty;

}
