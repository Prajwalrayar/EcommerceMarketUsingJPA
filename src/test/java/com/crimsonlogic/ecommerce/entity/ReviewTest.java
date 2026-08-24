package com.crimsonlogic.ecommerce.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ReviewTest {

    @Test
    void shouldCreateReviewWithDefaultValues() {

        Review review = new Review();

        assertNotNull(review);

        assertNull(review.getId());
        assertNull(review.getProduct());
        assertNull(review.getCustomer());
        assertEquals(0, review.getRating());
        assertNull(review.getComment());
        assertNull(review.getReviewDate());
    }

    @Test
    void shouldSetAndGetId() {

        Review review = new Review();

        review.setId("REV001");

        assertEquals("REV001", review.getId());
    }

    @Test
    void shouldSetAndGetProduct() {

        Review review = new Review();
        Product product = new Product();

        review.setProduct(product);

        assertEquals(product, review.getProduct());
    }

    @Test
    void shouldSetAndGetCustomer() {

        Review review = new Review();
        Customer customer = new Customer();

        review.setCustomer(customer);

        assertEquals(customer, review.getCustomer());
    }

    @Test
    void shouldSetAndGetRating() {

        Review review = new Review();

        review.setRating(5);

        assertEquals(5, review.getRating());
    }

    @Test
    void shouldSetAndGetComment() {

        Review review = new Review();

        review.setComment("Excellent product!");

        assertEquals(
                "Excellent product!",
                review.getComment()
        );
    }

    @Test
    void shouldSetAndGetReviewDate() {

        Review review = new Review();

        LocalDateTime reviewDate =
                LocalDateTime.of(2026, 8, 24, 15, 30);

        review.setReviewDate(reviewDate);

        assertEquals(reviewDate, review.getReviewDate());
    }

    @Test
    void shouldSetAndGetCompleteReview() {

        Review review = new Review();

        Product product = new Product();
        Customer customer = new Customer();

        LocalDateTime reviewDate =
                LocalDateTime.of(2026, 8, 24, 15, 30);

        review.setId("REV001");
        review.setProduct(product);
        review.setCustomer(customer);
        review.setRating(5);
        review.setComment("Excellent product!");
        review.setReviewDate(reviewDate);

        assertEquals("REV001", review.getId());
        assertEquals(product, review.getProduct());
        assertEquals(customer, review.getCustomer());
        assertEquals(5, review.getRating());
        assertEquals("Excellent product!", review.getComment());
        assertEquals(reviewDate, review.getReviewDate());
    }
}