package com.grocery.orders.domain;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class Product {

    private String id;
    private String name;
    private BigInteger price;
    private List<Promotion> promotions;

    public BigInteger getPrice() {
        return Optional.ofNullable(price).orElse(BigInteger.ZERO);
    }
}
