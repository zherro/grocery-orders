package com.grocery.orders.gateway.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.UUID;

@Entity(name = "tb_order_item")
@Getter
@Setter
public class OrderItemEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private String id;
    private String productId;
    private BigInteger price;
    private BigInteger discountPrice;
    private BigInteger totalPrice;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    // private List<OrderItemAppliedPromotion> appliedPromotions;

    @PrePersist
    public void generateId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
