package com.grocery.orders.usecases.promotions;

import com.grocery.orders.domain.OrderItem;
import com.grocery.orders.domain.Promotion;
import com.grocery.orders.domain.enums.PromotionType;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Objects;

@Component
public class ApplyFlatPercentPromotion {

    public void execute(final OrderItem orderItem, Promotion promotion) {
        if (Objects.nonNull(promotion) && Objects.nonNull(orderItem)
                && PromotionType.FLAT_PERCENT.equals(promotion.getType())) {
            BigInteger discountAmount = calculateDiscountAmount(orderItem.getTotalPrice(), promotion.getAmount());

            BigInteger newTotalPrice = orderItem.getTotalPrice().subtract(discountAmount);

            orderItem.setTotalPrice(newTotalPrice);
            orderItem.setDiscountPrice(discountAmount);
            orderItem.addAppliedPromotion(promotion, discountAmount);
        }
    }

    private BigInteger calculateDiscountAmount(BigInteger totalPrice, BigInteger percent) {
        return totalPrice.multiply(percent).divide(BigInteger.valueOf(100));
    }
}
