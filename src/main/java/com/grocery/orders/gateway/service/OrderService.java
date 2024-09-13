package com.grocery.orders.gateway.service;

import com.grocery.orders.config.exception.BusinessException;
import com.grocery.orders.domain.Order;
import com.grocery.orders.domain.OrderItemProduct;
import com.grocery.orders.gateway.database.OrderRepository;
import com.grocery.orders.gateway.http.ProductApiHttpIntegration;
import com.grocery.orders.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ProductApiHttpIntegration productApiIntegration;

    public Order createOrder(final Order order) {
        log.info("m=createOrder, saving order");
        return Optional.of(order)
                .map(orderMapper::dtoToEntity)
                .map(orderRepository::save)
                .map(orderMapper::entityToDto)
                .orElseThrow(() -> new BusinessException("Unexpected error on create new order"));
    }

    public Order findOrderById(final String uuid) {
        var order = orderRepository.findById(uuid)
                .map(orderMapper::entityToDto)
                .orElseThrow(() -> new BusinessException("Order not found"));

        if(order.shouldEnrichProducts()) {
            var products = order.getProducts().stream()
                    .map(productApiIntegration::enrichOrderProducts)
                    .map(OrderItemProduct::getOrderItem)
                    .toList();
            order.setProducts(products);
        }

        return order;
    }

}
