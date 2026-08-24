package com.crimsonlogic.ecommerce.controller;

import com.crimsonlogic.ecommerce.dto.cart.CartRequestDTO;
import com.crimsonlogic.ecommerce.dto.cart.CartResponseDTO;
import com.crimsonlogic.ecommerce.service.CartService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CartControllerTest {

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(cartController)
                .build();
    }

    // ==========================================================
    // ADD TO CART
    // ==========================================================

    @Test
    void shouldAddProductToCartSuccessfullyForCustomer()
            throws Exception {

        when(cartService.addToCart(
                any(CartRequestDTO.class),
                eq("CUS001")
        )).thenReturn("Product added to cart");

        String requestJson = """
                {
                    "productId": "PROD001",
                    "productName":"Laptop",
                    "quantity": 2
                }
                """;

        mockMvc.perform(
                        post("/api/v1/customer/cart/add")
                                .requestAttr("userId", "CUS001")
                                .requestAttr("role", "CUSTOMER")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isOk());

        verify(cartService)
                .addToCart(
                        any(CartRequestDTO.class),
                        eq("CUS001")
                );
    }

    @Test
    void shouldRejectAddToCartForSeller()
            throws Exception {

        String requestJson = """
                {
                    "productId": "PROD001",
                    "productName":"Laptop",
                    "quantity": 2
                }
                """;

        mockMvc.perform(
                        post("/api/v1/customer/cart/add")
                                .requestAttr("userId", "SEL001")
                                .requestAttr("role", "SELLER")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(cartService);
    }

    @Test
    void shouldRejectAddToCartForAdmin()
            throws Exception {

        String requestJson = """
                {
                    "productId": "PROD001",
                    "productName":"Laptop",
                    "quantity": 2
                }
                """;

        mockMvc.perform(
                        post("/api/v1/customer/cart/add")
                                .requestAttr("userId", "ADM001")
                                .requestAttr("role", "ADMIN")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(cartService);
    }

    // ==========================================================
    // VIEW CART
    // ==========================================================

    @Test
    void shouldViewCartSuccessfullyForCustomer()
            throws Exception {

        List<CartResponseDTO> cart =
                Collections.emptyList();

        when(cartService.viewCart("CUS001"))
                .thenReturn(cart);

        mockMvc.perform(
                        get("/api/v1/customer/cart")
                                .requestAttr("userId", "CUS001")
                                .requestAttr("role", "CUSTOMER")
                )
                .andExpect(status().isOk());

        verify(cartService)
                .viewCart("CUS001");
    }

    @Test
    void shouldRejectViewCartForSeller()
            throws Exception {

        mockMvc.perform(
                        get("/api/v1/customer/cart")
                                .requestAttr("userId", "SEL001")
                                .requestAttr("role", "SELLER")
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(cartService);
    }

    @Test
    void shouldRejectViewCartForAdmin()
            throws Exception {

        mockMvc.perform(
                        get("/api/v1/customer/cart")
                                .requestAttr("userId", "ADM001")
                                .requestAttr("role", "ADMIN")
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(cartService);
    }

    // ==========================================================
    // REMOVE FROM CART
    // ==========================================================

    @Test
    void shouldRemoveProductFromCartSuccessfullyForCustomer()
            throws Exception {

        when(cartService.removeFromCart(
                "CART001",
                "CUS001"
        )).thenReturn("Product removed from cart");

        mockMvc.perform(
                        delete("/api/v1/customer/cart/remove/CART001")
                                .requestAttr("userId", "CUS001")
                                .requestAttr("role", "CUSTOMER")
                )
                .andExpect(status().isOk());

        verify(cartService)
                .removeFromCart(
                        "CART001",
                        "CUS001"
                );
    }

    @Test
    void shouldRejectRemoveFromCartForSeller()
            throws Exception {

        mockMvc.perform(
                        delete("/api/v1/customer/cart/remove/CART001")
                                .requestAttr("userId", "SEL001")
                                .requestAttr("role", "SELLER")
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(cartService);
    }

    @Test
    void shouldRejectRemoveFromCartForAdmin()
            throws Exception {

        mockMvc.perform(
                        delete("/api/v1/customer/cart/remove/CART001")
                                .requestAttr("userId", "ADM001")
                                .requestAttr("role", "ADMIN")
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(cartService);
    }
}