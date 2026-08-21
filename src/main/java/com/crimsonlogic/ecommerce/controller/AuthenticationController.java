package com.crimsonlogic.ecommerce.controller;

import com.crimsonlogic.ecommerce.dto.auth.CustomerRegistrationRequestDTO;
import com.crimsonlogic.ecommerce.dto.auth.LoginRequestDTO;
import com.crimsonlogic.ecommerce.dto.auth.LoginResponseDTO;
import com.crimsonlogic.ecommerce.dto.auth.SellerRegistrationRequestDTO;
import com.crimsonlogic.ecommerce.service.AuthenticationService;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/customer/register")
    public ResponseEntity<LoginResponseDTO> registerCustomer(
            @Valid @RequestBody CustomerRegistrationRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authenticationService.registerCustomer(request));
    }

    @PostMapping("/seller/register")
    public ResponseEntity<LoginResponseDTO> registerSeller(
            @Valid @RequestBody SellerRegistrationRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authenticationService.registerSeller(request));
    }

    @PostMapping("/customer/login")
    public ResponseEntity<LoginResponseDTO> loginCustomer(
            @Valid @RequestBody LoginRequestDTO request, HttpSession session) {
        LoginResponseDTO response = authenticationService.loginCustomer(request);
        setupSession(session, response);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/seller/login")
    public ResponseEntity<LoginResponseDTO> loginSeller(
            @Valid @RequestBody LoginRequestDTO request, HttpSession session) {
        LoginResponseDTO response = authenticationService.loginSeller(request);
        setupSession(session, response);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin/login")
    public ResponseEntity<LoginResponseDTO> loginAdmin(
            @Valid @RequestBody LoginRequestDTO request, HttpSession session) {
        LoginResponseDTO response = authenticationService.loginAdmin(request);
        setupSession(session, response);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logged out successfully.");
    }

    private void setupSession(HttpSession session, LoginResponseDTO response) {
        session.setAttribute("userId", response.getId());
        session.setAttribute("role", response.getRole());
    }
}