package com.crimsonlogic.ecommerce.service;

import com.crimsonlogic.ecommerce.dto.product.ProductRequestDTO;
import com.crimsonlogic.ecommerce.dto.product.ProductResponseDTO;
import com.crimsonlogic.ecommerce.dto.product.ProductUpdateRequestDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    ProductResponseDTO addProduct(
            ProductRequestDTO request,
            String userId,
            String role
    );

    ProductResponseDTO updateProduct(
            String productId,
            ProductUpdateRequestDTO request,
            String userId,
            String role
    );


    List<ProductResponseDTO> getAllProducts();

    List<ProductResponseDTO> getProductsBySeller(String sellerId);

    String deleteProduct(
            String productId,
            String userId,
            String role
    );

    Page<ProductResponseDTO> getAllProductsPaginated(
            int page,
            int size,
            String sortBy
    );

    List<ProductResponseDTO> searchProducts(String keyword);

    Page<ProductResponseDTO> filterProducts(
            String keyword,
            String categoryName,
            Double minPrice,
            Double maxPrice,
            int page,
            int size,
            String sortBy
    );
}