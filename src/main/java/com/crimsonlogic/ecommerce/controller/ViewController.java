package com.crimsonlogic.ecommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    // Redirect root to the new landing page
    @GetMapping("/")
    public String home() {
        return "index";
    }

    // ==========================================
    // Public / Auth Views
    // ==========================================
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    // ==========================================
    // Customer Views
    // ==========================================
    @GetMapping("/cart")
    public String viewCart() {
        return "cart"; // Resolves to /WEB-INF/views/cart.jsp
    }

    @GetMapping("/checkout")
    public String checkoutPage() {
        return "checkout";
    }

    @GetMapping("/profile")
    public String viewProfile() {
        return "profile"; // Resolves to /WEB-INF/views/profile.jsp
    }

    @GetMapping("/products")
    public String viewProducts() {
        return "products"; // Resolves to /WEB-INF/views/products.jsp
    }


    // ==========================================
    // Admin & Seller Views
    // ==========================================
    @GetMapping("/admin/dashboard")
    public String adminDashboardPage() {
        return "admin-dashboard";
    }

    @GetMapping("/seller/dashboard")
    public String sellerDashboardPage() {
        return "seller-dashboard";
    }
}