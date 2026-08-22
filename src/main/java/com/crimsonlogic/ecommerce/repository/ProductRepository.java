package com.crimsonlogic.ecommerce.repository;

import com.crimsonlogic.ecommerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, String> {
    Optional<Product> findByName(String name);
    List<Product> findBySellerId(String sellerId);
    List<Product> findByCategoryId(String categoryId);
    List<Product> findByCreatedBy(String createdBy);
    Optional<Product> findByIdAndSellerId(String productId, String sellerId);

    // --- NEW: Pagination and Search Methods ---
    Page<Product> findAll(Pageable pageable);
    Page<Product> findByCategoryNameIgnoreCase(String categoryName, Pageable pageable);
    Page<Product> findByNameContainingIgnoreCaseOrBrandContainingIgnoreCase(String name, String brand, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE " +
            "(:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.brand) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
            "(:categoryName IS NULL OR LOWER(p.category.name) = LOWER(:categoryName)) AND " +
            "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
            "(p.status = 'AVAILABLE')")
    Page<Product> searchAndFilterProducts(
            @Param("keyword") String keyword,
            @Param("categoryName") String categoryName,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            Pageable pageable
    );

}