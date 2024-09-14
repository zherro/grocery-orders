package com.grocery.orders.web.controller.doc;

import com.grocery.orders.config.exception.ErrorResponse;
import com.grocery.orders.domain.Order;
import com.grocery.orders.web.request.CreateOrderRequest;
import com.grocery.orders.web.request.UpdateOrderRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
}
