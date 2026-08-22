package com.crimsonlogic.ecommerce.service.impl;

import com.crimsonlogic.ecommerce.dto.review.ReviewRequestDTO;
import com.crimsonlogic.ecommerce.dto.review.ReviewResponseDTO;
import com.crimsonlogic.ecommerce.entity.Customer;
import com.crimsonlogic.ecommerce.entity.Product;
import com.crimsonlogic.ecommerce.entity.Review;
import com.crimsonlogic.ecommerce.enumeration.OrderStatus;
import com.crimsonlogic.ecommerce.exception.ValidationException;
import com.crimsonlogic.ecommerce.repository.CustomerRepository;
import com.crimsonlogic.ecommerce.repository.OrderRepository;
import com.crimsonlogic.ecommerce.repository.ProductRepository;
import com.crimsonlogic.ecommerce.repository.ReviewRepository;
import com.crimsonlogic.ecommerce.util.IdGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReviewServiceImpl {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository, ProductRepository productRepository,
                             CustomerRepository customerRepository, OrderRepository orderRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
    }

    public String addReview(ReviewRequestDTO request, String customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ValidationException("Customer not found."));

        // ---> 1. Find the product by name first <---
        Product product = productRepository.findByName(request.getProductName().trim())
                .orElseThrow(() -> new ValidationException("Product '" + request.getProductName() + "' not found."));

        // ---> 2. Check if customer has a delivered order for this product ID <---
        boolean hasDeliveredOrder = orderRepository.findByCustomerIdOrderByOrderDateDesc(customerId).stream()
                .anyMatch(order -> order.getProduct().getId().equals(product.getId())
                        && order.getStatus() == OrderStatus.DELIVERED);

        if (!hasDeliveredOrder) {
            throw new ValidationException("You can only review products that have been successfully delivered to you.");
        }

        // ---> 3. Prevent duplicate reviews using product ID <---
        if (reviewRepository.existsByCustomerIdAndProductId(customerId, product.getId())) {
            throw new ValidationException("You have already reviewed this product.");
        }

        Review review = new Review();
        review.setId(IdGenerator.generateId("REV"));
        review.setCustomer(customer);
        review.setProduct(product);
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setReviewDate(LocalDateTime.now());

        reviewRepository.save(review);
        return "Review submitted successfully!";
    }

    public List<ReviewResponseDTO> getProductReviews(String productName) {
        Product product = productRepository.findByName(productName.trim())
                .orElseThrow(() -> new ValidationException("Product '" + productName + "' not found."));

        return reviewRepository.findByProductId(product.getId()).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<ReviewResponseDTO> getReviewsForSellerProduct(String productName, String sellerId) {
        Product product = productRepository.findByName(productName.trim())
                .orElseThrow(() -> new ValidationException("Product '" + productName + "' not found."));

        if (product.getSeller() == null || !product.getSeller().getId().equals(sellerId)) {
            throw new ValidationException("Access Denied: You can only manage reviews for your own products.");
        }

        return reviewRepository.findByProductId(product.getId())
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private ReviewResponseDTO mapToDTO(Review review) {
        ReviewResponseDTO dto = new ReviewResponseDTO();
        dto.setReviewId(review.getId());
        dto.setCustomerName(review.getCustomer() != null ? review.getCustomer().getName() : "Anonymous");
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        if (review.getReviewDate() != null) {
            dto.setReviewDate(review.getReviewDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }
        return dto;
    }
}