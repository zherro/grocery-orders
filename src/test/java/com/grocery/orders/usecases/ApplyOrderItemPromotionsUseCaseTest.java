package com.grocery.orders.usecases;

import com.grocery.orders.domain.OrderItem;
import com.grocery.orders.domain.OrderItemProduct;
import com.grocery.orders.domain.Product;
import com.grocery.orders.domain.Promotion;
import com.grocery.orders.domain.enums.PromotionType;
import com.grocery.orders.usecases.promotions.ApplyBuyXGetYFreePromotion;
import com.grocery.orders.usecases.promotions.ApplyFlatPercentPromotion;
import com.grocery.orders.usecases.promotions.ApplyQtyBasedPriceOverridePromotion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigInteger;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ApplyOrderItemPromotionsUseCaseTest {

    @Mock
    private ApplyFlatPercentPromotion applyFlatPercentPromotion;

    @Mock
    private ApplyBuyXGetYFreePromotion applyBuyXGetYFreePromotion;

    @Mock
    private ApplyQtyBasedPriceOverridePromotion applyQtyBasedPriceOverridePromotion;

    @InjectMocks
    private ApplyOrderItemPromotionsUseCase applyOrderItemPromotionsUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldApplyFlatPercentPromotion() {
        // Given
        OrderItem orderItem = new OrderItem();
        orderItem.setPrice(new BigInteger("1000"));
        orderItem.setTotalPrice(new BigInteger("1000"));

        Promotion promotion = new Promotion();
        promotion.setType(PromotionType.FLAT_PERCENT);
        promotion.setAmount(BigInteger.valueOf(10));

        OrderItemProduct input = new OrderItemProduct(orderItem, new Product());
        input.getProduct().setPromotions(Collections.singletonList(promotion));

        // When
        applyOrderItemPromotionsUseCase.execute(input);

        // Assert
        verify(applyFlatPercentPromotion).execute(orderItem, promotion);
        verifyNoMoreInteractions(applyFlatPercentPromotion, applyBuyXGetYFreePromotion, applyQtyBasedPriceOverridePromotion);
    }

    @Test
    void shouldApplyBuyXGetYFreePromotion() {
        // Given
        OrderItem orderItem = new OrderItem();
        orderItem.setPrice(new BigInteger("1000"));
        orderItem.setTotalPrice(new BigInteger("2000"));

        Promotion promotion = new Promotion();
        promotion.setType(PromotionType.BUY_X_GET_Y_FREE);
        promotion.setRequiredQty(2L);
        promotion.setFreeQty(1L);

        OrderItemProduct input = new OrderItemProduct(orderItem, new Product());
        input.getProduct().setPromotions(Collections.singletonList(promotion));

        // When
        applyOrderItemPromotionsUseCase.execute(input);

        // Assert
        verify(applyBuyXGetYFreePromotion).execute(orderItem, promotion);
        verifyNoMoreInteractions(applyFlatPercentPromotion, applyBuyXGetYFreePromotion, applyQtyBasedPriceOverridePromotion);
    }

    @Test
    void shouldApplyQtyBasedPriceOverridePromotion() {
        // Given
        OrderItem orderItem = new OrderItem();
        orderItem.setPrice(new BigInteger("1000"));
        orderItem.setTotalPrice(new BigInteger("2000"));

        Promotion promotion = new Promotion();
        promotion.setType(PromotionType.QTY_BASED_PRICE_OVERRIDE);
        promotion.setRequiredQty(2L);
        promotion.setPrice(new BigInteger("1799"));

        OrderItemProduct input = new OrderItemProduct(orderItem, new Product());
        input.getProduct().setPromotions(Collections.singletonList(promotion));

        // When
        applyOrderItemPromotionsUseCase.execute(input);

        // Assert
        verify(applyQtyBasedPriceOverridePromotion).execute(orderItem, promotion);
        verifyNoMoreInteractions(applyFlatPercentPromotion, applyBuyXGetYFreePromotion, applyQtyBasedPriceOverridePromotion);
    }

    @Test
    void shouldHandleNoPromotions() {
        // Given
        OrderItem orderItem = new OrderItem();
        orderItem.setPrice(new BigInteger("1000"));
        orderItem.setTotalPrice(new BigInteger("1000"));

        OrderItemProduct input = new OrderItemProduct(orderItem, new Product());

        // When
        OrderItem result = applyOrderItemPromotionsUseCase.execute(input);

        // Assert
        assertThat(result).isEqualTo(orderItem);
        verifyNoInteractions(applyFlatPercentPromotion, applyBuyXGetYFreePromotion, applyQtyBasedPriceOverridePromotion);
    }

    @Test
    void shouldHandleNullOrderItemProduct() {
        // Given
        OrderItemProduct input = new OrderItemProduct(null, null);

        // When
        OrderItem result = applyOrderItemPromotionsUseCase.execute(input);

        // Assert
        assertThat(result).isNull();
        verifyNoInteractions(applyFlatPercentPromotion, applyBuyXGetYFreePromotion, applyQtyBasedPriceOverridePromotion);
    }

    @Test
    void shouldHandleNullOrderItem() {
        // Given

        OrderItemProduct input = new OrderItemProduct(null, new Product());

        // When
        OrderItem result = applyOrderItemPromotionsUseCase.execute(input);

        // Assert
        assertThat(result).isNull();
        verifyNoInteractions(applyFlatPercentPromotion, applyBuyXGetYFreePromotion, applyQtyBasedPriceOverridePromotion);
    }
}
