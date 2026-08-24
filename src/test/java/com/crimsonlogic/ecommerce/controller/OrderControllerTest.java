package com.crimsonlogic.ecommerce.controller;

import com.crimsonlogic.ecommerce.dto.order.CheckoutRequestDTO;
import com.crimsonlogic.ecommerce.dto.order.OrderResponseDTO;
import com.crimsonlogic.ecommerce.dto.order.OrderStatusUpdateRequestDTO;
import com.crimsonlogic.ecommerce.service.OrderService;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private MockMvc mockMvc;


    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(orderController)
                .build();
    }


    // ==========================================================
    // CHECKOUT
    // ==========================================================

    @Test
    void shouldCheckoutSuccessfullyForCustomer()
            throws Exception {

        when(orderService.checkout(
                eq("CUS001"),
                any(CheckoutRequestDTO.class)
        )).thenReturn("Order placed successfully");

        String requestJson = """
                {
                    "paymentMethod": "UPI"
                }
                """;

        mockMvc.perform(
                        post("/api/v1/orders/checkout")
                                .requestAttr("userId", "CUS001")
                                .requestAttr("role", "CUSTOMER")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isOk());

        verify(orderService)
                .checkout(
                        eq("CUS001"),
                        any(CheckoutRequestDTO.class)
                );
    }


    @Test
    void shouldRejectCheckoutForSeller()
            throws Exception {

        String requestJson = """
                {
                    "paymentMethod": "UPI"
                }
                """;

        mockMvc.perform(
                        post("/api/v1/orders/checkout")
                                .requestAttr("userId", "SEL001")
                                .requestAttr("role", "SELLER")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(orderService);
    }


    @Test
    void shouldRejectCheckoutForAdmin()
            throws Exception {

        String requestJson = """
                {
                    "paymentMethod": "UPI"
                }
                """;

        mockMvc.perform(
                        post("/api/v1/orders/checkout")
                                .requestAttr("userId", "ADM001")
                                .requestAttr("role", "ADMIN")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(orderService);
    }


    // ==========================================================
    // UPDATE ORDER STATUS
    // ==========================================================

    @Test
    void shouldUpdateOrderStatusSuccessfully()
            throws Exception {

        when(orderService.updateOrderStatus(
                eq("ORD001"),
                any(),
                eq("SEL001"),
                eq("SELLER")
        )).thenReturn("Order status updated successfully");

        String requestJson = """
                {
                    "status": "SHIPPED"
                }
                """;

        mockMvc.perform(
                        put("/api/v1/orders/ORD001/status")
                                .requestAttr("userId", "SEL001")
                                .requestAttr("role", "SELLER")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isOk());

        verify(orderService)
                .updateOrderStatus(
                        eq("ORD001"),
                        any(),
                        eq("SEL001"),
                        eq("SELLER")
                );
    }


    // ==========================================================
    // CANCEL ORDER
    // ==========================================================

    @Test
    void shouldCancelOrderSuccessfullyForCustomer()
            throws Exception {

        when(orderService.cancelOrder(
                "ORD001",
                "CUS001"
        )).thenReturn("Order cancelled successfully");

        mockMvc.perform(
                        put("/api/v1/orders/ORD001/cancel")
                                .requestAttr("userId", "CUS001")
                                .requestAttr("role", "CUSTOMER")
                )
                .andExpect(status().isOk());

        verify(orderService)
                .cancelOrder(
                        "ORD001",
                        "CUS001"
                );
    }


    @Test
    void shouldRejectCancelOrderForSeller()
            throws Exception {

        mockMvc.perform(
                        put("/api/v1/orders/ORD001/cancel")
                                .requestAttr("userId", "SEL001")
                                .requestAttr("role", "SELLER")
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(orderService);
    }


    @Test
    void shouldRejectCancelOrderForAdmin()
            throws Exception {

        mockMvc.perform(
                        put("/api/v1/orders/ORD001/cancel")
                                .requestAttr("userId", "ADM001")
                                .requestAttr("role", "ADMIN")
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(orderService);
    }


    // ==========================================================
    // ORDER HISTORY
    // ==========================================================

    @Test
    void shouldGetOrderHistorySuccessfully()
            throws Exception {

        List<OrderResponseDTO> orders =
                Collections.emptyList();

        when(orderService.getMyOrders(
                "CUS001",
                "CUSTOMER"
        )).thenReturn(orders);

        mockMvc.perform(
                        get("/api/v1/orders/history")
                                .requestAttr("userId", "CUS001")
                                .requestAttr("role", "CUSTOMER")
                )
                .andExpect(status().isOk());

        verify(orderService)
                .getMyOrders(
                        "CUS001",
                        "CUSTOMER"
                );
    }


    // ==========================================================
    // REQUEST RETURN
    // ==========================================================

    @Test
    void shouldRequestReturnSuccessfullyForCustomer()
            throws Exception {

        when(orderService.requestReturn(
                "ORD001",
                "CUS001"
        )).thenReturn("Return requested successfully");

        mockMvc.perform(
                        put("/api/v1/orders/ORD001/return")
                                .requestAttr("userId", "CUS001")
                                .requestAttr("role", "CUSTOMER")
                )
                .andExpect(status().isOk());

        verify(orderService)
                .requestReturn(
                        "ORD001",
                        "CUS001"
                );
    }


    @Test
    void shouldRejectReturnRequestForSeller()
            throws Exception {

        mockMvc.perform(
                        put("/api/v1/orders/ORD001/return")
                                .requestAttr("userId", "SEL001")
                                .requestAttr("role", "SELLER")
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(orderService);
    }


    @Test
    void shouldRejectReturnRequestForAdmin()
            throws Exception {

        mockMvc.perform(
                        put("/api/v1/orders/ORD001/return")
                                .requestAttr("userId", "ADM001")
                                .requestAttr("role", "ADMIN")
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(orderService);
    }
}