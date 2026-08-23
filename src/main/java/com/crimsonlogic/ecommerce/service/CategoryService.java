package com.crimsonlogic.ecommerce.service;

import com.crimsonlogic.ecommerce.dto.product.CategoryRequestDTO;

public interface CategoryService {

    String addCategory(CategoryRequestDTO request);
}