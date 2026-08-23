package com.crimsonlogic.ecommerce.controller;

import com.crimsonlogic.ecommerce.dto.address.AddressDTO;
import com.crimsonlogic.ecommerce.dto.address.AddressRequestDTO;
import com.crimsonlogic.ecommerce.dto.user.CustomerProfileUpdateRequestDTO;
import com.crimsonlogic.ecommerce.dto.user.SellerProfileUpdateRequestDTO;
import com.crimsonlogic.ecommerce.dto.user.UpdatePhoneRequestDTO;
import com.crimsonlogic.ecommerce.dto.user.UserResponseDTO;
import com.crimsonlogic.ecommerce.handler.ApiResponse;
import com.crimsonlogic.ecommerce.service.UserService;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class UserProfileController {

    private final UserService userService;

    public UserProfileController(UserService userService) {
        this.userService = userService;
    }

    // ==========================================================
    // ADMIN ENDPOINTS
    // ==========================================================

    @GetMapping("/admin/profile")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getAdminProfile(
            @RequestAttribute("userId") String adminId) {

        UserResponseDTO response =
                userService.getAdminProfile(adminId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Admin profile retrieved successfully",
                        response
                )
        );
    }

    @PutMapping("/admin/profile/phone")
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateAdminPhone(
            @RequestAttribute("userId") String adminId,
            @Valid @RequestBody UpdatePhoneRequestDTO request) {

        UserResponseDTO response =
                userService.updateAdminPhone(adminId, request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Admin phone number updated successfully",
                        response
                )
        );
    }

    // ==========================================================
    // CUSTOMER ENDPOINTS
    // ==========================================================

    @GetMapping("/customer/profile")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getCustomerProfile(
            @RequestAttribute("userId") String customerId) {

        UserResponseDTO response =
                userService.getCustomerProfile(customerId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Customer profile retrieved successfully",
                        response
                )
        );
    }

    @PutMapping("/customer/profile")
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateCustomerProfile(
            @RequestAttribute("userId") String customerId,
            @Valid @RequestBody CustomerProfileUpdateRequestDTO request) {

        UserResponseDTO response =
                userService.updateCustomerProfile(
                        customerId,
                        request
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Customer profile updated successfully",
                        response
                )
        );
    }

    @PostMapping("/customer/address")
    public ResponseEntity<ApiResponse<UserResponseDTO>> addCustomerAddress(
            @RequestAttribute("userId") String customerId,
            @Valid @RequestBody AddressDTO request) {

        UserResponseDTO response =
                userService.addCustomerAddress(
                        customerId,
                        request
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Customer address added successfully",
                        response
                )
        );
    }

    @DeleteMapping("/customer/address/{addressId}")
    public ResponseEntity<ApiResponse<Void>> removeCustomerAddress(
            @RequestAttribute("userId") String customerId,
            @PathVariable String addressId) {

        userService.removeCustomerAddress(
                customerId,
                addressId
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Address removed successfully."
                )
        );
    }

    // ==========================================================
    // SELLER ENDPOINTS
    // ==========================================================

    @GetMapping("/seller/profile")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getSellerProfile(
            @RequestAttribute("userId") String sellerId) {

        UserResponseDTO response =
                userService.getSellerProfile(sellerId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Seller profile retrieved successfully",
                        response
                )
        );
    }

    @PutMapping("/seller/profile")
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateSellerProfile(
            @RequestAttribute("userId") String sellerId,
            @Valid @RequestBody SellerProfileUpdateRequestDTO request) {

        UserResponseDTO response =
                userService.updateSellerProfile(
                        sellerId,
                        request
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Seller profile updated successfully",
                        response
                )
        );
    }

    @PostMapping("/seller/address")
    public ResponseEntity<ApiResponse<UserResponseDTO>> addSellerAddress(
            @RequestAttribute("userId") String sellerId,
            @Valid @RequestBody AddressRequestDTO request) {

        UserResponseDTO response =
                userService.addSellerAddress(
                        sellerId,
                        request
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Seller address added successfully",
                        response
                )
        );
    }

    @DeleteMapping("/seller/address/{addressId}")
    public ResponseEntity<ApiResponse<Void>> removeSellerAddress(
            @RequestAttribute("userId") String sellerId,
            @PathVariable String addressId) {

        userService.removeSellerAddress(
                sellerId,
                addressId
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Address removed successfully."
                )
        );
    }
}