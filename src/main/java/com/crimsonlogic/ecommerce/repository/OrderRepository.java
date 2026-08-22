package com.crimsonlogic.ecommerce.repository;

import com.crimsonlogic.ecommerce.entity.Order;
import com.crimsonlogic.ecommerce.enumeration.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByCustomerIdOrderByOrderDateDesc(String customerId);
    List<Order> findByProductSellerIdOrderByOrderDateDesc(String sellerId);
    boolean existsByCustomerIdAndProductIdAndStatus(String customerId, String productId, OrderStatus status);
}