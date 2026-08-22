package com.crimsonlogic.ecommerce.controller;

import com.crimsonlogic.ecommerce.dto.order.OrderResponseDTO;
import com.crimsonlogic.ecommerce.service.impl.OrderServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderServiceImpl orderService;

    public OrderController(OrderServiceImpl orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/history")
    public ResponseEntity<List<OrderResponseDTO>> getOrderHistory(
            @RequestAttribute("userId") String userId,
            @RequestAttribute("role") String role) {

        return ResponseEntity.ok(orderService.getMyOrders(userId, role));
    }
}