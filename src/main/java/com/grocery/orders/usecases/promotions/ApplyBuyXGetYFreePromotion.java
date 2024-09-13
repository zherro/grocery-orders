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

    public void execute(final OrderItem orderItem, Promotion promotion) {
        if (Objects.nonNull(promotion) && PromotionType.BUY_X_GET_Y_FREE.equals(promotion.getType())
                && Objects.nonNull(orderItem) && orderItem.getQty() >= promotion.getRequiredQty()) {

            if (orderItem.getQty() > promotion.getRequiredQty()) {
                BigInteger totalFreeItemValue =  Optional.ofNullable(orderItem.getPrice()).orElse(BigInteger.ZERO);

                BigInteger currentTotalPrice = Optional.ofNullable(orderItem.getTotalPrice()).orElse(BigInteger.ZERO);
                BigInteger newTotalPrice = currentTotalPrice.subtract(totalFreeItemValue);

                var discount = Optional.ofNullable(orderItem.getDiscountPrice())
                        .orElse(BigInteger.ZERO).add(totalFreeItemValue);
                orderItem.setDiscountPrice(discount);
                orderItem.setTotalPrice(newTotalPrice);
                orderItem.addAppliedPromotion(promotion, totalFreeItemValue);
            }
        }
    }
}
