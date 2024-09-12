package com.grocery.orders.domain;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.List;

@Getter
@Setter
public class Product {

    private String id;
    private String name;
    private BigInteger price;
    private List<Promotion> promotions;
}
