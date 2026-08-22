package com.crimsonlogic.ecommerce.controller;

import com.crimsonlogic.ecommerce.dto.order.CheckoutRequestDTO;
import com.crimsonlogic.ecommerce.handler.ApiResponse;
import com.crimsonlogic.ecommerce.service.impl.CheckoutServiceImpl;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer/checkout")
public class CheckoutController {

    private final CheckoutServiceImpl checkoutService;

    public CheckoutController(CheckoutServiceImpl checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<String>> processCheckout(
            @RequestAttribute("userId") String customerId,
            @Valid @RequestBody CheckoutRequestDTO request) {

        String response = checkoutService.checkout(customerId, request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Checkout completed successfully",
                        response
                )
        );
    }
}