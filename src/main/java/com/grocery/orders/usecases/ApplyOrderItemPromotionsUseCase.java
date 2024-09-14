package com.grocery.orders.usecases;

import com.grocery.orders.domain.OrderItem;
import com.grocery.orders.domain.OrderItemProduct;
import com.grocery.orders.domain.Promotion;
import com.grocery.orders.usecases.promotions.ApplyBuyXGetYFreePromotion;
import com.grocery.orders.usecases.promotions.ApplyFlatPercentPromotion;
import com.grocery.orders.usecases.promotions.ApplyQtyBasedPriceOverridePromotion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ApplyOrderItemPromotionsUseCase implements UseCase<OrderItemProduct, OrderItem> {

    private final ApplyFlatPercentPromotion applyFlatPercentPromotion;
    private final ApplyBuyXGetYFreePromotion applyBuyXGetYFreePromotion;
    private final ApplyQtyBasedPriceOverridePromotion applyQtyBasedPriceOverridePromotion;

    @Override
    public OrderItem execute(OrderItemProduct input) {
        var orderItem = input.getOrderItem();

        if(Objects.nonNull(input.getProduct()) && Objects.nonNull(input.getProduct().getPromotions())) {
           input.getProduct().getPromotions().forEach(
                   promotion -> applyOrderItemPromotion(orderItem, promotion));
        }
        return orderItem;
    }

    private void applyOrderItemPromotion(final OrderItem orderItem, final Promotion promotion) {
        switch (promotion.getType()) {
            case BUY_X_GET_Y_FREE -> applyBuyXGetYFreePromotion.execute(orderItem, promotion);
            case QTY_BASED_PRICE_OVERRIDE -> applyQtyBasedPriceOverridePromotion.execute(orderItem, promotion);
            case FLAT_PERCENT -> applyFlatPercentPromotion.execute(orderItem, promotion);
        }
    }
}
