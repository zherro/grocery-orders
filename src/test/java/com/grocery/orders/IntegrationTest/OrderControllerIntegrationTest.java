package com.grocery.orders.IntegrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grocery.orders.IntegrationTest.data.OrderData;
import com.grocery.orders.domain.OrderItem;
import com.grocery.orders.domain.OrderItemProduct;
import com.grocery.orders.domain.Product;
import com.grocery.orders.gateway.database.OrderItemRepository;
import com.grocery.orders.gateway.database.OrderRepository;
import com.grocery.orders.gateway.database.entity.OrderEntity;
import com.grocery.orders.gateway.http.ProductApiHttpIntegration;
import com.grocery.orders.gateway.service.OrderService;
import com.grocery.orders.mapper.OrderMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderMapper orderMapper;

    @MockBean
    private ProductApiHttpIntegration productApiHttpIntegration;

    @BeforeEach
    void clear() {
        orderItemRepository.deleteAll();
        orderRepository.deleteAll();
    }

    @Test
    void shouldCreateNewOrder() throws Exception {
        // Given
        createOrder(OrderData.CREATE_ORDER_PAYLOAD, status().isCreated());

        // When
        var orders = (List<OrderEntity>) orderRepository.findAll();
        var order = orders.get(0);

        // Assert
        assertThat(orders).hasSize(1);
        assertThat(order.getProducts()).hasSize(2);
        assertThat(order.getCustomerId()).isEqualTo("customer_test_id");
        assertThat(order.getCustomerName()).isEqualTo("customer_test_name");
    }

    @Test
    void shouldReturnAnErrorWhenIsInvalidInputPayload() throws Exception {
        // Given
        createOrder(OrderData.CREATE_ORDER_PAYLOAD_INVALID_1, status().isBadRequest());
        createOrder(OrderData.CREATE_ORDER_PAYLOAD_INVALID_2, status().isBadRequest());

        // When
        var orders = (List<OrderEntity>) orderRepository.findAll();

        // Assert
        assertThat(orders).isEmpty();
    }

    @Test
    void shouldGetCreatedOrderById() throws Exception {
        // Given
        createOrder(OrderData.CREATE_ORDER_PAYLOAD_0, status().isCreated());

        // When
        var order = getFirstOrder();
        mockProductResponse(order);

        // Assert
        mockMvc.perform(get("/api/orders/{id}", order.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customer_id").value("customer_test_id"))
                .andExpect(jsonPath("$.customer_name").value("customer_test_name"))
                .andExpect(jsonPath("$.status").value("OPEN"))
                .andExpect(jsonPath("$.order_total_discount").value(999))
                .andExpect(jsonPath("$.order_total_price").value(3996))
                .andExpect(jsonPath("$.products[0].product_id").value("PWWe3w1SDU"))
                .andExpect(jsonPath("$.products[0].product_name").value("Amazing Burger!"))
                .andExpect(jsonPath("$.products[0].price").value(999))
                .andExpect(jsonPath("$.products[0].qty").value(5))
                .andExpect(jsonPath("$.products[0].discount_price").value(999))
                .andExpect(jsonPath("$.products[0].applied_promotions[0].promotion_type").value("BUY_X_GET_Y_FREE"))
                .andExpect(jsonPath("$.products[0].applied_promotions[0].total_discount").value(999))
                .andExpect(jsonPath("$.products[0].applied_promotions[0].enabled").value(true))
                .andExpect(jsonPath("$.products[0].total_price").value(3996));
    }

    @Test
    void shouldReturn404ErrorWhenOrderNotFound() throws Exception {
        // Assert
        mockMvc.perform(get("/api/orders/{id}", "not_found_id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Order not found"))
                .andExpect(jsonPath("$.type").value("BUSINESS_VALIDATION"))
                .andExpect(jsonPath("$.fields").isEmpty());
    }

    @Test
    void shouldUpdateOrder() throws Exception {
        // Given
        createOrder(OrderData.CREATE_ORDER_PAYLOAD_0, status().isCreated());

        // When
        var order = getFirstOrder();

        mockMvc.perform(put("/api/orders/{id}", order.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OrderData.ORDER_UPDATE))
                .andExpect(status().isAccepted());

        // Assert
        mockProductResponse(order);
        mockMvc.perform(get("/api/orders/{id}", order.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customer_id").value("customer_test_id"))
                .andExpect(jsonPath("$.customer_name").value("customer_test_name"))
                .andExpect(jsonPath("$.status").value("OPEN"))
                .andExpect(jsonPath("$.order_total_discount").value(0))
                .andExpect(jsonPath("$.order_total_price").value(1998))
                .andExpect(jsonPath("$.products[0].product_id").value("PWWe3w1SDU"))
                .andExpect(jsonPath("$.products[0].product_name").value("Amazing Burger!"))
                .andExpect(jsonPath("$.products[0].price").value(999))
                .andExpect(jsonPath("$.products[0].qty").value(2))
                .andExpect(jsonPath("$.products[0].discount_price").value(0))
                .andExpect(jsonPath("$.products[0].total_price").value(1998));
    }

    private void createOrder(String payload, ResultMatcher status) throws Exception {
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status);
    }

    private OrderEntity getFirstOrder() {
        var orders = (List<OrderEntity>) orderRepository.findAll();
        return orders.get(0);
    }

    private OrderItemProduct mockProductResponse(OrderEntity order) throws Exception {
        var product = objectMapper.readValue(OrderData.MOCK_PRODUCT_RESPONSE, Product.class);
        var orderItem = orderMapper.entityToDto(order).getProducts().get(0);
        var orderitemProduct = new OrderItemProduct(orderItem, product);

        Mockito.when(productApiHttpIntegration.enrichOrderProducts(Mockito.any(OrderItem.class)))
                .thenReturn(orderitemProduct);
        return orderitemProduct;
    }
}
