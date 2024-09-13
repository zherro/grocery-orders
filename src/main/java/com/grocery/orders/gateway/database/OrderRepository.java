package com.grocery.orders.gateway.database;

import com.grocery.orders.gateway.database.entity.OrderEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends CrudRepository<OrderEntity, String> {
    @EntityGraph(attributePaths = "products")
    Optional<OrderEntity> findById(String id);
}
