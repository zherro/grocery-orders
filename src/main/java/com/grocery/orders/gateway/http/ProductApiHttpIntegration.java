package com.grocery.orders.gateway.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grocery.orders.config.exception.BusinessException;
import com.grocery.orders.domain.OrderItem;
import com.grocery.orders.domain.OrderItemProduct;
import com.grocery.orders.domain.Product;
import com.grocery.orders.domain.enums.ProductStatus;
import com.grocery.orders.gateway.http.factory.HttpClientFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductApiHttpIntegration {

    private final HttpClientFactory httpClientFactory;

    public OrderItemProduct enrichOrderProducts(final OrderItem orderItem) {
        log.info("m=enrichOrderProducts, retrieving product information for ID: {}", orderItem.getProductId());
        try {
            var response = httpClientFactory.HttpGetClient("http://localhost:8081/product/PWWe3w1SDU");

            switch (response.statusCode()) {
                case 200:
                    ObjectMapper objectMapper = new ObjectMapper();
                    Product product = objectMapper.readValue(response.body(), Product.class);
                    orderItem.setProductStatus(ProductStatus.OK);
                    return new OrderItemProduct(orderItem, product);
                case 404:
                    orderItem.setProductStatus(ProductStatus.NOT_FOUND);
                    break;
                default:
                    throw new BusinessException("Error on retrieve order products information");
            }

            return new OrderItemProduct(orderItem, null);
        } catch (IOException | InterruptedException e) {
            log.error("m=enrichOrderProducts, error on retrieve product information", e);
            throw new BusinessException("Error on retrieve order products information");
        }
    }

}
