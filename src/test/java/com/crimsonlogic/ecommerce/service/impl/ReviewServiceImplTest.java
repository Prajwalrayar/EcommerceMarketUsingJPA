package com.crimsonlogic.ecommerce.service.impl;

import com.crimsonlogic.ecommerce.dto.review.ReviewRequestDTO;
import com.crimsonlogic.ecommerce.dto.review.ReviewResponseDTO;
import com.crimsonlogic.ecommerce.entity.Customer;
import com.crimsonlogic.ecommerce.entity.Order;
import com.crimsonlogic.ecommerce.entity.Product;
import com.crimsonlogic.ecommerce.entity.Review;
import com.crimsonlogic.ecommerce.enumeration.OrderStatus;
import com.crimsonlogic.ecommerce.exception.ValidationException;
import com.crimsonlogic.ecommerce.repository.CustomerRepository;
import com.crimsonlogic.ecommerce.repository.OrderRepository;
import com.crimsonlogic.ecommerce.repository.ProductRepository;
import com.crimsonlogic.ecommerce.repository.ReviewRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private Customer customer;
    private Product product;
    private ReviewRequestDTO request;
    private Order order;

    @BeforeEach
    void setUp() {

        customer = new Customer();
        customer.setId("CUS001");
        customer.setName("Prajwal");

        product = new Product();
        product.setId("PRO001");
        product.setName("Laptop");

        request = new ReviewRequestDTO();
        request.setProductName("Laptop");
        request.setRating(5);
        request.setComment("Excellent product");

        order = new Order();
        order.setId("ORD001");
        order.setCustomer(customer);
        order.setProduct(product);
        order.setStatus(OrderStatus.DELIVERED);
    }

    // ==========================================================
    // ADD REVIEW - SUCCESS
    // ==========================================================

    @Test
    void shouldAddReviewSuccessfully() {

        when(customerRepository.findById("CUS001"))
                .thenReturn(Optional.of(customer));

        when(productRepository.findByName("Laptop"))
                .thenReturn(Optional.of(product));

        when(orderRepository.findByCustomerIdOrderByOrderDateDesc("CUS001"))
                .thenReturn(List.of(order));

        when(reviewRepository.existsByCustomerIdAndProductId(
                "CUS001",
                "PRO001"
        )).thenReturn(false);

        when(reviewRepository.save(any(Review.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        String result =
                reviewService.addReview(request, "CUS001");

        assertEquals(
                "Review submitted successfully!",
                result
        );

        verify(customerRepository)
                .findById("CUS001");

        verify(productRepository)
                .findByName("Laptop");

        verify(orderRepository)
                .findByCustomerIdOrderByOrderDateDesc("CUS001");

        verify(reviewRepository)
                .existsByCustomerIdAndProductId(
                        "CUS001",
                        "PRO001"
                );

        verify(reviewRepository)
                .save(any(Review.class));
    }

    // ==========================================================
    // CUSTOMER NOT FOUND
    // ==========================================================

    @Test
    void shouldRejectReviewWhenCustomerDoesNotExist() {

        when(customerRepository.findById("CUS001"))
                .thenReturn(Optional.empty());

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> reviewService.addReview(
                                request,
                                "CUS001"
                        )
                );

        assertEquals(
                "Customer not found.",
                exception.getMessage()
        );

        verify(customerRepository)
                .findById("CUS001");

        verifyNoInteractions(
                productRepository,
                orderRepository,
                reviewRepository
        );
    }

    // ==========================================================
    // PRODUCT NOT FOUND
    // ==========================================================

    @Test
    void shouldRejectReviewWhenProductDoesNotExist() {

        when(customerRepository.findById("CUS001"))
                .thenReturn(Optional.of(customer));

        when(productRepository.findByName("Laptop"))
                .thenReturn(Optional.empty());

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> reviewService.addReview(
                                request,
                                "CUS001"
                        )
                );

        assertEquals(
                "Product 'Laptop' not found.",
                exception.getMessage()
        );

        verify(customerRepository)
                .findById("CUS001");

        verify(productRepository)
                .findByName("Laptop");

        verifyNoInteractions(
                orderRepository,
                reviewRepository
        );
    }

    // ==========================================================
    // PRODUCT NOT DELIVERED
    // ==========================================================

    @Test
    void shouldRejectReviewWhenProductWasNotDelivered() {

        order.setStatus(OrderStatus.PENDING_APPROVAL);

        when(customerRepository.findById("CUS001"))
                .thenReturn(Optional.of(customer));

        when(productRepository.findByName("Laptop"))
                .thenReturn(Optional.of(product));

        when(orderRepository.findByCustomerIdOrderByOrderDateDesc("CUS001"))
                .thenReturn(List.of(order));

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> reviewService.addReview(
                                request,
                                "CUS001"
                        )
                );

        assertEquals(
                "You can only review products that have been successfully delivered to you.",
                exception.getMessage()
        );

        verify(orderRepository)
                .findByCustomerIdOrderByOrderDateDesc("CUS001");

        verifyNoInteractions(reviewRepository);
    }

    // ==========================================================
    // NO ORDERS
    // ==========================================================

    @Test
    void shouldRejectReviewWhenCustomerHasNoOrders() {

        when(customerRepository.findById("CUS001"))
                .thenReturn(Optional.of(customer));

        when(productRepository.findByName("Laptop"))
                .thenReturn(Optional.of(product));

        when(orderRepository.findByCustomerIdOrderByOrderDateDesc("CUS001"))
                .thenReturn(Collections.emptyList());

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> reviewService.addReview(
                                request,
                                "CUS001"
                        )
                );

        assertEquals(
                "You can only review products that have been successfully delivered to you.",
                exception.getMessage()
        );

        verifyNoInteractions(reviewRepository);
    }

    // ==========================================================
    // DUPLICATE REVIEW
    // ==========================================================

    @Test
    void shouldRejectDuplicateReview() {

        when(customerRepository.findById("CUS001"))
                .thenReturn(Optional.of(customer));

        when(productRepository.findByName("Laptop"))
                .thenReturn(Optional.of(product));

        when(orderRepository.findByCustomerIdOrderByOrderDateDesc("CUS001"))
                .thenReturn(List.of(order));

        when(reviewRepository.existsByCustomerIdAndProductId(
                "CUS001",
                "PRO001"
        )).thenReturn(true);

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> reviewService.addReview(
                                request,
                                "CUS001"
                        )
                );

        assertEquals(
                "You have already reviewed this product.",
                exception.getMessage()
        );

        verify(reviewRepository)
                .existsByCustomerIdAndProductId(
                        "CUS001",
                        "PRO001"
                );

        verify(reviewRepository, never())
                .save(any(Review.class));
    }

    // ==========================================================
    // GET PRODUCT REVIEWS - SUCCESS
    // ==========================================================

    @Test
    void shouldGetProductReviewsSuccessfully() {

        Review review = new Review();
        review.setId("REV001");
        review.setCustomer(customer);
        review.setProduct(product);
        review.setRating(5);
        review.setComment("Excellent product");

        when(productRepository.findByName("Laptop"))
                .thenReturn(Optional.of(product));

        when(reviewRepository.findByProductId("PRO001"))
                .thenReturn(List.of(review));

        List<ReviewResponseDTO> result =
                reviewService.getProductReviews("Laptop");

        assertNotNull(result);
        assertEquals(1, result.size());

        assertEquals(
                "REV001",
                result.get(0).getReviewId()
        );

        assertEquals(
                "Prajwal",
                result.get(0).getCustomerName()
        );

        assertEquals(
                5,
                result.get(0).getRating()
        );

        assertEquals(
                "Excellent product",
                result.get(0).getComment()
        );

        verify(productRepository)
                .findByName("Laptop");

        verify(reviewRepository)
                .findByProductId("PRO001");
    }

    // ==========================================================
    // GET PRODUCT REVIEWS - PRODUCT NOT FOUND
    // ==========================================================

    @Test
    void shouldRejectGetReviewsWhenProductDoesNotExist() {

        when(productRepository.findByName("Laptop"))
                .thenReturn(Optional.empty());

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> reviewService.getProductReviews("Laptop")
                );

        assertEquals(
                "Product 'Laptop' not found.",
                exception.getMessage()
        );

        verify(reviewRepository, never())
                .findByProductId(anyString());
    }

    // ==========================================================
    // SELLER GET REVIEWS - SUCCESS
    // ==========================================================

    @Test
    void shouldGetSellerProductReviewsSuccessfully() {

        var seller = new com.crimsonlogic.ecommerce.entity.Seller();
        seller.setId("SEL001");
        seller.setShopName("Prajwal Electronics");

        product.setSeller(seller);

        Review review = new Review();
        review.setId("REV001");
        review.setCustomer(customer);
        review.setProduct(product);
        review.setRating(4);
        review.setComment("Good product");

        when(productRepository.findByName("Laptop"))
                .thenReturn(Optional.of(product));

        when(reviewRepository.findByProductId("PRO001"))
                .thenReturn(List.of(review));

        List<ReviewResponseDTO> result =
                reviewService.getReviewsForSellerProduct(
                        "Laptop",
                        "SEL001"
                );

        assertNotNull(result);
        assertEquals(1, result.size());

        assertEquals(
                "REV001",
                result.get(0).getReviewId()
        );

        verify(productRepository)
                .findByName("Laptop");

        verify(reviewRepository)
                .findByProductId("PRO001");
    }

    // ==========================================================
    // SELLER ACCESS DENIED
    // ==========================================================

    @Test
    void shouldRejectSellerWhenProductBelongsToAnotherSeller() {

        var seller = new com.crimsonlogic.ecommerce.entity.Seller();
        seller.setId("SEL002");

        product.setSeller(seller);

        when(productRepository.findByName("Laptop"))
                .thenReturn(Optional.of(product));

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> reviewService.getReviewsForSellerProduct(
                                "Laptop",
                                "SEL001"
                        )
                );

        assertEquals(
                "Access Denied: You can only manage reviews for your own products.",
                exception.getMessage()
        );

        verify(reviewRepository, never())
                .findByProductId(anyString());
    }
}