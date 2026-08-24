package com.crimsonlogic.ecommerce.dto.product;

import javax.validation.constraints.Size;

public class ProductUpdateRequestDTO {

    @Size(max = 150, message = "Product name cannot exceed 150 characters.")
    private String name;

    @Size(max = 100, message = "Brand cannot exceed 100 characters.")
    private String brand;

    @Size(max = 500, message = "Description cannot exceed 500 characters.")
    private String description;

    private Double price;

    @Size(max = 100, message = "Category name cannot exceed 100 characters.")
    private String categoryName;

    private Integer quantity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}