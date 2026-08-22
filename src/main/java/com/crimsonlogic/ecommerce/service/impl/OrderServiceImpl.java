package com.crimsonlogic.ecommerce.service.impl;

import com.crimsonlogic.ecommerce.dto.order.OrderResponseDTO;
import com.crimsonlogic.ecommerce.entity.Order;
import com.crimsonlogic.ecommerce.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<OrderResponseDTO> getMyOrders(String userId, String role) {
        List<Order> orders;

        if ("SELLER".equals(role)) {
            // Sellers see orders placed for their products
            orders = orderRepository.findByProductSellerIdOrderByOrderDateDesc(userId);
        } else {
            // Customers see their own purchase history
            orders = orderRepository.findByCustomerIdOrderByOrderDateDesc(userId);
        }

        return orders.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private OrderResponseDTO mapToDTO(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setOrderId(order.getId());
        dto.setProductName(order.getProduct().getName());
        dto.setQuantity(order.getQuantity());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setStatus(order.getStatus().name());
        dto.setOrderDate(order.getOrderDate());
        return dto;
    }
}