package com.crimsonlogic.ecommerce.controller;

import com.crimsonlogic.ecommerce.dto.product.CategoryRequestDTO;
import com.crimsonlogic.ecommerce.service.impl.CategoryServiceImpl;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/categories")
public class CategoryController {

    private final CategoryServiceImpl categoryService;

    public CategoryController(CategoryServiceImpl categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addCategory(
            @RequestAttribute("role") String role,
            @Valid @RequestBody CategoryRequestDTO request) {

        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(403).body("Only Admins can add categories.");
        }

        return ResponseEntity.ok(categoryService.addCategory(request));
    }
}