package com.crimsonlogic.ecommerce.controller;

import com.crimsonlogic.ecommerce.dto.order.CheckoutRequestDTO;
import com.crimsonlogic.ecommerce.service.CheckoutService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CheckoutControllerTest {

    @Mock
    private CheckoutService checkoutService;

    @InjectMocks
    private CheckoutController checkoutController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(checkoutController)
                .build();
    }

    // ==========================================================
    // SUCCESSFUL CHECKOUT
    // ==========================================================

    @Test
    void shouldProcessCheckoutSuccessfully()
            throws Exception {

        when(checkoutService.checkout(
                eq("CUS001"),
                any(CheckoutRequestDTO.class)
        )).thenReturn("Order placed successfully");

        String requestJson = """
                {
                    "cartId": "CART001",
                    "paymentMethod": "UPI"
                }
                """;

        mockMvc.perform(
                        post("/api/v1/customer/checkout")
                                .requestAttr("userId", "CUS001")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isOk());

        verify(checkoutService)
                .checkout(
                        eq("CUS001"),
                        any(CheckoutRequestDTO.class)
                );
    }

    // ==========================================================
    // VALIDATION FAILURE
    // ==========================================================

    @Test
    void shouldRejectCheckoutWhenRequestIsInvalid()
            throws Exception {

        String requestJson = """
                {
                }
                """;

        mockMvc.perform(
                        post("/api/v1/customer/checkout")
                                .requestAttr("userId", "CUS001")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(checkoutService);
    }

    // ==========================================================
    // MISSING USER ID
    // ==========================================================

    @Test
    void shouldRejectCheckoutWhenUserIdIsMissing()
            throws Exception {

        String requestJson = """
                {
                    "cartId": "CART001"
                }
                """;

        mockMvc.perform(
                        post("/api/v1/customer/checkout")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(checkoutService);
    }
}