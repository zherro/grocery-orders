package com.grocery.orders.gateway.database;

import com.grocery.orders.domain.enums.OrderStatus;
import com.grocery.orders.gateway.database.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends CrudRepository<OrderEntity, String> {
    @EntityGraph(attributePaths = "products")
    Optional<OrderEntity> findById(String id);

    @Query("SELECT o FROM tb_order o WHERE (:customerId IS NULL OR o.customerId = :customerId) " +
            "AND (:status IS NULL OR o.status = :status)")
    Page<OrderEntity> findOrders(@Param("customerId") String customerId,
                                 @Param("status") OrderStatus status,
                                 Pageable pageable);
}
