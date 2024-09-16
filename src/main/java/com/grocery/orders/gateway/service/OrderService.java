package com.grocery.orders.gateway.service;

import com.grocery.orders.config.exception.BusinessException;
import com.grocery.orders.domain.Order;
import com.grocery.orders.domain.enums.OrderStatus;
import com.grocery.orders.gateway.database.OrderItemRepository;
import com.grocery.orders.gateway.database.OrderRepository;
import com.grocery.orders.gateway.http.ProductApiHttpIntegration;
import com.grocery.orders.mapper.OrderMapper;
import com.grocery.orders.usecases.ApplyOrderItemPromotionsUseCase;
import com.grocery.orders.usecases.MergeDuplicateOrderItemsUseCase;
import com.grocery.orders.usecases.OrderProcessUseCase;
import com.grocery.orders.usecases.UpdateOrderItemWithProductUseCase;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductApiHttpIntegration productApiIntegration;

    private final UpdateOrderItemWithProductUseCase updateOrderItemWithProductUseCase;
    private final ApplyOrderItemPromotionsUseCase applyOrderItemPromotionsUseCase;
    private final OrderProcessUseCase orderProcessUseCase;
    private final MergeDuplicateOrderItemsUseCase mergeDuplicateOrderItemsUseCase;

    public Order createOrder(final Order order) {
        log.info("m=createOrder, saving order");
        return Optional.of(order)
                .map(mergeDuplicateOrderItemsUseCase::execute)
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

        return orderProcessUseCase.execute(order);
    }

    @Transactional
    public Order updateOrder(final String orderId, final Order orderUpdate) {
        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("Order not found"));

        orderItemRepository.deleteByOrderId(order.getId());

        return Optional.ofNullable(orderUpdate)
                .map(mergeDuplicateOrderItemsUseCase::execute)
                .map(orderMapper::dtoToEntity)
                .map(o -> {
                    order.setProducts(o.getProducts());
                    return order;
                })
                .map(orderRepository::save)
                .map(orderMapper::entityToDto)
                .get();
    }

    public Page<Order> searchOrders(String customerId, OrderStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        var results = orderRepository.findOrders(customerId, status, pageable);
        var orders = results.stream().map(orderMapper::entityToDto).toList();
        return new PageImpl<>(orders, results.getPageable(), results.getSize());
    }
}
