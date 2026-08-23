package com.crimsonlogic.ecommerce.service;

import com.crimsonlogic.ecommerce.dto.address.AddressRequestDTO;
import com.crimsonlogic.ecommerce.dto.address.AddressResponseDTO;

import java.util.List;

public interface AddressService {

    // Customer
    String addCustomerAddress(AddressRequestDTO request, String customerId);

    List<AddressResponseDTO> getCustomerAddresses(String customerId);

    // Seller
    String addSellerAddress(AddressRequestDTO request, String sellerId);

    List<AddressResponseDTO> getSellerAddresses(String sellerId);
}