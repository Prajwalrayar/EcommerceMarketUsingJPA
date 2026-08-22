package com.crimsonlogic.ecommerce.service.impl;

import com.crimsonlogic.ecommerce.dto.inventory.InventoryResponseDTO;
import com.crimsonlogic.ecommerce.dto.inventory.InventoryUpdateRequestDTO;
import com.crimsonlogic.ecommerce.entity.Inventory;
import com.crimsonlogic.ecommerce.entity.Product;
import com.crimsonlogic.ecommerce.enumeration.ProductStatus;
import com.crimsonlogic.ecommerce.exception.ValidationException;
import com.crimsonlogic.ecommerce.repository.InventoryRepository;
import com.crimsonlogic.ecommerce.repository.ProductRepository;
import com.crimsonlogic.ecommerce.util.IdGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InventoryServiceImpl {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    public InventoryServiceImpl(InventoryRepository inventoryRepository, ProductRepository productRepository) {
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
    }

    // --- Entry point for ID-based updates ---
    public InventoryResponseDTO updateQuantityById(String productId, InventoryUpdateRequestDTO request, String userId, String role) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ValidationException("Product with ID '" + productId + "' not found."));
        return processInventoryUpdate(product, request, userId, role);
    }

    // --- Entry point for Name-based updates ---
    public InventoryResponseDTO updateQuantityByName(String productName, InventoryUpdateRequestDTO request, String userId, String role) {
        Product product = productRepository.findByName(productName)
                .orElseThrow(() -> new ValidationException("Product '" + productName + "' not found."));
        return processInventoryUpdate(product, request, userId, role);
    }

    // --- Core Logic Helper Method (Prevents duplicate code) ---
    private InventoryResponseDTO processInventoryUpdate(Product product, InventoryUpdateRequestDTO request, String userId, String role) {
        // 1. Authorization checks
        if ("SELLER".equals(role)) {
            if (product.getSeller() == null || !product.getSeller().getId().equals(userId)) {
                throw new ValidationException("You can only manage inventory for your own products.");
            }
        } else if ("ADMIN".equals(role)) {
            if (product.getSeller() != null) {
                throw new ValidationException("Admins can only manage inventory for admin-created products.");
            }
        }

        // 2. Fetch or Create Inventory
        Inventory inventory = inventoryRepository.findByProductId(product.getId())
                .orElseGet(() -> {
                    Inventory newInv = new Inventory();
                    newInv.setId(IdGenerator.generateId("INV"));
                    newInv.setProduct(product);
                    return newInv;
                });

        inventory.setQuantity(request.getQuantity());

        // 3. Automatically update the Product Status
        if (inventory.getQuantity() > 0) {
            product.setStatus(ProductStatus.AVAILABLE);
        } else {
            product.setStatus(ProductStatus.OUT_OF_STOCK);
        }
        productRepository.save(product);

        Inventory savedInventory = inventoryRepository.save(inventory);
        return mapToDTO(savedInventory);
    }

    public List<InventoryResponseDTO> getInventoryByRole(String userId, String role) {
        List<Inventory> inventoryList;
        if ("SELLER".equals(role)) {
            inventoryList = inventoryRepository.findByProductSellerId(userId);
        } else {
            inventoryList = inventoryRepository.findByProductSellerIsNull(); // Admin sees admin products
        }
        return inventoryList.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private InventoryResponseDTO mapToDTO(Inventory inventory) {
        InventoryResponseDTO dto = new InventoryResponseDTO();
        dto.setInventoryId(inventory.getId());
        dto.setProductId(inventory.getProduct().getId());
        dto.setProductName(inventory.getProduct().getName());
        dto.setQuantity(inventory.getQuantity());
        dto.setProductStatus(inventory.getProduct().getStatus().name());
        return dto;
    }
}