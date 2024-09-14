package com.grocery.orders.usecases;

import com.grocery.orders.domain.Order;
import com.grocery.orders.domain.OrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderProcessUseCaseTest {

    @InjectMocks
    private OrderProcessUseCase orderProcessUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCalculateTotalPriceAndDiscountCorrectly() {
        // Given
        Order order = new Order();
        OrderItem item1 = new OrderItem();
        item1.setTotalPrice(new BigInteger("1000"));
        item1.setDiscountPrice(new BigInteger("200"));

        OrderItem item2 = new OrderItem();
        item2.setTotalPrice(new BigInteger("2000"));
        item2.setDiscountPrice(new BigInteger("300"));

        order.setProducts(Arrays.asList(item1, item2));

        // When
        Order processedOrder = orderProcessUseCase.execute(order);

        // Assert
        assertEquals(new BigInteger("3000"), processedOrder.getOrderTotalPrice());
        assertEquals(new BigInteger("500"), processedOrder.getOrderTotalDiscount());
    }

    @Test
    void shouldHandleEmptyOrder() {
        // Given
        Order order = new Order();
        order.setProducts(Collections.emptyList());

        // When
        Order processedOrder = orderProcessUseCase.execute(order);

        // Assert
        assertEquals(BigInteger.ZERO, processedOrder.getOrderTotalPrice());
        assertEquals(BigInteger.ZERO, processedOrder.getOrderTotalDiscount());
    }

    @Test
    void shouldHandleNullOrder() {
        // Given & When & AssertN
        assertDoesNotThrow(() -> orderProcessUseCase.execute(null));
    }

    @Test
    void shouldHandleOrderWithNullProductsList() {
        // Given
        Order order = new Order();
        order.setProducts(null);

        // When
        Order processedOrder = orderProcessUseCase.execute(order);

        // Assert
        assertEquals(BigInteger.ZERO, processedOrder.getOrderTotalPrice());
        assertEquals(BigInteger.ZERO, processedOrder.getOrderTotalDiscount());
    }

    @Test
    void shouldHandleOrderItemWithNullPrices() {
        // Given
        Order order = new Order();
        OrderItem item1 = new OrderItem();
        item1.setTotalPrice(null);
        item1.setDiscountPrice(null);

        order.setProducts(Collections.singletonList(item1));

        // When
        Order processedOrder = orderProcessUseCase.execute(order);

        // Assert
        assertEquals(BigInteger.ZERO, processedOrder.getOrderTotalPrice());
        assertEquals(BigInteger.ZERO, processedOrder.getOrderTotalDiscount());
    }
}
