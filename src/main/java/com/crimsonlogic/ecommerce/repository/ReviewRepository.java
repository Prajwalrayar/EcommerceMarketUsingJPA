package com.crimsonlogic.ecommerce.repository;

import com.crimsonlogic.ecommerce.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, String> {

    // Finds all reviews for a specific product so we can display them on the product page
    List<Review> findByProductId(String productId);

    boolean existsByCustomerIdAndProductId(String customerId, String productId);
}