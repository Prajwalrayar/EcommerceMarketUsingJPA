package com.crimsonlogic.ecommerce.service.impl;

import com.crimsonlogic.ecommerce.dto.product.CategoryRequestDTO;
import com.crimsonlogic.ecommerce.entity.Category;
import com.crimsonlogic.ecommerce.exception.ValidationException;
import com.crimsonlogic.ecommerce.repository.CategoryRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    // ==========================================================
    // ADD CATEGORY - SUCCESS
    // ==========================================================

    @Test
    void shouldAddCategorySuccessfully() {

        CategoryRequestDTO request = new CategoryRequestDTO();

        request.setName("Electronics");
        request.setDescription(
                "Electronic products and accessories"
        );

        when(categoryRepository.existsByName("Electronics"))
                .thenReturn(false);

        String result =
                categoryService.addCategory(request);

        assertEquals(
                "Category added successfully!",
                result
        );

        verify(categoryRepository)
                .existsByName("Electronics");

        verify(categoryRepository)
                .save(any(Category.class));
    }

    // ==========================================================
    // ADD CATEGORY - DUPLICATE
    // ==========================================================

    @Test
    void shouldThrowExceptionWhenCategoryAlreadyExists() {

        CategoryRequestDTO request = new CategoryRequestDTO();

        request.setName("Electronics");
        request.setDescription(
                "Electronic products and accessories"
        );

        when(categoryRepository.existsByName("Electronics"))
                .thenReturn(true);

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> categoryService.addCategory(request)
                );

        assertEquals(
                "Category with this name already exists.",
                exception.getMessage()
        );

        verify(categoryRepository)
                .existsByName("Electronics");

        // Because validation failed, save must never happen
        org.mockito.Mockito.verify(
                categoryRepository,
                org.mockito.Mockito.never()
        ).save(any(Category.class));
    }
}