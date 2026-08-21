package com.crimsonlogic.ecommerce.repository;

import com.crimsonlogic.ecommerce.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SellerRepository extends JpaRepository<Seller, String> {
    Optional<Seller> findByEmail(String email);
    Optional<Seller> findByPhone(String phone);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

}