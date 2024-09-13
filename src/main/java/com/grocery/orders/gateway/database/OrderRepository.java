package com.grocery.orders.gateway.database;

import com.grocery.orders.gateway.database.entity.OrderEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<OrderEntity, String> {
}
