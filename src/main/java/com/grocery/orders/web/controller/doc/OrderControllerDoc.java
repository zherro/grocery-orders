package com.grocery.orders.web.controller.doc;

import com.grocery.orders.config.exception.ErrorResponse;
import com.grocery.orders.domain.Order;
import com.grocery.orders.domain.enums.OrderStatus;
import com.grocery.orders.web.request.CreateOrderRequest;
import com.grocery.orders.web.request.UpdateOrderRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface OrderControllerDoc {

    @Operation(summary = "Create a new order", description = "This endpoint allows you to create a new order by " +
            "providing customer id and name, and optionally a list of products.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created successfully"),
            @ApiResponse(responseCode = "400",  description = "Error response",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Order createOrder(@RequestBody @Valid CreateOrderRequest createOrderRequest);

    @Operation(summary = "Find order by ID", description = "This endpoint allows you to find an order by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order found successfully"),
            @ApiResponse(responseCode = "404",  description = "Order not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(responseCode = "400",  description = "Error response",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping("/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    Order findOrderById(@PathVariable String orderId);

    @Operation(summary = "Update order by ID", description = "This endpoint allows you to update order by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order updated successfully"),
            @ApiResponse(responseCode = "404",  description = "Order not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(responseCode = "400",  description = "Error response",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PutMapping("/{orderId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    Order updateOrder(
            @PathVariable String orderId, @RequestBody @Valid UpdateOrderRequest updateOrderRequest);


    @Operation(summary = "Search for orders", description = "This endpoint allows searching for orders based on customer ID and status with pagination support. "
            + "The response will include the orders that match the search criteria, and pagination details such as total pages and current page.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders found successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "400", description = "Invalid search parameters",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "No orders found matching the criteria",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping()
    Page<Order> searchOrders(
            @RequestParam(required = false, name = "customer_id") String customerId,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size);
}
