package com.crimsonlogic.ecommerce.controller;

import com.crimsonlogic.ecommerce.dto.product.ProductRequestDTO;
import com.crimsonlogic.ecommerce.dto.product.ProductResponseDTO;
import com.crimsonlogic.ecommerce.handler.ApiResponse;
import com.crimsonlogic.ecommerce.service.ProductService;
import com.crimsonlogic.ecommerce.service.impl.ProductServiceImpl;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Admins and Sellers can add products
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponseDTO>> addProduct(
            @Valid @RequestBody ProductRequestDTO request,
            @RequestAttribute("userId") String userId,
            @RequestAttribute("role") String role) {

        ProductResponseDTO response =
                productService.addProduct(request, userId, role);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Product added successfully",
                        response
                ));
    }

    // Admins and Sellers can edit their own products
    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductResponseDTO>> updateProduct(
            @PathVariable String productId,
            @Valid @RequestBody ProductRequestDTO request,
            @RequestAttribute("userId") String userId,
            @RequestAttribute("role") String role) {

        ProductResponseDTO response =
                productService.updateProduct(
                        productId,
                        request,
                        userId,
                        role
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Product updated successfully",
                        response
                )
        );
    }

    // Public/Customer viewing
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponseDTO>>> getAllProducts() {

        List<ProductResponseDTO> products =
                productService.getAllProducts();

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Products retrieved successfully",
                        products
                )
        );
    }

    // View seller-specific products
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<ApiResponse<List<ProductResponseDTO>>> getSellerProducts(
            @PathVariable String sellerId) {

        List<ProductResponseDTO> products =
                productService.getProductsBySeller(sellerId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Seller products retrieved successfully",
                        products
                )
        );
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<ApiResponse<String>> deleteProduct(
            @PathVariable String productId,
            @RequestAttribute("userId") String userId,
            @RequestAttribute("role") String role) {

        String response =
                productService.deleteProduct(
                        productId,
                        userId,
                        role
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Product deleted successfully",
                        response
                )
        );
    }

    // Sellers can view only their own products
    @GetMapping("/my-products")
    public ResponseEntity<ApiResponse<List<ProductResponseDTO>>> getMyProducts(
            @RequestAttribute("userId") String userId,
            @RequestAttribute("role") String role) {

        if (!"SELLER".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(
                            "Only sellers can view their own products."
                    ));
        }

        List<ProductResponseDTO> products =
                productService.getProductsBySeller(userId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Your products retrieved successfully",
                        products
                )
        );
    }

    // Paginated product browsing
    // Example: /api/products/paged?page=0&size=10&sortBy=price
    @GetMapping("/paged")
    public ResponseEntity<ApiResponse<Page<ProductResponseDTO>>> getProductsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy) {

        Page<ProductResponseDTO> products =
                productService.getAllProductsPaginated(
                        page,
                        size,
                        sortBy
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Products retrieved successfully",
                        products
                )
        );
    }

    // Search products by keyword
    // Example: /api/products/search?keyword=mouse
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ProductResponseDTO>>> searchProducts(
            @RequestParam String keyword) {

        List<ProductResponseDTO> products =
                productService.searchProducts(keyword);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Products search completed successfully",
                        products
                )
        );
    }

    // Advanced search, category filter, price range, and pagination endpoint
    // Example:
    // /api/products/filter?keyword=mouse&categoryName=Electronics
    // &minPrice=500&maxPrice=2000&page=0&size=10&sortBy=price
    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<Page<ProductResponseDTO>>> filterProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy) {

        Page<ProductResponseDTO> products =
                productService.filterProducts(
                        keyword,
                        categoryName,
                        minPrice,
                        maxPrice,
                        page,
                        size,
                        sortBy
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Products filtered successfully",
                        products
                )
        );
    }
}