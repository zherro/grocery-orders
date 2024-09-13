package com.grocery.orders.gateway.database.entity;

import com.grocery.orders.domain.enums.OrderStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

@Entity(name = "tb_order")
@Getter
@Setter
public class OrderEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private String id;
    private String customerId;
    private String customerName;
    private OrderStatus status;
    private BigInteger orderTotalDiscount;
    private BigInteger orderTotalPrice;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItemEntity> products;

    @PrePersist
    public void generateId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
        status = OrderStatus.OPEN;
    }
}
