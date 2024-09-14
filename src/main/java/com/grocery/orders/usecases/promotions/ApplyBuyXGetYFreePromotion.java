package com.grocery.orders.usecases.promotions;

import com.grocery.orders.domain.OrderItem;
import com.grocery.orders.domain.Promotion;
import com.grocery.orders.domain.enums.PromotionType;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Objects;
import java.util.Optional;

@Component
public class ApplyBuyXGetYFreePromotion {

    private boolean shouldApplyPromotion(final OrderItem orderItem, final Promotion promotion) {
        return Objects.nonNull(promotion)
                && PromotionType.BUY_X_GET_Y_FREE.equals(promotion.getType())
                && Objects.nonNull(orderItem)
                && orderItem.getQty() >= promotion.getRequiredQty()
                && orderItem.getQty() > promotion.getRequiredQty();
    }

    public void execute(final OrderItem orderItem, final Promotion promotion) {
        if (shouldApplyPromotion(orderItem, promotion)) {
            BigInteger totalFreeItemValue = orderItem.getPrice()
                    .multiply(BigInteger.valueOf(promotion.getFreeQty()));
            BigInteger newTotalPrice = orderItem.getTotalPrice().subtract(totalFreeItemValue);

            var discount = Optional.ofNullable(orderItem.getDiscountPrice())
                    .orElse(BigInteger.ZERO).add(totalFreeItemValue);
            orderItem.setDiscountPrice(discount);
            orderItem.setTotalPrice(newTotalPrice);
            orderItem.addAppliedPromotion(promotion, totalFreeItemValue);
        }
    }
}
