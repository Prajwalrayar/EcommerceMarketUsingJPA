package com.crimsonlogic.ecommerce.service;

import com.crimsonlogic.ecommerce.dto.auth.CustomerRegistrationRequestDTO;
import com.crimsonlogic.ecommerce.dto.auth.LoginRequestDTO;
import com.crimsonlogic.ecommerce.dto.auth.LoginResponseDTO;
import com.crimsonlogic.ecommerce.dto.auth.SellerRegistrationRequestDTO;

public interface AuthenticationService {
    LoginResponseDTO loginCustomer(LoginRequestDTO request);
    LoginResponseDTO loginSeller(LoginRequestDTO request);
    LoginResponseDTO loginAdmin(LoginRequestDTO request);
    LoginResponseDTO registerCustomer(CustomerRegistrationRequestDTO request);
    LoginResponseDTO registerSeller(SellerRegistrationRequestDTO request);
}