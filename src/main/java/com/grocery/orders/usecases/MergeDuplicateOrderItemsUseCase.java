package com.grocery.orders.usecases;

import com.grocery.orders.domain.Order;
import com.grocery.orders.domain.OrderItem;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class MergeDuplicateOrderItemsUseCase implements UseCase<Order, Order> {

    @Override
    public Order execute(final Order order) {
        if (Objects.isNull(order)) {
            return null;
        }

        if(Objects.isNull(order.getProducts()) || order.getProducts().isEmpty()) {
            order.setProducts(new ArrayList<>());
            return order;
        }

        order.getProducts().removeIf(item -> item.getQty() <= 0);

        Map<String, List<OrderItem>> groupedByProductId = order.getProducts().stream()
                .collect(Collectors.groupingBy(OrderItem::getProductId));

        List<OrderItem> mergedItems = new ArrayList<>();

        for (Map.Entry<String, List<OrderItem>> entry : groupedByProductId.entrySet()) {
            List<OrderItem> itemsWithSameProductId = entry.getValue();

            if (itemsWithSameProductId.size() > 1) {
                OrderItem mergedItem = mergeItems(itemsWithSameProductId);
                mergedItems.add(mergedItem);
            } else {
                mergedItems.addAll(itemsWithSameProductId);
            }
        }

        order.setProducts(mergedItems);
        return order;
    }

    private OrderItem mergeItems(List<OrderItem> items) {
        OrderItem item = items.get(0);

        BigInteger totalPrice = items.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigInteger.ZERO, BigInteger::add);

        long totalQty = items.stream()
                .mapToLong(OrderItem::getQty)
                .sum();

        item.setTotalPrice(totalPrice);
        item.setQty(totalQty);
        return item;
    }
}
