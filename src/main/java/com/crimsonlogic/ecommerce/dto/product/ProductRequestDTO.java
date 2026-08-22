package com.crimsonlogic.ecommerce.dto.product;

import javax.validation.constraints.*;

public class ProductRequestDTO {

    @NotBlank(message = "Product Name cannot be empty.")
    @Size(min = 3, max = 150)
    private String name;

    @NotBlank(message = "Brand is required.")
    @Size(max = 100)
    private String brand;

    @NotBlank(message = "Product Description cannot be empty.")
    @Size(min = 10, max = 500)
    private String description;

    @NotNull(message = "Price is required.")
    @DecimalMin(value = "0.01", message = "Price must be greater than zero.")
    private Double price;

    // CHANGED: Use Category Name instead of ID
    @NotBlank(message = "Category Name is required.")
    private String categoryName;

    // ADD THIS NEW FIELD
    @NotNull(message = "Quantity is required.")
    @Min(value = 1, message = "Quantity must be at least 1.")
    private Integer quantity;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}