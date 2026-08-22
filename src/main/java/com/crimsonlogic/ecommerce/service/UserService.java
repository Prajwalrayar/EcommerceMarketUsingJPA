package com.crimsonlogic.ecommerce.service;

import com.crimsonlogic.ecommerce.dto.address.AddressDTO;
import com.crimsonlogic.ecommerce.dto.address.AddressRequestDTO;
import com.crimsonlogic.ecommerce.dto.user.CustomerProfileUpdateRequestDTO;
import com.crimsonlogic.ecommerce.dto.user.SellerProfileUpdateRequestDTO;
import com.crimsonlogic.ecommerce.dto.user.UpdatePhoneRequestDTO;
import com.crimsonlogic.ecommerce.dto.user.UserResponseDTO;

import javax.validation.Valid;

public interface UserService {
    // Admin Operations
    UserResponseDTO getAdminProfile(String adminId);
    UserResponseDTO updateAdminPhone(String adminId, UpdatePhoneRequestDTO request);

    // Customer Operations
    UserResponseDTO getCustomerProfile(String customerId);
    UserResponseDTO updateCustomerProfile(String customerId, CustomerProfileUpdateRequestDTO request);
    UserResponseDTO addCustomerAddress(String customerId, AddressDTO request);
    void removeCustomerAddress(String customerId, String addressId);

    // Seller Operations
    UserResponseDTO getSellerProfile(String sellerId);
    UserResponseDTO updateSellerProfile(String sellerId, SellerProfileUpdateRequestDTO request);
    UserResponseDTO addSellerAddress(String sellerId, @Valid AddressRequestDTO request);
    void removeSellerAddress(String sellerId, String addressId);
}