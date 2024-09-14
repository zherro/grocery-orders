package com.grocery.orders.usecases;

import com.grocery.orders.domain.Order;
import com.grocery.orders.domain.OrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MergeDuplicateOrderItemsUseCaseTest {

    private MergeDuplicateOrderItemsUseCase mergeDuplicateOrderItemsUseCase;

    @BeforeEach
    void setUp() {
        mergeDuplicateOrderItemsUseCase = new MergeDuplicateOrderItemsUseCase();
    }

    @Test
    void shouldMergeDuplicateItemsCorrectly() {
        // Given
        Order order = new Order();
        List<OrderItem> items = new ArrayList<>();

        OrderItem item1 = new OrderItem();
        item1.setProductId("1");
        item1.setQty(2);
        item1.setTotalPrice(new BigInteger("200"));

        OrderItem item2 = new OrderItem();
        item2.setProductId("1");
        item2.setQty(3);
        item2.setTotalPrice(new BigInteger("300"));

        items.add(item1);
        items.add(item2);
        order.setProducts(items);

        // When
        mergeDuplicateOrderItemsUseCase.execute(order);

        // Assert
        assertThat(order.getProducts()).hasSize(1);
        assertThat(order.getProducts().get(0).getQty()).isEqualTo(5);
        assertThat(order.getProducts().get(0).getTotalPrice()).isEqualTo(new BigInteger("500"));
    }

    @Test
    void shouldNotMergeWhenThereAreNoDuplicates() {
        // Given
        Order order = new Order();
        List<OrderItem> items = new ArrayList<>();

        OrderItem item1 = new OrderItem();
        item1.setProductId("1");
        item1.setQty(2);
        item1.setTotalPrice(new BigInteger("200"));

        OrderItem item2 = new OrderItem();
        item2.setProductId("2");
        item2.setQty(3);
        item2.setTotalPrice(new BigInteger("300"));

        items.add(item1);
        items.add(item2);
        order.setProducts(items);

        // When
        mergeDuplicateOrderItemsUseCase.execute(order);

        // Assert
        assertThat(order.getProducts()).hasSize(2);
        assertThat(order.getProducts().get(0).getQty()).isEqualTo(2);
        assertThat(order.getProducts().get(1).getQty()).isEqualTo(3);
    }

    @Test
    void shouldReturnEmptyProductListWhenOrderHasNoItems() {
        // Given
        Order order = new Order();
        order.setProducts(new ArrayList<>());

        // When
        mergeDuplicateOrderItemsUseCase.execute(order);

        // Assert
        assertThat(order.getProducts()).isEmpty();
    }

    @Test
    void shouldReturnNullWhenOrderIsNull() {
        // When
        Order result = mergeDuplicateOrderItemsUseCase.execute(null);

        // Assert
        assertThat(result).isNull();
    }

    @Test
    void shouldRemoveItemsWithQtyLessThanOrEqualToZero() {
        // Given
        Order order = new Order();
        List<OrderItem> items = new ArrayList<>();

        OrderItem item1 = new OrderItem();
        item1.setProductId("1");
        item1.setQty(0);
        item1.setTotalPrice(new BigInteger("0"));

        OrderItem item2 = new OrderItem();
        item2.setProductId("2");
        item2.setQty(-1);
        item2.setTotalPrice(new BigInteger("100"));

        items.add(item1);
        items.add(item2);
        order.setProducts(items);

        // When
        mergeDuplicateOrderItemsUseCase.execute(order);

        // Assert
        assertThat(order.getProducts()).isEmpty();
    }

    @Test
    void shouldHandleMixedValidAndInvalidItems() {
        // Given
        Order order = new Order();
        List<OrderItem> items = new ArrayList<>();

        OrderItem validItem1 = new OrderItem();
        validItem1.setProductId("1");
        validItem1.setQty(2);
        validItem1.setTotalPrice(new BigInteger("200"));

        OrderItem invalidItem = new OrderItem();
        invalidItem.setProductId("2");
        invalidItem.setQty(-1);
        invalidItem.setTotalPrice(new BigInteger("100"));

        OrderItem validItem2 = new OrderItem();
        validItem2.setProductId("1");
        validItem2.setQty(3);
        validItem2.setTotalPrice(new BigInteger("300"));

        items.add(validItem1);
        items.add(invalidItem);
        items.add(validItem2);
        order.setProducts(items);

        // When
        mergeDuplicateOrderItemsUseCase.execute(order);

        // Assert
        assertThat(order.getProducts()).hasSize(1);
        assertThat(order.getProducts().get(0).getQty()).isEqualTo(5);
        assertThat(order.getProducts().get(0).getTotalPrice()).isEqualTo(new BigInteger("500"));
    }
}
