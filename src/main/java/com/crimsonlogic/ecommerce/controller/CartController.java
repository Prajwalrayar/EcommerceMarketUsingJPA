package com.crimsonlogic.ecommerce.controller;

import com.crimsonlogic.ecommerce.dto.cart.CartRequestDTO;
import com.crimsonlogic.ecommerce.dto.cart.CartResponseDTO;
import com.crimsonlogic.ecommerce.handler.ApiResponse;
import com.crimsonlogic.ecommerce.service.impl.CartServiceImpl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/customer/cart")
public class CartController {

    private final CartServiceImpl cartService;

    public CartController(CartServiceImpl cartService) {
        this.cartService = cartService;
    }

    // 1. Add to Cart
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<String>> addToCart(
            @RequestAttribute("userId") String customerId,
            @Valid @RequestBody CartRequestDTO request) {

        String response = cartService.addToCart(request, customerId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Product added to cart successfully",
                        response
                )
        );
    }

    // 2. View Cart
    @GetMapping
    public ResponseEntity<ApiResponse<List<CartResponseDTO>>> viewCart(
            @RequestAttribute("userId") String userId,
            @RequestAttribute("role") String role) {

        if (!"CUSTOMER".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(
                            "Only customers can view their cart."
                    ));
        }

        List<CartResponseDTO> cart = cartService.viewCart(userId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Cart retrieved successfully",
                        cart
                )
        );
    }

    // 3. Remove from Cart
    @DeleteMapping("/remove/{cartId}")
    public ResponseEntity<ApiResponse<String>> removeFromCart(
            @PathVariable String cartId,
            @RequestAttribute("userId") String userId,
            @RequestAttribute("role") String role) {

        if (!"CUSTOMER".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(
                            "Only customers can modify their cart."
                    ));
        }

        String response = cartService.removeFromCart(cartId, userId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Product removed from cart successfully",
                        response
                )
        );
    }
}