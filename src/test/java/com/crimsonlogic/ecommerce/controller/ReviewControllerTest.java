package com.crimsonlogic.ecommerce.controller;

import com.crimsonlogic.ecommerce.dto.review.ReviewRequestDTO;
import com.crimsonlogic.ecommerce.dto.review.ReviewResponseDTO;
import com.crimsonlogic.ecommerce.service.ReviewService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class ReviewControllerTest {

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    private MockMvc mockMvc;


    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(reviewController)
                .build();
    }


    // ==========================================================
    // ADD REVIEW
    // ==========================================================

    @Test
    void shouldAddReviewSuccessfullyForCustomer()
            throws Exception {

        when(reviewService.addReview(
                any(ReviewRequestDTO.class),
                eq("CUS001")
        )).thenReturn("Review added successfully");

        String requestJson = """
                {
                    "productName": "Laptop",
                    "rating": 5,
                    "comment": "Excellent product"
                }
                """;

        mockMvc.perform(
                        post("/api/v1/reviews/add")
                                .requestAttr("userId", "CUS001")
                                .requestAttr("role", "CUSTOMER")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isOk());

        verify(reviewService)
                .addReview(
                        any(ReviewRequestDTO.class),
                        eq("CUS001")
                );
    }


    @Test
    void shouldRejectAddReviewForSeller()
            throws Exception {

        String requestJson = """
                {
                    "productName": "Laptop",
                    "rating": 5,
                    "comment": "Excellent product"
                }
                """;

        mockMvc.perform(
                        post("/api/v1/reviews/add")
                                .requestAttr("userId", "SEL001")
                                .requestAttr("role", "SELLER")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(reviewService);
    }


    @Test
    void shouldRejectAddReviewForAdmin()
            throws Exception {

        String requestJson = """
                {
                    "productName": "Laptop",
                    "rating": 5,
                    "comment": "Excellent product"
                }
                """;

        mockMvc.perform(
                        post("/api/v1/reviews/add")
                                .requestAttr("userId", "ADM001")
                                .requestAttr("role", "ADMIN")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(reviewService);
    }


    // ==========================================================
    // GET PRODUCT REVIEWS
    // ==========================================================

    @Test
    void shouldGetProductReviewsSuccessfully()
            throws Exception {

        List<ReviewResponseDTO> reviews =
                Collections.emptyList();

        when(reviewService.getProductReviews("Laptop"))
                .thenReturn(reviews);

        mockMvc.perform(
                        get("/api/v1/reviews/product/Laptop")
                )
                .andExpect(status().isOk());

        verify(reviewService)
                .getProductReviews("Laptop");
    }


    // ==========================================================
    // GET SELLER PRODUCT REVIEWS
    // ==========================================================

    @Test
    void shouldGetSellerProductReviewsSuccessfully()
            throws Exception {

        List<ReviewResponseDTO> reviews =
                Collections.emptyList();

        when(reviewService.getReviewsForSellerProduct(
                "Laptop",
                "SEL001"
        )).thenReturn(reviews);

        mockMvc.perform(
                        get("/api/v1/reviews/seller/product/Laptop")
                                .requestAttr("userId", "SEL001")
                                .requestAttr("role", "SELLER")
                )
                .andExpect(status().isOk());

        verify(reviewService)
                .getReviewsForSellerProduct(
                        "Laptop",
                        "SEL001"
                );
    }


    @Test
    void shouldRejectSellerProductReviewsForCustomer()
            throws Exception {

        mockMvc.perform(
                        get("/api/v1/reviews/seller/product/Laptop")
                                .requestAttr("userId", "CUS001")
                                .requestAttr("role", "CUSTOMER")
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(reviewService);
    }


    @Test
    void shouldRejectSellerProductReviewsForAdmin()
            throws Exception {

        mockMvc.perform(
                        get("/api/v1/reviews/seller/product/Laptop")
                                .requestAttr("userId", "ADM001")
                                .requestAttr("role", "ADMIN")
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(reviewService);
    }
}