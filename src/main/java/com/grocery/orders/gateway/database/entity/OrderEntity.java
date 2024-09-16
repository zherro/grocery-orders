package com.grocery.orders.gateway.database.entity;

import com.grocery.orders.domain.enums.OrderStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private BigInteger orderTotalDiscount;
    private BigInteger orderTotalPrice;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItemEntity> products;

    private Date createdAt;
    private Date updateAt;

    @PrePersist
    private void onCreate() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
        status = OrderStatus.OPEN;
        Optional.ofNullable(products).orElse(new ArrayList<>())
                .forEach(p -> p.setOrder(this));
        createdAt = new Date();
    }

    @PreUpdate
    private void onUpdate() {
        Optional.ofNullable(products).orElse(new ArrayList<>())
                .forEach(p -> p.setOrder(this));
        updateAt = new Date();
    }
}
