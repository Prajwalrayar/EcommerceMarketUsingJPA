package com.crimsonlogic.ecommerce.controller;

import com.crimsonlogic.ecommerce.dto.review.ReviewRequestDTO;
import com.crimsonlogic.ecommerce.dto.review.ReviewResponseDTO;
import com.crimsonlogic.ecommerce.handler.ApiResponse;
import com.crimsonlogic.ecommerce.service.impl.ReviewServiceImpl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewServiceImpl reviewService;

    public ReviewController(ReviewServiceImpl reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<String>> addReview(
            @Valid @RequestBody ReviewRequestDTO request,
            @RequestAttribute("userId") String userId,
            @RequestAttribute("role") String role) {

        if (!"CUSTOMER".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(
                            "Only customers can leave reviews."
                    ));
        }

        String response = reviewService.addReview(request, userId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Review added successfully",
                        response
                )
        );
    }

    @GetMapping("/product/{productName}")
    public ResponseEntity<ApiResponse<List<ReviewResponseDTO>>> getProductReviews(
            @PathVariable String productName) {

        // This endpoint is public; no login required to read reviews
        List<ReviewResponseDTO> reviews =
                reviewService.getProductReviews(productName);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Product reviews retrieved successfully",
                        reviews
                )
        );
    }

    // Seller can view reviews for their own product
    @GetMapping("/seller/product/{productName}")
    public ResponseEntity<ApiResponse<List<ReviewResponseDTO>>> getSellerProductReviews(
            @PathVariable String productName,
            @RequestAttribute("userId") String userId,
            @RequestAttribute("role") String role) {

        // Ensure only sellers can access this specific dashboard endpoint
        if (!"SELLER".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(
                            "Only sellers can access product reviews from this endpoint."
                    ));
        }

        List<ReviewResponseDTO> reviews =
                reviewService.getReviewsForSellerProduct(
                        productName,
                        userId
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Seller product reviews retrieved successfully",
                        reviews
                )
        );
    }
}