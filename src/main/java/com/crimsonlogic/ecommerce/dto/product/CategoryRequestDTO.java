package com.crimsonlogic.ecommerce.dto.product;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CategoryRequestDTO {
    @NotBlank(message = "Category Name cannot be empty.")
    @Size(min = 3, max = 100, message = "Category Name must be 3-100 characters.")
    private String name;

    @NotBlank(message = "Category Description cannot be empty.")
    @Size(min = 5, max = 500, message = "Category Description must be 5-500 characters.")
    private String description;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}