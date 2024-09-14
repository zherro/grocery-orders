package com.grocery.orders.gateway.database;

import com.grocery.orders.gateway.database.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface OrderItemRepository extends CrudRepository<OrderItemEntity, String> {
    @Transactional
    @Modifying
    void deleteByOrderId(String orderId);
}
