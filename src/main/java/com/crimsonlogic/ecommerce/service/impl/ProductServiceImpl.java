package com.crimsonlogic.ecommerce.service.impl;

import com.crimsonlogic.ecommerce.dto.product.ProductRequestDTO;
import com.crimsonlogic.ecommerce.dto.product.ProductResponseDTO;
import com.crimsonlogic.ecommerce.entity.Category;
import com.crimsonlogic.ecommerce.entity.Inventory;
import com.crimsonlogic.ecommerce.entity.Product;
import com.crimsonlogic.ecommerce.entity.Seller;
import com.crimsonlogic.ecommerce.enumeration.ProductStatus;
import com.crimsonlogic.ecommerce.exception.ValidationException;
import com.crimsonlogic.ecommerce.repository.CategoryRepository;
import com.crimsonlogic.ecommerce.repository.InventoryRepository;
import com.crimsonlogic.ecommerce.repository.ProductRepository;
import com.crimsonlogic.ecommerce.repository.SellerRepository;
import com.crimsonlogic.ecommerce.util.IdGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SellerRepository sellerRepository;
    private final InventoryRepository inventoryRepository; // Used directly to prevent circular dependency

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository,
                              SellerRepository sellerRepository, InventoryRepository inventoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.sellerRepository = sellerRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public ProductResponseDTO addProduct(ProductRequestDTO request, String userId, String role) {
        Category category = categoryRepository.findByName(request.getCategoryName())
                .orElseThrow(() -> new ValidationException("Category '" + request.getCategoryName() + "' not found."));

        Product product = new Product();
        product.setId(IdGenerator.generateId("PRO"));
        product.setName(request.getName());
        product.setBrand(request.getBrand());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setCategory(category);
        product.setCreatedBy(userId);

        // Since quantity must be >= 1, the product is immediately available
        product.setStatus(ProductStatus.AVAILABLE);

        if ("SELLER".equals(role)) {
            Seller seller = sellerRepository.findById(userId)
                    .orElseThrow(() -> new ValidationException("Seller not found."));
            product.setSeller(seller);
        }

        Product savedProduct = productRepository.save(product);

        // Initialize Inventory using the exact quantity passed in Postman
        Inventory inventory = new Inventory();
        inventory.setId(IdGenerator.generateId("INV"));
        inventory.setProduct(savedProduct);
        inventory.setQuantity(request.getQuantity());
        inventoryRepository.save(inventory);

        return mapToDTO(savedProduct);
    }

    public ProductResponseDTO updateProduct(String productId, ProductRequestDTO request, String userId, String role) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ValidationException("Product not found."));

        if ("SELLER".equals(role) && (product.getSeller() == null || !product.getSeller().getId().equals(userId))) {
            throw new ValidationException("You can only modify your own products.");
        }
        if ("ADMIN".equals(role) && product.getSeller() != null) {
            throw new ValidationException("Admins can only edit admin-created products.");
        }

        Category category = categoryRepository.findByName(request.getCategoryName())
                .orElseThrow(() -> new ValidationException("Category '" + request.getCategoryName() + "' not found."));

        product.setName(request.getName());
        product.setBrand(request.getBrand());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setCategory(category);

        return mapToDTO(productRepository.save(product));
    }

    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<ProductResponseDTO> getProductsBySeller(String sellerId) {
        return productRepository.findBySellerId(sellerId).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private ProductResponseDTO mapToDTO(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setBrand(product.getBrand());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStatus(product.getStatus() != null ? product.getStatus().name() : "N/A");
        dto.setCategoryName(product.getCategory().getName());
        dto.setSellerName(product.getSeller() != null ? product.getSeller().getShopName() : "Admin");
        return dto;
    }

    public String deleteProduct(String productId, String userId, String role) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ValidationException("Product not found."));

        // 1. Verify Ownership
        if ("SELLER".equals(role) && (product.getSeller() == null || !product.getSeller().getId().equals(userId))) {
            throw new ValidationException("You can only delete your own products.");
        }
        if ("ADMIN".equals(role) && product.getSeller() != null) {
            throw new ValidationException("Admins can only delete admin-created products.");
        }

        // 2. Delete Associated Inventory First (to prevent Foreign Key constraint crash)
        inventoryRepository.findByProductId(productId).ifPresent(inventory ->
                inventoryRepository.delete(inventory)
        );

        // 3. Delete the Product
        // Note: If this product is already inside a customer's Cart or Order, deleting it
        // will crash unless you also clear those carts/orders first!
        productRepository.delete(product);

        return "Product deleted successfully.";
    }
}