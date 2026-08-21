package com.crimsonlogic.ecommerce.repository;

import com.crimsonlogic.ecommerce.entity.ShopAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ShopAddressRepository extends JpaRepository<ShopAddress, String> {
    List<ShopAddress> findBySellerId(String sellerId);
}