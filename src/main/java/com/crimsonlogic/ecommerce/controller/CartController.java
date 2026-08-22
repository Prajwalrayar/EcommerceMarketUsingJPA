package com.crimsonlogic.ecommerce.controller;

import com.crimsonlogic.ecommerce.dto.cart.CartRequestDTO;
import com.crimsonlogic.ecommerce.service.impl.CartServiceImpl;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer/cart")
public class CartController {

    private final CartServiceImpl cartService;

    public CartController(CartServiceImpl cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(
            @RequestAttribute("userId") String customerId,
            @Valid @RequestBody CartRequestDTO request) {
        return ResponseEntity.ok(cartService.addToCart(customerId, request));
    }
}