package com.crimsonlogic.ecommerce.controller;

import com.crimsonlogic.ecommerce.dto.product.CategoryRequestDTO;
import com.crimsonlogic.ecommerce.service.CategoryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(categoryController)
                .build();
    }

    // ==========================================================
    // ADD CATEGORY - ADMIN
    // ==========================================================

    @Test
    void shouldAddCategorySuccessfullyForAdmin()
            throws Exception {

        when(categoryService.addCategory(
                any(CategoryRequestDTO.class)
        )).thenReturn("Category added successfully");

        String requestJson = """
                {
                    "name": "Electronics",
                    "description": "Electronic products and accessories"
                }
                """;

        mockMvc.perform(
                        post("/api/v1/admin/categories/add")
                                .requestAttr("role", "ADMIN")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isOk());

        verify(categoryService)
                .addCategory(
                        any(CategoryRequestDTO.class)
                );
    }

    // ==========================================================
    // ADD CATEGORY - CUSTOMER
    // ==========================================================

    @Test
    void shouldRejectAddCategoryForCustomer()
            throws Exception {

        String requestJson = """
                {
                    "name": "Electronics",
                    "description": "Electronic products and accessories"
                }
                """;

        mockMvc.perform(
                        post("/api/v1/admin/categories/add")
                                .requestAttr("role", "CUSTOMER")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(categoryService);
    }

    // ==========================================================
    // ADD CATEGORY - SELLER
    // ==========================================================

    @Test
    void shouldRejectAddCategoryForSeller()
            throws Exception {

        String requestJson = """
                {
                    "name": "Electronics",
                    "description": "Electronic products and accessories"
                }
                """;

        mockMvc.perform(
                        post("/api/v1/admin/categories/add")
                                .requestAttr("role", "SELLER")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(categoryService);
    }
}