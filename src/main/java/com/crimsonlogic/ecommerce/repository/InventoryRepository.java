package com.crimsonlogic.ecommerce.repository;

import com.crimsonlogic.ecommerce.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, String> {
    Optional<Inventory> findByProductId(String productId);
    List<Inventory> findByProductSellerId(String sellerId);
    List<Inventory> findByProductSellerIsNull(); // Retrieves admin-created product inventory
}