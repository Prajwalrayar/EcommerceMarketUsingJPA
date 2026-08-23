package com.crimsonlogic.ecommerce.controller;

import com.crimsonlogic.ecommerce.dto.address.AddressRequestDTO;
import com.crimsonlogic.ecommerce.dto.address.AddressResponseDTO;
import com.crimsonlogic.ecommerce.handler.ApiResponse;
import com.crimsonlogic.ecommerce.service.impl.AddressServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/addresses")
public class AddressController {

    private final AddressServiceImpl addressService;

    public AddressController(AddressServiceImpl addressService) {
        this.addressService = addressService;
    }

    // ==========================================================
    // CUSTOMER ENDPOINTS
    // ==========================================================

    @PostMapping("/customer/add")
    public ResponseEntity<ApiResponse<String>> addCustomerAddress(
            @Valid @RequestBody AddressRequestDTO request,
            @RequestAttribute("userId") String userId,
            @RequestAttribute("role") String role) {

        if (!"CUSTOMER".equals(role)) {
            return ResponseEntity.status(403).body(ApiResponse.error("Only customers can use this endpoint."));
        }
        String message = addressService.addCustomerAddress(request, userId);
        return ResponseEntity.ok(ApiResponse.success(message));
    }

    @GetMapping("/customer/my-addresses")
    public ResponseEntity<ApiResponse<List<AddressResponseDTO>>> getCustomerAddresses(
            @RequestAttribute("userId") String userId,
            @RequestAttribute("role") String role) {

        if (!"CUSTOMER".equals(role)) {
            return ResponseEntity.status(403).build();
        }
        List<AddressResponseDTO> addresses = addressService.getCustomerAddresses(userId);
        return ResponseEntity.ok(ApiResponse.success("Customer addresses retrieved successfully", addresses));
    }

    // ==========================================================
    // SELLER ENDPOINTS
    // ==========================================================

    @PostMapping("/seller/add")
    public ResponseEntity<ApiResponse<String>> addSellerAddress(
            @Valid @RequestBody AddressRequestDTO request,
            @RequestAttribute("userId") String userId,
            @RequestAttribute("role") String role) {

        if (!"SELLER".equals(role)) {
            return ResponseEntity.status(403).body(ApiResponse.error("Only sellers can use this endpoint."));
        }
        String message = addressService.addSellerAddress(request, userId);
        return ResponseEntity.ok(ApiResponse.success(message));
    }

    @GetMapping("/seller/my-addresses")
    public ResponseEntity<ApiResponse<List<AddressResponseDTO>>> getSellerAddresses(
            @RequestAttribute("userId") String userId,
            @RequestAttribute("role") String role) {

        if (!"SELLER".equals(role)) {
            return ResponseEntity.status(403).build();
        }
        List<AddressResponseDTO> addresses = addressService.getSellerAddresses(userId);
        return ResponseEntity.ok(ApiResponse.success("Seller addresses retrieved successfully", addresses));
    }
}