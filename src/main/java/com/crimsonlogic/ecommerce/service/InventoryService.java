package com.crimsonlogic.ecommerce.service;

import com.crimsonlogic.ecommerce.dto.inventory.InventoryResponseDTO;
import com.crimsonlogic.ecommerce.dto.inventory.InventoryUpdateRequestDTO;

import java.util.List;

public interface InventoryService {

    InventoryResponseDTO updateQuantityById(
            String productId,
            InventoryUpdateRequestDTO request,
            String userId,
            String role
    );

    InventoryResponseDTO updateQuantityByName(
            String productName,
            InventoryUpdateRequestDTO request,
            String userId,
            String role
    );

    List<InventoryResponseDTO> getInventoryByRole(
            String userId,
            String role
    );
}