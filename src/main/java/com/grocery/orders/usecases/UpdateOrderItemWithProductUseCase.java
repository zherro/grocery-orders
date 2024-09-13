package com.grocery.orders.usecases;

import com.grocery.orders.domain.OrderItemProduct;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UpdateOrderItemWithProductUseCase implements UseCase<OrderItemProduct, OrderItemProduct> {

    @Override
    public OrderItemProduct execute(OrderItemProduct input) {
        var orderItem = input.getOrderItem();

        if(Objects.nonNull(input.getProduct())) {
            orderItem.setProductName(input.getProduct().getName());
            orderItem.setPrice(input.getProduct().getPrice());
        }
        return new OrderItemProduct(orderItem, input.getProduct());
    }
}
