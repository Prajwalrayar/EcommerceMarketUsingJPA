package com.crimsonlogic.ecommerce.entity;

import com.crimsonlogic.ecommerce.enumeration.ProductStatus;
import javax.persistence.*;

@Entity
@Table(name = "products",
        uniqueConstraints = {@UniqueConstraint(name = "uk_product_name_brand",
                columnNames = {"product_name", "brand"})
        }
)
public class Product {

    @Id
    @Column(name = "product_id", length = 20)
    private String id;

    @Column(name = "product_name", nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 100)
    private String brand;

    @Column(name = "product_description", nullable = false, length = 500)
    private String description;

    @Column(name = "product_price", nullable = false)
    private Double price;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_status", nullable = false)
    private ProductStatus status = ProductStatus.OUT_OF_STOCK;

    @Column(name = "created_by", nullable = false, length = 20)
    private String createdBy; // ID of the Admin or Seller who created it

    // A Product belongs to one Category
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    // A Product belongs to one Seller (nullable because Admins can create products without a seller)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Seller seller;

    public Product() {}

    // Getters and Setters
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
    public ProductStatus getStatus() { return status; }
    public void setStatus(ProductStatus status) { this.status = status; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    public Seller getSeller() { return seller; }
    public void setSeller(Seller seller) { this.seller = seller; }
}