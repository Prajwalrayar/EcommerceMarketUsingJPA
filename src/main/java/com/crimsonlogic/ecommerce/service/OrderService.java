package com.crimsonlogic.ecommerce.service;

import com.crimsonlogic.ecommerce.dto.order.CheckoutRequestDTO;
import com.crimsonlogic.ecommerce.dto.order.OrderResponseDTO;

import java.util.List;

public interface OrderService {

    String checkout(String customerId, CheckoutRequestDTO request);

    List<OrderResponseDTO> getMyOrders(String userId, String role);

    String updateOrderStatus(
            String orderId,
            String newStatusString,
            String userId,
            String role
    );

    String cancelOrder(String orderId, String customerId);

    String requestReturn(String orderId, String customerId);
}