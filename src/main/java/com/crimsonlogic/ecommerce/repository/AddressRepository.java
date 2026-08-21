package com.crimsonlogic.ecommerce.repository;

import com.crimsonlogic.ecommerce.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, String> {
}