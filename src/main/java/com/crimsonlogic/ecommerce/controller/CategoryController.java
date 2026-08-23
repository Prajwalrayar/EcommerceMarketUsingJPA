package com.crimsonlogic.ecommerce.controller;

import com.crimsonlogic.ecommerce.dto.product.CategoryRequestDTO;
import com.crimsonlogic.ecommerce.handler.ApiResponse;
import com.crimsonlogic.ecommerce.service.CategoryService;
import com.crimsonlogic.ecommerce.service.impl.CategoryServiceImpl;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<String>> addCategory(
            @RequestAttribute("role") String role,
            @Valid @RequestBody CategoryRequestDTO request) {

        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(
                            "Only Admins can add categories."
                    ));
        }

        String response = categoryService.addCategory(request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Category added successfully",
                        response
                )
        );
    }
}