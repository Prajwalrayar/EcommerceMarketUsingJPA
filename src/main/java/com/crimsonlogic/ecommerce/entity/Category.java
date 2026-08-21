package com.crimsonlogic.ecommerce.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @Column(name = "category_id", length = 20)
    private String id;

    @Column(name = "category_name", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "category_description", nullable = false, length = 500)
    private String description;

    public Category() {}

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}