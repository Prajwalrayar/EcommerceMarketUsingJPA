package com.crimsonlogic.ecommerce.repository;

import com.crimsonlogic.ecommerce.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, String> {
    Optional<Admin> findByEmail(String email);
    Optional<Admin> findByPhone(String phone);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}