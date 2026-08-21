package com.crimsonlogic.ecommerce.controller;

import com.crimsonlogic.ecommerce.dto.address.AddressDTO;
import com.crimsonlogic.ecommerce.dto.address.ShopAddressDTO;
import com.crimsonlogic.ecommerce.dto.user.CustomerProfileUpdateRequestDTO;
import com.crimsonlogic.ecommerce.dto.user.SellerProfileUpdateRequestDTO;
import com.crimsonlogic.ecommerce.dto.user.UpdatePhoneRequestDTO;
import com.crimsonlogic.ecommerce.dto.user.UserResponseDTO;
import com.crimsonlogic.ecommerce.service.UserService;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserProfileController {

    private final UserService userService;

    public UserProfileController(UserService userService) {
        this.userService = userService;
    }

    // ==========================================================
    // ADMIN ENDPOINTS
    // ==========================================================
    @GetMapping("/admin/profile")
    public ResponseEntity<UserResponseDTO> getAdminProfile(@RequestAttribute("userId") String adminId) {
        return ResponseEntity.ok(userService.getAdminProfile(adminId));
    }

    @PutMapping("/admin/profile/phone")
    public ResponseEntity<UserResponseDTO> updateAdminPhone(
            @RequestAttribute("userId") String adminId,
            @Valid @RequestBody UpdatePhoneRequestDTO request) {
        return ResponseEntity.ok(userService.updateAdminPhone(adminId, request));
    }

    // ==========================================================
    // CUSTOMER ENDPOINTS
    // ==========================================================
    @GetMapping("/customer/profile")
    public ResponseEntity<UserResponseDTO> getCustomerProfile(@RequestAttribute("userId") String customerId) {
        return ResponseEntity.ok(userService.getCustomerProfile(customerId));
    }

    @PutMapping("/customer/profile")
    public ResponseEntity<UserResponseDTO> updateCustomerProfile(
            @RequestAttribute("userId") String customerId,
            @Valid @RequestBody CustomerProfileUpdateRequestDTO request) {
        return ResponseEntity.ok(userService.updateCustomerProfile(customerId, request));
    }

    @PostMapping("/customer/address")
    public ResponseEntity<UserResponseDTO> addCustomerAddress(
            @RequestAttribute("userId") String customerId,
            @Valid @RequestBody AddressDTO request) {
        return ResponseEntity.ok(userService.addCustomerAddress(customerId, request));
    }

    @DeleteMapping("/customer/address/{addressId}")
    public ResponseEntity<String> removeCustomerAddress(
            @RequestAttribute("userId") String customerId,
            @PathVariable String addressId) {
        userService.removeCustomerAddress(customerId, addressId);
        return ResponseEntity.ok("Address removed successfully.");
    }

    // ==========================================================
    // SELLER ENDPOINTS
    // ==========================================================
    @GetMapping("/seller/profile")
    public ResponseEntity<UserResponseDTO> getSellerProfile(@RequestAttribute("userId") String sellerId) {
        return ResponseEntity.ok(userService.getSellerProfile(sellerId));
    }

    @PutMapping("/seller/profile")
    public ResponseEntity<UserResponseDTO> updateSellerProfile(
            @RequestAttribute("userId") String sellerId,
            @Valid @RequestBody SellerProfileUpdateRequestDTO request) {
        return ResponseEntity.ok(userService.updateSellerProfile(sellerId, request));
    }

    @PostMapping("/seller/address")
    public ResponseEntity<UserResponseDTO> addSellerAddress(
            @RequestAttribute("userId") String sellerId,
            @Valid @RequestBody ShopAddressDTO request) {
        return ResponseEntity.ok(userService.addSellerAddress(sellerId, request));
    }

    @DeleteMapping("/seller/address/{addressId}")
    public ResponseEntity<String> removeSellerAddress(
            @RequestAttribute("userId") String sellerId,
            @PathVariable String addressId) {
        userService.removeSellerAddress(sellerId, addressId);
        return ResponseEntity.ok("Address removed successfully.");
    }
}