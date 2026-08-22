package com.crimsonlogic.ecommerce.dto.product;

public class ProductResponseDTO {
    private String id;
    private String name;
    private String brand;
    private String description;
    private Double price;
    private String categoryName;
    private String sellerName;
    private String status;

    // Standard Getters and Setters omitted for brevity
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
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
    public String getSellerName() { return sellerName; }
    public void setSellerName(String sellerName) { this.sellerName = sellerName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}