package com.crimsonlogic.ecommerce.repository;

import com.crimsonlogic.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findBySellerId(String sellerId);
    List<Product> findByCategoryId(String categoryId);
    List<Product> findByCreatedBy(String createdBy);
    Optional<Product> findByIdAndSellerId(String productId, String sellerId);
}