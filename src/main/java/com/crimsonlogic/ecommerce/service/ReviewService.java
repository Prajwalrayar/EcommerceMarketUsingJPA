package com.crimsonlogic.ecommerce.service;

import com.crimsonlogic.ecommerce.dto.review.ReviewRequestDTO;
import com.crimsonlogic.ecommerce.dto.review.ReviewResponseDTO;

import java.util.List;

public interface ReviewService {

    String addReview(
            ReviewRequestDTO request,
            String customerId
    );

    List<ReviewResponseDTO> getProductReviews(
            String productName
    );

    List<ReviewResponseDTO> getReviewsForSellerProduct(
            String productName,
            String sellerId
    );
}