package com.grocery.orders.mapper;

import com.grocery.orders.domain.Order;
import com.grocery.orders.gateway.database.entity.OrderEntity;
import com.grocery.orders.web.request.CreateOrderRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    Order createRequestToDto(CreateOrderRequest createOrderRequest);

    Order entityToDto(OrderEntity orderEntity);

    OrderEntity dtoToEntity(Order order);
}
