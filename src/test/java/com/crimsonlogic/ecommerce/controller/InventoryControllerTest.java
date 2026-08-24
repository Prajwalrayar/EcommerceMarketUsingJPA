package com.crimsonlogic.ecommerce.controller;

import com.crimsonlogic.ecommerce.dto.inventory.InventoryResponseDTO;
import com.crimsonlogic.ecommerce.dto.inventory.InventoryUpdateRequestDTO;
import com.crimsonlogic.ecommerce.service.InventoryService;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class InventoryControllerTest {

    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    private InventoryController inventoryController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(inventoryController)
                .build();
    }

    // ==========================================================
    // GET INVENTORY
    // ==========================================================

    @Test
    void shouldGetInventoryForSeller()
            throws Exception {

        List<InventoryResponseDTO> inventory =
                Collections.emptyList();

        when(inventoryService.getInventoryByRole(
                "SEL001",
                "SELLER"
        )).thenReturn(inventory);

        mockMvc.perform(
                        get("/api/v1/inventory")
                                .requestAttr("userId", "SEL001")
                                .requestAttr("role", "SELLER")
                )
                .andExpect(status().isOk());

        verify(inventoryService)
                .getInventoryByRole(
                        "SEL001",
                        "SELLER"
                );
    }

    @Test
    void shouldGetInventoryForAdmin()
            throws Exception {

        List<InventoryResponseDTO> inventory =
                Collections.emptyList();

        when(inventoryService.getInventoryByRole(
                "ADM001",
                "ADMIN"
        )).thenReturn(inventory);

        mockMvc.perform(
                        get("/api/v1/inventory")
                                .requestAttr("userId", "ADM001")
                                .requestAttr("role", "ADMIN")
                )
                .andExpect(status().isOk());

        verify(inventoryService)
                .getInventoryByRole(
                        "ADM001",
                        "ADMIN"
                );
    }

    @Test
    void shouldRejectGetInventoryForCustomer()
            throws Exception {

        mockMvc.perform(
                        get("/api/v1/inventory")
                                .requestAttr("userId", "CUS001")
                                .requestAttr("role", "CUSTOMER")
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(inventoryService);
    }

    // ==========================================================
    // UPDATE STOCK BY PRODUCT ID
    // ==========================================================

    @Test
    void shouldUpdateStockByProductIdForSeller()
            throws Exception {

        InventoryResponseDTO response =
                new InventoryResponseDTO();

        when(inventoryService.updateQuantityById(
                eq("PROD001"),
                any(InventoryUpdateRequestDTO.class),
                eq("SEL001"),
                eq("SELLER")
        )).thenReturn(response);

        String requestJson = """
                {
                    "quantity": 50
                }
                """;

        mockMvc.perform(
                        put("/api/v1/inventory/product/PROD001")
                                .requestAttr("userId", "SEL001")
                                .requestAttr("role", "SELLER")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isOk());

        verify(inventoryService)
                .updateQuantityById(
                        eq("PROD001"),
                        any(InventoryUpdateRequestDTO.class),
                        eq("SEL001"),
                        eq("SELLER")
                );
    }

    @Test
    void shouldUpdateStockByProductIdForAdmin()
            throws Exception {

        InventoryResponseDTO response =
                new InventoryResponseDTO();

        when(inventoryService.updateQuantityById(
                eq("PROD001"),
                any(InventoryUpdateRequestDTO.class),
                eq("ADM001"),
                eq("ADMIN")
        )).thenReturn(response);

        String requestJson = """
                {
                    "quantity": 50
                }
                """;

        mockMvc.perform(
                        put("/api/v1/inventory/product/PROD001")
                                .requestAttr("userId", "ADM001")
                                .requestAttr("role", "ADMIN")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isOk());

        verify(inventoryService)
                .updateQuantityById(
                        eq("PROD001"),
                        any(InventoryUpdateRequestDTO.class),
                        eq("ADM001"),
                        eq("ADMIN")
                );
    }

    @Test
    void shouldRejectUpdateStockByProductIdForCustomer()
            throws Exception {

        String requestJson = """
                {
                    "quantity": 50
                }
                """;

        mockMvc.perform(
                        put("/api/v1/inventory/product/PROD001")
                                .requestAttr("userId", "CUS001")
                                .requestAttr("role", "CUSTOMER")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(inventoryService);
    }

    // ==========================================================
    // UPDATE STOCK BY PRODUCT NAME
    // ==========================================================

    @Test
    void shouldUpdateStockByProductNameForSeller()
            throws Exception {

        InventoryResponseDTO response =
                new InventoryResponseDTO();

        when(inventoryService.updateQuantityByName(
                eq("Laptop"),
                any(InventoryUpdateRequestDTO.class),
                eq("SEL001"),
                eq("SELLER")
        )).thenReturn(response);

        String requestJson = """
                {
                    "quantity": 50
                }
                """;

        mockMvc.perform(
                        put("/api/v1/inventory/product/name/Laptop")
                                .requestAttr("userId", "SEL001")
                                .requestAttr("role", "SELLER")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isOk());

        verify(inventoryService)
                .updateQuantityByName(
                        eq("Laptop"),
                        any(InventoryUpdateRequestDTO.class),
                        eq("SEL001"),
                        eq("SELLER")
                );
    }

    @Test
    void shouldUpdateStockByProductNameForAdmin()
            throws Exception {

        InventoryResponseDTO response =
                new InventoryResponseDTO();

        when(inventoryService.updateQuantityByName(
                eq("Laptop"),
                any(InventoryUpdateRequestDTO.class),
                eq("ADM001"),
                eq("ADMIN")
        )).thenReturn(response);

        String requestJson = """
                {
                    "quantity": 50
                }
                """;

        mockMvc.perform(
                        put("/api/v1/inventory/product/name/Laptop")
                                .requestAttr("userId", "ADM001")
                                .requestAttr("role", "ADMIN")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isOk());

        verify(inventoryService)
                .updateQuantityByName(
                        eq("Laptop"),
                        any(InventoryUpdateRequestDTO.class),
                        eq("ADM001"),
                        eq("ADMIN")
                );
    }

    @Test
    void shouldRejectUpdateStockByProductNameForCustomer()
            throws Exception {

        String requestJson = """
                {
                    "quantity": 50
                }
                """;

        mockMvc.perform(
                        put("/api/v1/inventory/product/name/Laptop")
                                .requestAttr("userId", "CUS001")
                                .requestAttr("role", "CUSTOMER")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(inventoryService);
    }
}