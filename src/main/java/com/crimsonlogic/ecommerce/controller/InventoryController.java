package com.crimsonlogic.ecommerce.controller;

import com.crimsonlogic.ecommerce.dto.inventory.InventoryResponseDTO;
import com.crimsonlogic.ecommerce.dto.inventory.InventoryUpdateRequestDTO;
import com.crimsonlogic.ecommerce.handler.ApiResponse;
import com.crimsonlogic.ecommerce.service.impl.InventoryServiceImpl;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {

    private final InventoryServiceImpl inventoryService;

    public InventoryController(InventoryServiceImpl inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<InventoryResponseDTO>>> getMyInventory(
            @RequestAttribute("userId") String userId,
            @RequestAttribute("role") String role) {

        if ("CUSTOMER".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(
                            "Customers are not allowed to access inventory."
                    ));
        }

        List<InventoryResponseDTO> inventory =
                inventoryService.getInventoryByRole(userId, role);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Inventory retrieved successfully",
                        inventory
                )
        );
    }

    // Route 1: Update by ID
    @PutMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<InventoryResponseDTO>> updateStockById(
            @PathVariable String productId,
            @Valid @RequestBody InventoryUpdateRequestDTO request,
            @RequestAttribute("userId") String userId,
            @RequestAttribute("role") String role) {

        if ("CUSTOMER".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(
                            "Customers are not allowed to update inventory."
                    ));
        }

        InventoryResponseDTO response =
                inventoryService.updateQuantityById(
                        productId,
                        request,
                        userId,
                        role
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Inventory updated successfully",
                        response
                )
        );
    }

    // Route 2: Update by Name
    @PutMapping("/product/name/{productName}")
    public ResponseEntity<ApiResponse<InventoryResponseDTO>> updateStockByName(
            @PathVariable String productName,
            @Valid @RequestBody InventoryUpdateRequestDTO request,
            @RequestAttribute("userId") String userId,
            @RequestAttribute("role") String role) {

        if ("CUSTOMER".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(
                            "Customers are not allowed to update inventory."
                    ));
        }

        InventoryResponseDTO response =
                inventoryService.updateQuantityByName(
                        productName,
                        request,
                        userId,
                        role
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Inventory updated successfully",
                        response
                )
        );
    }
}