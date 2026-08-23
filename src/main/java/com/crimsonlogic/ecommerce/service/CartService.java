package com.crimsonlogic.ecommerce.service;

import com.crimsonlogic.ecommerce.dto.cart.CartRequestDTO;
import com.crimsonlogic.ecommerce.dto.cart.CartResponseDTO;

import java.util.List;

public interface CartService {

    String addToCart(CartRequestDTO request, String customerId);

    List<CartResponseDTO> viewCart(String customerId);

    String removeFromCart(String cartId, String customerId);
}