package com.crimsonlogic.ecommerce.service;

import com.crimsonlogic.ecommerce.dto.order.CheckoutRequestDTO;

public interface CheckoutService {

    String checkout(String customerId, CheckoutRequestDTO request);
}