package com.grocery.orders.usecases.promotions;

import com.grocery.orders.domain.OrderItem;
import com.grocery.orders.domain.Promotion;
import com.grocery.orders.domain.enums.PromotionType;
import org.junit.jupiter.api.Test;
import java.math.BigInteger;
import static org.assertj.core.api.Assertions.assertThat;

class ApplyQtyBasedPriceOverridePromotionTest {

    private final ApplyQtyBasedPriceOverridePromotion applyQtyBasedPriceOverridePromotion = new ApplyQtyBasedPriceOverridePromotion();

    @Test
    void shouldApplyQtyBasedPriceOverridePromotionWhenQtyMeetsRequirement() {
        // Given
        OrderItem orderItem = new OrderItem();
        orderItem.setQty(4);
        orderItem.setPrice(new BigInteger("1000"));
        orderItem.setTotalPrice(new BigInteger("4000"));
        orderItem.setDiscountPrice(BigInteger.ZERO);

        Promotion promotion = new Promotion();
        promotion.setType(PromotionType.QTY_BASED_PRICE_OVERRIDE);
        promotion.setRequiredQty(2L);
        promotion.setPrice(new BigInteger("1799"));

        // When
        applyQtyBasedPriceOverridePromotion.execute(orderItem, promotion);

        // Assert
        BigInteger expectedTotalPrice = new BigInteger("3799");
        BigInteger expectedTotalDiscount = new BigInteger("201");
        assertThat(orderItem.getTotalPrice()).isEqualTo(expectedTotalPrice);
        assertThat(orderItem.getDiscountPrice()).isEqualTo(expectedTotalDiscount);
    }

    @Test
    void shouldNotApplyPromotionWhenQtyBelowRequirement() {
        // Given
        OrderItem orderItem = new OrderItem();
        orderItem.setQty(1);
        orderItem.setPrice(new BigInteger("1000"));
        orderItem.setTotalPrice(new BigInteger("1000"));
        orderItem.setDiscountPrice(BigInteger.ZERO);

        Promotion promotion = new Promotion();
        promotion.setType(PromotionType.QTY_BASED_PRICE_OVERRIDE);
        promotion.setRequiredQty(2L);
        promotion.setPrice(new BigInteger("1799"));

        // When
        applyQtyBasedPriceOverridePromotion.execute(orderItem, promotion);

        // Assert
        assertThat(orderItem.getTotalPrice()).isEqualTo(new BigInteger("1000"));
        assertThat(orderItem.getDiscountPrice()).isEqualTo(BigInteger.ZERO);
    }

    @Test
    void shouldHandleNullValues() {
        // Given
        OrderItem orderItem = new OrderItem();
        orderItem.setQty(0);
        orderItem.setPrice(BigInteger.ZERO);
        orderItem.setTotalPrice(BigInteger.ZERO);
        orderItem.setDiscountPrice(BigInteger.ZERO);

        Promotion promotion = new Promotion();
        promotion.setType(PromotionType.QTY_BASED_PRICE_OVERRIDE);
        promotion.setRequiredQty(2L);
        promotion.setPrice(BigInteger.ZERO);

        // When
        applyQtyBasedPriceOverridePromotion.execute(orderItem, promotion);

        // Assert
        assertThat(orderItem.getTotalPrice()).isEqualTo(BigInteger.ZERO);
        assertThat(orderItem.getDiscountPrice()).isEqualTo(BigInteger.ZERO);
    }

    @Test
    void shouldCalculateDiscountCorrectlyWhenPromotionIsApplied() {
        // Given
        OrderItem orderItem = new OrderItem();
        orderItem.setQty(6);
        orderItem.setPrice(new BigInteger("1000"));
        orderItem.setTotalPrice(new BigInteger("6000"));
        orderItem.setDiscountPrice(BigInteger.ZERO);

        Promotion promotion = new Promotion();
        promotion.setType(PromotionType.QTY_BASED_PRICE_OVERRIDE);
        promotion.setRequiredQty(2L);
        promotion.setPrice(new BigInteger("1800"));

        // When
        applyQtyBasedPriceOverridePromotion.execute(orderItem, promotion);

        // Assert
        BigInteger expectedTotalPrice = new BigInteger("5800");
        BigInteger expectedTotalDiscount = new BigInteger("200");
        assertThat(orderItem.getTotalPrice()).isEqualTo(expectedTotalPrice);
        assertThat(orderItem.getDiscountPrice()).isEqualTo(expectedTotalDiscount);
    }
}
