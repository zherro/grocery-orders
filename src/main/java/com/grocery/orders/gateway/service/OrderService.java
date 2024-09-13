package com.grocery.orders.gateway.service;

import com.grocery.orders.config.exception.BusinessException;
import com.grocery.orders.domain.Order;
import com.grocery.orders.domain.OrderItemProduct;
import com.grocery.orders.gateway.database.OrderRepository;
import com.grocery.orders.gateway.http.ProductApiHttpIntegration;
import com.grocery.orders.mapper.OrderMapper;
import com.grocery.orders.usecases.ApplyOrderItemPromotionsUseCase;
import com.grocery.orders.usecases.UpdateOrderItemWithProductUseCase;
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

    private final UpdateOrderItemWithProductUseCase updateOrderItemWithProductUseCase;
    private final ApplyOrderItemPromotionsUseCase applyOrderItemPromotionsUseCase;

    public Order createOrder(final Order order) {
        log.info("m=createOrder, saving order");
        return Optional.of(order)
                .map(orderMapper::dtoToEntity)
                .map(orderRepository::save)
                .map(orderMapper::entityToDto)
                .orElseThrow(() -> new BusinessException("Unexpected error on create new order"));
    }

    public Order findOrderById(final String orderId) {
        var order = orderRepository.findById(orderId)
                .map(orderMapper::entityToDto)
                .orElseThrow(() -> new BusinessException("Order not found"));

        if(order.shouldEnrichProducts()) {
            var products = order.getProducts().stream()
                    .map(productApiIntegration::enrichOrderProducts)
                    .map(updateOrderItemWithProductUseCase::execute)
                    .map(applyOrderItemPromotionsUseCase::execute)
                    .toList();
            order.setProducts(products);
        }

        return order;
    }

    public Order updateOrder(final String orderId, final Order orderUpdate) {
        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("Order not found"));

        return Optional.ofNullable(orderUpdate)
                .map(orderMapper::dtoToEntity)
                .map(o -> {
                    order.setProducts(o.getProducts());
                    return order;
                })
                .map(orderRepository::save)
                .map(orderMapper::entityToDto)
                .get();
    }
}
