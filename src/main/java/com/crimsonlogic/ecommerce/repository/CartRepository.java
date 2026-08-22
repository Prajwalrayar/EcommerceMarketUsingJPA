package com.crimsonlogic.ecommerce.repository;

import com.crimsonlogic.ecommerce.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, String> {
    List<Cart> findByCustomerId(String customerId);
    Optional<Cart> findByCustomerIdAndProductId(String customerId, String productId);
    void deleteByCustomerId(String customerId);
}