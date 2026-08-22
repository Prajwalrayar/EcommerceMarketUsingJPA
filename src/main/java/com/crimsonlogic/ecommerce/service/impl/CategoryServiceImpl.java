package com.crimsonlogic.ecommerce.service.impl;

import com.crimsonlogic.ecommerce.dto.product.CategoryRequestDTO;
import com.crimsonlogic.ecommerce.entity.Category;
import com.crimsonlogic.ecommerce.exception.ValidationException;
import com.crimsonlogic.ecommerce.repository.CategoryRepository;
import com.crimsonlogic.ecommerce.util.IdGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CategoryServiceImpl {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public String addCategory(CategoryRequestDTO request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new ValidationException("Category with this name already exists.");
        }

        Category category = new Category();
        category.setId(IdGenerator.generateId("CAT"));
        category.setName(request.getName());
        category.setDescription(request.getDescription());

        categoryRepository.save(category);
        return "Category added successfully!";
    }
}