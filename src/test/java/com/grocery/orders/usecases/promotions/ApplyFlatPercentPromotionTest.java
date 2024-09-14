package com.grocery.orders.usecases.promotions;

import com.grocery.orders.domain.OrderItem;
import com.grocery.orders.domain.Promotion;
import com.grocery.orders.domain.enums.PromotionType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.math.BigInteger;
import static org.assertj.core.api.Assertions.assertThat;

public class ApplyFlatPercentPromotionTest {

    private final ApplyFlatPercentPromotion applyFlatPercentPromotion = new ApplyFlatPercentPromotion();

    @Test
    void shouldApplyFlatPercentPromotionCorrectly() {
        // Given
        OrderItem orderItem = new OrderItem();
        orderItem.setQty(2);
        orderItem.setPrice(new BigInteger("1000"));
        orderItem.setTotalPrice(new BigInteger("2000"));
        orderItem.setDiscountPrice(BigInteger.ZERO);

        Promotion promotion = new Promotion();
        promotion.setType(PromotionType.FLAT_PERCENT);
        promotion.setAmount(BigInteger.TEN);

        // When
        applyFlatPercentPromotion.execute(orderItem, promotion);

        // Assert
        BigInteger expectedDiscount = new BigInteger("200");
        BigInteger expectedTotalPrice = new BigInteger("1800");
        assertThat(orderItem.getTotalPrice()).isEqualTo(expectedTotalPrice);
        assertThat(orderItem.getDiscountPrice()).isEqualTo(expectedDiscount);
    }

    @Test
    void shouldHandleZeroPercentDiscount() {
        // Given
        OrderItem orderItem = new OrderItem();
        orderItem.setQty(3);
        orderItem.setPrice(new BigInteger("1500"));
        orderItem.setTotalPrice(new BigInteger("4500"));
        orderItem.setDiscountPrice(BigInteger.ZERO);

        Promotion promotion = new Promotion();
        promotion.setType(PromotionType.FLAT_PERCENT);
        promotion.setAmount(BigInteger.ZERO);

        // When
        applyFlatPercentPromotion.execute(orderItem, promotion);

        // Assert
        assertThat(orderItem.getTotalPrice()).isEqualTo(new BigInteger("4500"));
        assertThat(orderItem.getDiscountPrice()).isEqualTo(BigInteger.ZERO);
    }

    @Test
    void shouldHandleNullOrderItem() {
        // Given
        Promotion promotion = new Promotion();
        promotion.setType(PromotionType.FLAT_PERCENT);
        promotion.setAmount(new BigInteger("15"));

        // Assert
        Assertions.assertDoesNotThrow(() ->  applyFlatPercentPromotion.execute(null, promotion));
    }

    @Test
    void shouldHandleNullPromotion() {
        // Given
        OrderItem orderItem = new OrderItem();
        orderItem.setQty(1);
        orderItem.setPrice(new BigInteger("500"));
        orderItem.setTotalPrice(new BigInteger("500"));
        orderItem.setDiscountPrice(BigInteger.ZERO);

        // When
        applyFlatPercentPromotion.execute(orderItem, null);

        // Assert
        assertThat(orderItem.getTotalPrice()).isEqualTo(new BigInteger("500"));
        assertThat(orderItem.getDiscountPrice()).isEqualTo(BigInteger.ZERO);
    }

    @Test
    void shouldHandleInvalidPromotionType() {
        // Given
        OrderItem orderItem = new OrderItem();
        orderItem.setQty(2);
        orderItem.setPrice(new BigInteger("400"));
        orderItem.setTotalPrice(new BigInteger("800"));
        orderItem.setDiscountPrice(BigInteger.ZERO);

        Promotion promotion = new Promotion();
        promotion.setType(PromotionType.BUY_X_GET_Y_FREE);
        promotion.setAmount(new BigInteger("20"));

        // When
        applyFlatPercentPromotion.execute(orderItem, promotion);

        // Assert
        assertThat(orderItem.getTotalPrice()).isEqualTo(new BigInteger("800"));
        assertThat(orderItem.getDiscountPrice()).isEqualTo(BigInteger.ZERO);
    }
}
