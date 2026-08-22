package com.crimsonlogic.ecommerce.dto.review;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ReviewRequestDTO {

    @NotBlank(message = "Product Name is required.")
    private String productName;

    @NotNull(message = "Rating is required.")
    @Min(value = 1, message = "Rating must be at least 1.")
    @Max(value = 5, message = "Rating cannot exceed 5.")
    private Integer rating;

    private String comment;

    // Getters and Setters
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}