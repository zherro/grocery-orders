package com.grocery.orders.usecases.promotions;

import com.grocery.orders.domain.OrderItem;
import com.grocery.orders.domain.Promotion;
import com.grocery.orders.domain.enums.PromotionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;

class ApplyBuyXGetYFreePromotionTest {

    private ApplyBuyXGetYFreePromotion applyBuyXGetYFreePromotion;

    @BeforeEach
    void setUp() {
        applyBuyXGetYFreePromotion = new ApplyBuyXGetYFreePromotion();
    }

    @Test
    void shouldApplyPromotionWhenQtyMeetsRequirement() {
        // Arrange
        OrderItem orderItem = new OrderItem();
        orderItem.setQty(5);
        orderItem.setPrice(new BigInteger("1000"));
        orderItem.setTotalPrice(new BigInteger("5000"));

        Promotion promotion = new Promotion();
        promotion.setType(PromotionType.BUY_X_GET_Y_FREE);
        promotion.setRequiredQty(2L);
        promotion.setFreeQty(1L);

        // Act
        applyBuyXGetYFreePromotion.execute(orderItem, promotion);

        // Assert
        BigInteger expectedTotalPrice = new BigInteger("4000"); // 1 item free (1000 discount)
        assertThat(orderItem.getTotalPrice()).isEqualTo(expectedTotalPrice);
    }

    @Test
    void shouldApplyPromotionWhenQtyMeetsRequirementIsMin() {
        // Arrange
        OrderItem orderItem = new OrderItem();
        orderItem.setQty(3);
        orderItem.setPrice(new BigInteger("1000"));
        orderItem.setTotalPrice(new BigInteger("3000"));

        Promotion promotion = new Promotion();
        promotion.setType(PromotionType.BUY_X_GET_Y_FREE);
        promotion.setRequiredQty(2L);
        promotion.setFreeQty(1L);

        // Act
        applyBuyXGetYFreePromotion.execute(orderItem, promotion);

        // Assert
        BigInteger expectedTotalPrice = new BigInteger("2000"); // 1 item free (1000 discount)
        assertThat(orderItem.getTotalPrice()).isEqualTo(expectedTotalPrice);
    }

    @Test
    void shouldNotApplyPromotionWhenQtyIsLessThanRequired() {
        // Arrange
        OrderItem orderItem = new OrderItem();
        orderItem.setQty(1);
        orderItem.setPrice(new BigInteger("1000"));
        orderItem.setTotalPrice(new BigInteger("1000"));

        Promotion promotion = new Promotion();
        promotion.setType(PromotionType.BUY_X_GET_Y_FREE);
        promotion.setRequiredQty(2L);
        promotion.setFreeQty(1L);

        // Act
        applyBuyXGetYFreePromotion.execute(orderItem, promotion);

        // Assert
        BigInteger expectedTotalPrice = new BigInteger("1000"); // No discount
        assertThat(orderItem.getTotalPrice()).isEqualTo(expectedTotalPrice);
    }

    @Test
    void shouldNotApplyPromotionWhenPromotionTypeIsDifferent() {
        // Arrange
        OrderItem orderItem = new OrderItem();
        orderItem.setQty(5);
        orderItem.setPrice(new BigInteger("1000"));
        orderItem.setTotalPrice(new BigInteger("5000"));

        Promotion promotion = new Promotion();
        promotion.setType(PromotionType.FLAT_PERCENT); // Different promotion type
        promotion.setRequiredQty(2L);
        promotion.setFreeQty(1L);

        // Act
        applyBuyXGetYFreePromotion.execute(orderItem, promotion);

        // Assert
        BigInteger expectedTotalPrice = new BigInteger("5000"); // No discount
        assertThat(orderItem.getTotalPrice()).isEqualTo(expectedTotalPrice);
    }

    @Test
    void shouldNotApplyPromotionWhenItemPriceIsNull() {
        // Arrange
        OrderItem orderItem = new OrderItem();
        orderItem.setQty(5);
        orderItem.setPrice(null);
        orderItem.setTotalPrice(new BigInteger("5000"));

        Promotion promotion = new Promotion();
        promotion.setType(PromotionType.BUY_X_GET_Y_FREE);
        promotion.setRequiredQty(2L);
        promotion.setFreeQty(1L);

        // Act
        applyBuyXGetYFreePromotion.execute(orderItem, promotion);

        // Assert
        assertThat(orderItem.getTotalPrice()).isEqualTo(new BigInteger("5000"));
    }

    @Test
    void shouldNotApplyPromotionWhenPromotionIsNull() {
        // Arrange
        OrderItem orderItem = new OrderItem();
        orderItem.setQty(5);
        orderItem.setPrice(new BigInteger("1000"));
        orderItem.setTotalPrice(new BigInteger("5000"));

        // Act
        applyBuyXGetYFreePromotion.execute(orderItem, null);

        // Assert
        // Verifique se o preço total do pedido permanece inalterado
        assertThat(orderItem.getTotalPrice()).isEqualTo(new BigInteger("5000"));
    }


}
