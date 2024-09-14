package com.grocery.orders.usecases.promotions;

import com.grocery.orders.domain.OrderItem;
import com.grocery.orders.domain.Promotion;
import com.grocery.orders.domain.enums.PromotionType;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public class ApplyQtyBasedPriceOverridePromotion {

    public void execute(final OrderItem orderItem, Promotion promotion) {
        if (PromotionType.QTY_BASED_PRICE_OVERRIDE.equals(promotion.getType())
                && orderItem.getQty() >= promotion.getRequiredQty()) {

            BigInteger totalPrice = orderItem.getTotalPrice()
                    .subtract(orderItem.getPrice()
                            .multiply(BigInteger.valueOf(promotion.getRequiredQty())))
                    .add(promotion.getPrice());
            BigInteger promoTotalDiscount = orderItem.getTotalPrice().subtract(totalPrice);

            orderItem.setDiscountPrice(promoTotalDiscount);
            orderItem.setTotalPrice(totalPrice);
            orderItem.addAppliedPromotion(promotion, promoTotalDiscount);
        }
    }
}
