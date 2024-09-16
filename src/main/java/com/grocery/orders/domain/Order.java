package com.grocery.orders.domain;

import com.grocery.orders.domain.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    private Date createdAt;
    private Date updateAt;

    public boolean shouldEnrichProducts() {
        return !OrderStatus.CLOSED.equals(this.getStatus()) && Objects.nonNull(this.getProducts());
    }

    public BigInteger getOrderTotalDiscount() {
        return Optional.ofNullable(orderTotalDiscount).orElse(BigInteger.ZERO);
    }

    public BigInteger getOrderTotalPrice() {
        return Optional.ofNullable(orderTotalPrice).orElse(BigInteger.ZERO);
    }
}
