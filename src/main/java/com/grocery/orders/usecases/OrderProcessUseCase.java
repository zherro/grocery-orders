package com.grocery.orders.usecases;

import com.grocery.orders.domain.Order;
import com.grocery.orders.domain.OrderItem;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Objects;

@Component
public class OrderProcessUseCase implements UseCase<Order, Order> {

    @Override
    public Order execute(Order order) {
        if(Objects.nonNull(order) && Objects.nonNull(order.getProducts())) {
            var totalOrderPrice = order.getProducts().stream()
                    .map(OrderItem::getTotalPrice)
                    .reduce(BigInteger.ZERO, BigInteger::add);
            var totalOrderDiscount = order.getProducts().stream()
                    .map(OrderItem::getDiscountPrice)
                    .reduce(BigInteger.ZERO, BigInteger::add);

            order.setOrderTotalPrice(totalOrderPrice);
            order.setOrderTotalDiscount(totalOrderDiscount);
        }
        return order;
    }
}
