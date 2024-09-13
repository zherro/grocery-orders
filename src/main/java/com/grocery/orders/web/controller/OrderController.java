package com.grocery.orders.web.controller;

import com.grocery.orders.domain.Order;
import com.grocery.orders.gateway.service.OrderService;
import com.grocery.orders.mapper.OrderMapper;
import com.grocery.orders.web.controller.doc.OrderControllerDoc;
import com.grocery.orders.web.request.CreateOrderRequest;
import com.grocery.orders.web.request.UpdateOrderRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController implements OrderControllerDoc {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @Override
    public Order createOrder(CreateOrderRequest createOrderRequest) {
        log.info("m=createOrder, creating new order");
        return Optional.of(createOrderRequest)
                .map(orderMapper::createRequestToDto)
                .map(orderService::createOrder)
                .get();
    }

    @Override
    public Order findOrderById(String orderId) {
        log.info("m=findOrderById, finding order by ID: {}", orderId);
        return orderService.findOrderById(orderId);
    }

    @Override
    public Order updateOrder(String orderId, UpdateOrderRequest updateOrderRequest) {
        return orderService.updateOrder(
                orderId, Optional.of(updateOrderRequest).map(orderMapper::updateRequestToDto).get());
    }
}