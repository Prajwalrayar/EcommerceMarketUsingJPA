package com.crimsonlogic.ecommerce.controller;

import com.crimsonlogic.ecommerce.dto.auth.CustomerRegistrationRequestDTO;
import com.crimsonlogic.ecommerce.dto.auth.LoginRequestDTO;
import com.crimsonlogic.ecommerce.dto.auth.LoginResponseDTO;
import com.crimsonlogic.ecommerce.dto.auth.SellerRegistrationRequestDTO;
import com.crimsonlogic.ecommerce.handler.GlobalExceptionHandler;
import com.crimsonlogic.ecommerce.service.AuthenticationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(authenticationController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    // ==========================================================
    // CUSTOMER REGISTRATION
    // ==========================================================

    @Test
    void shouldRegisterCustomerSuccessfully() throws Exception {

        LoginResponseDTO response = new LoginResponseDTO();

        when(authenticationService.registerCustomer(
                any(CustomerRegistrationRequestDTO.class)
        )).thenReturn(response);

        String requestJson = """
                {
                    "name": "Prajwal",
                    "email": "prajwal@gmail.com",
                    "phone": "9876543210",
                    "password": "Password@123"
                }
                """;

        mockMvc.perform(
                        post("/api/v1/auth/customer/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isCreated());

        verify(authenticationService)
                .registerCustomer(
                        any(CustomerRegistrationRequestDTO.class)
                );
    }

    // ==========================================================
    // SELLER REGISTRATION
    // ==========================================================

    @Test
    void shouldRegisterSellerSuccessfully() throws Exception {

        LoginResponseDTO response = new LoginResponseDTO();

        when(authenticationService.registerSeller(
                any(SellerRegistrationRequestDTO.class)
        )).thenReturn(response);

        String requestJson = """
                {
                    "name": "Prajwal",
                    "email": "seller@gmail.com",
                    "phone": "9876543211",
                    "password": "Password@23",
                    "shopName": "Sham Electronics",
                    "address": {
                        "houseNumber": "25B",
                        "street": "ITPL Road",
                        "city": "Bengaluru",
                        "state": "Karnataka",
                        "country": "India",
                        "zipCode": "560066"
                    }
                }
                """;

        mockMvc.perform(
                        post("/api/v1/auth/seller/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isCreated());

        verify(authenticationService)
                .registerSeller(
                        any(SellerRegistrationRequestDTO.class)
                );
    }

    // ==========================================================
    // INVALID SELLER REGISTRATION
    // ==========================================================

    @Test
    void shouldRejectSellerRegistrationWhenAddressIsMissing()
            throws Exception {

        String requestJson = """
            {
                "name": "Prajwal",
                "email": "seller@gmail.com",
                "phone": "9876543211",
                "password": "Password@23",
                "shopName": "Sham Electronics"
            }
            """;

        String response = mockMvc.perform(
                        post("/api/v1/auth/seller/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        System.out.println("Response: " + response);

        org.junit.jupiter.api.Assertions.assertTrue(
                response.contains("Validation failed")
        );

        org.junit.jupiter.api.Assertions.assertTrue(
                response.contains("Address is required.")
        );

        verifyNoInteractions(authenticationService);
    }

    // ==========================================================
    // CUSTOMER LOGIN
    // ==========================================================

    @Test
    void shouldLoginCustomerSuccessfully() throws Exception {

        LoginResponseDTO response = new LoginResponseDTO();

        response.setId("CUS001");
        response.setRole("CUSTOMER");

        when(authenticationService.loginCustomer(
                any(LoginRequestDTO.class)
        )).thenReturn(response);

        String requestJson = """
                {
                    "email": "prajwal@gmail.com",
                    "password": "password123"
                }
                """;

        mockMvc.perform(
                        post("/api/v1/auth/customer/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isOk())
                .andExpect(request()
                        .sessionAttribute("userId", "CUS001"))
                .andExpect(request()
                        .sessionAttribute("role", "CUSTOMER"));

        verify(authenticationService)
                .loginCustomer(
                        any(LoginRequestDTO.class)
                );
    }

    // ==========================================================
    // SELLER LOGIN
    // ==========================================================

    @Test
    void shouldLoginSellerSuccessfully() throws Exception {

        LoginResponseDTO response = new LoginResponseDTO();

        response.setId("SEL001");
        response.setRole("SELLER");

        when(authenticationService.loginSeller(
                any(LoginRequestDTO.class)
        )).thenReturn(response);

        String requestJson = """
                {
                    "email": "seller@gmail.com",
                    "password": "password123"
                }
                """;

        mockMvc.perform(
                        post("/api/v1/auth/seller/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isOk())
                .andExpect(request()
                        .sessionAttribute("userId", "SEL001"))
                .andExpect(request()
                        .sessionAttribute("role", "SELLER"));

        verify(authenticationService)
                .loginSeller(
                        any(LoginRequestDTO.class)
                );
    }

    // ==========================================================
    // ADMIN LOGIN
    // ==========================================================

    @Test
    void shouldLoginAdminSuccessfully() throws Exception {

        LoginResponseDTO response = new LoginResponseDTO();

        response.setId("ADM001");
        response.setRole("ADMIN");

        when(authenticationService.loginAdmin(
                any(LoginRequestDTO.class)
        )).thenReturn(response);

        String requestJson = """
                {
                    "email": "admin@gmail.com",
                    "password": "admin123"
                }
                """;

        mockMvc.perform(
                        post("/api/v1/auth/admin/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isOk())
                .andExpect(request()
                        .sessionAttribute("userId", "ADM001"))
                .andExpect(request()
                        .sessionAttribute("role", "ADMIN"));

        verify(authenticationService)
                .loginAdmin(
                        any(LoginRequestDTO.class)
                );
    }

    // ==========================================================
    // LOGOUT
    // ==========================================================

    @Test
    void shouldLogoutSuccessfully() throws Exception {

        MockHttpSession session = new MockHttpSession();

        session.setAttribute("userId", "CUS001");
        session.setAttribute("role", "CUSTOMER");

        mockMvc.perform(
                        post("/api/v1/auth/logout")
                                .session(session)
                )
                .andExpect(status().isOk());

        org.junit.jupiter.api.Assertions.assertTrue(
                session.isInvalid()
        );
    }
}