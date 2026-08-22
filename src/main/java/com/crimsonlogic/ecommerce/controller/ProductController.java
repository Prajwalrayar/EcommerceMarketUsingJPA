package com.crimsonlogic.ecommerce.controller;

import com.crimsonlogic.ecommerce.dto.product.ProductRequestDTO;
import com.crimsonlogic.ecommerce.dto.product.ProductResponseDTO;
import com.crimsonlogic.ecommerce.service.impl.ProductServiceImpl;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductServiceImpl productService;

    public ProductController(ProductServiceImpl productService) {
        this.productService = productService;
    }

    // Admins and Sellers can add products
    @PostMapping
    public ResponseEntity<ProductResponseDTO> addProduct(
            @Valid @RequestBody ProductRequestDTO request,
            @RequestAttribute("userId") String userId,
            @RequestAttribute("role") String role) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.addProduct(request, userId, role));
    }

    // Admins and Sellers can edit their own products
    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable String productId,
            @Valid @RequestBody ProductRequestDTO request,
            @RequestAttribute("userId") String userId,
            @RequestAttribute("role") String role) {
        return ResponseEntity.ok(productService.updateProduct(productId, request, userId, role));
    }

    // Public/Customer viewing
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // View seller-specific products
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<ProductResponseDTO>> getSellerProducts(@PathVariable String sellerId) {
        return ResponseEntity.ok(productService.getProductsBySeller(sellerId));
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<String> deleteProduct(
            @PathVariable String productId,
            @RequestAttribute("userId") String userId,
            @RequestAttribute("role") String role) {
        return ResponseEntity.ok(productService.deleteProduct(productId, userId, role));
    }

    // . Sellers can view only their own products
    @GetMapping("/my-products")
    public ResponseEntity<List<ProductResponseDTO>> getMyProducts(
            @RequestAttribute("userId") String userId,
            @RequestAttribute("role") String role) {

        if (!"SELLER".equals(role)) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(productService.getProductsBySeller(userId));
    }
}