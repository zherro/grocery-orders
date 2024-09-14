package com.grocery.orders.usecases;

import com.grocery.orders.domain.OrderItem;
import com.grocery.orders.domain.OrderItemProduct;
import com.grocery.orders.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigInteger;
import static org.assertj.core.api.Assertions.assertThat;

class UpdateOrderItemWithProductUseCaseTest {

    private UpdateOrderItemWithProductUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new UpdateOrderItemWithProductUseCase();
    }

    @Test
    void shouldUpdateOrderItemWhenProductIsProvided() {
        // Arrange
        OrderItem orderItem = new OrderItem();
        orderItem.setQty(3);

        Product product = new Product();
        product.setName("Amazing Burger");
        product.setPrice(new BigInteger("999"));

        OrderItemProduct input = new OrderItemProduct(orderItem, product);

        // Act
        OrderItemProduct result = useCase.execute(input);

        // Assert
        assertThat(result.getOrderItem().getProductName()).isEqualTo("Amazing Burger");
        assertThat(result.getOrderItem().getPrice()).isEqualTo(new BigInteger("999"));
        assertThat(result.getOrderItem().getTotalPrice()).isEqualTo(new BigInteger("2997")); // 999 * 3
    }

    @Test
    void shouldNotUpdateOrderItemWhenProductIsNotProvided() {
        // Arrange
        OrderItem orderItem = new OrderItem();
        orderItem.setQty(3);

        OrderItemProduct input = new OrderItemProduct(orderItem, null);

        // Act
        OrderItemProduct result = useCase.execute(input);

        // Assert
        assertThat(result.getOrderItem().getProductName()).isNull();
        assertThat(result.getOrderItem().getPrice()).isEqualTo(BigInteger.ZERO);
        assertThat(result.getOrderItem().getTotalPrice()).isEqualTo(BigInteger.ZERO);
    }

    @Test
    void shouldHandleNullOrderItem() {
        // Arrange
        Product product = new Product();
        product.setName("Amazing Burger");
        product.setPrice(new BigInteger("999"));

        OrderItemProduct input = new OrderItemProduct(null, product);

        // Act
        OrderItemProduct result = useCase.execute(input);

        // Assert
        assertThat(result.getOrderItem()).isNull();
        assertThat(result.getProduct()).isEqualTo(product);
    }

    @Test
    void shouldHandleInvalidProductPrice() {
        // Arrange
        OrderItem orderItem = new OrderItem();
        orderItem.setQty(3);

        Product product = new Product();
        product.setName("Amazing Burger");
        product.setPrice(null);

        OrderItemProduct input = new OrderItemProduct(orderItem, product);

        // Act
        OrderItemProduct result = useCase.execute(input);

        // Assert
        assertThat(result.getOrderItem().getProductName()).isEqualTo("Amazing Burger");
        assertThat(result.getOrderItem().getPrice()).isEqualTo(BigInteger.ZERO);
        assertThat(result.getOrderItem().getTotalPrice()).isEqualTo(BigInteger.ZERO);
    }
}
