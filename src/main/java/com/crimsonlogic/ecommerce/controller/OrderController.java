package com.crimsonlogic.ecommerce.controller;

import com.crimsonlogic.ecommerce.dto.order.CheckoutRequestDTO;
import com.crimsonlogic.ecommerce.dto.order.OrderResponseDTO;
import com.crimsonlogic.ecommerce.handler.ApiResponse;
import com.crimsonlogic.ecommerce.service.impl.OrderServiceImpl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderServiceImpl orderService;

    public OrderController(OrderServiceImpl orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<ApiResponse<String>> checkout(
            @Valid @RequestBody CheckoutRequestDTO request,
            @RequestAttribute("userId") String userId,
            @RequestAttribute("role") String role) {

        if (!"CUSTOMER".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(
                            "Only customers can checkout."
                    ));
        }

        // Pass the FULL request object to the service
        // so it can check for new vs old address
        String response = orderService.checkout(userId, request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Checkout completed successfully",
                        response
                )
        );
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<ApiResponse<String>> updateOrderStatus(
            @PathVariable String orderId,
            @Valid @RequestBody com.crimsonlogic.ecommerce.dto.order.OrderStatusUpdateRequestDTO request,
            @RequestAttribute("userId") String userId,
            @RequestAttribute("role") String role) {

        String response = orderService.updateOrderStatus(
                orderId,
                request.getStatus(),
                userId,
                role
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Order status updated successfully",
                        response
                )
        );
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<ApiResponse<String>> cancelCustomerOrder(
            @PathVariable String orderId,
            @RequestAttribute("userId") String userId,
            @RequestAttribute("role") String role) {

        if (!"CUSTOMER".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(
                            "Only customers can use the cancellation endpoint."
                    ));
        }

        String response = orderService.cancelOrder(orderId, userId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Order cancelled successfully",
                        response
                )
        );
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<OrderResponseDTO>>> getOrderHistory(
            @RequestAttribute("userId") String userId,
            @RequestAttribute("role") String role) {

        List<OrderResponseDTO> orders =
                orderService.getMyOrders(userId, role);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Order history retrieved successfully",
                        orders
                )
        );
    }

    @PutMapping("/{orderId}/return")
    public ResponseEntity<ApiResponse<String>> requestOrderReturn(
            @PathVariable String orderId,
            @RequestAttribute("userId") String userId,
            @RequestAttribute("role") String role) {

        if (!"CUSTOMER".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(
                            "Only customers can request order returns."
                    ));
        }

        String response = orderService.requestReturn(orderId, userId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Order return requested successfully",
                        response
                )
        );
    }
}