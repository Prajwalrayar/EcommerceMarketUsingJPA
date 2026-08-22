package com.crimsonlogic.ecommerce.controller;

import com.crimsonlogic.ecommerce.dto.inventory.InventoryResponseDTO;
import com.crimsonlogic.ecommerce.dto.inventory.InventoryUpdateRequestDTO;
import com.crimsonlogic.ecommerce.service.impl.InventoryServiceImpl;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryServiceImpl inventoryService;

    public InventoryController(InventoryServiceImpl inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public ResponseEntity<List<InventoryResponseDTO>> getMyInventory(
            @RequestAttribute("userId") String userId,
            @RequestAttribute("role") String role) {

        if ("CUSTOMER".equals(role)) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(inventoryService.getInventoryByRole(userId, role));
    }

    // Route 1: Update by ID
    @PutMapping("/product/{productId}")
    public ResponseEntity<InventoryResponseDTO> updateStockById(
            @PathVariable String productId,
            @Valid @RequestBody InventoryUpdateRequestDTO request,
            @RequestAttribute("userId") String userId,
            @RequestAttribute("role") String role) {

        if ("CUSTOMER".equals(role)) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(inventoryService.updateQuantityById(productId, request, userId, role));
    }

    // Route 2: Update by Name (Notice the added "/name/" in the URL)
    @PutMapping("/product/name/{productName}")
    public ResponseEntity<InventoryResponseDTO> updateStockByName(
            @PathVariable String productName,
            @Valid @RequestBody InventoryUpdateRequestDTO request,
            @RequestAttribute("userId") String userId,
            @RequestAttribute("role") String role) {

        if ("CUSTOMER".equals(role)) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(inventoryService.updateQuantityByName(productName, request, userId, role));
    }
}