package com.crimsonlogic.ecommerce.controller;

import com.crimsonlogic.ecommerce.dto.admin.AdminDashboardStatsDTO;
import com.crimsonlogic.ecommerce.handler.ApiResponse;
import com.crimsonlogic.ecommerce.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // View Platform Metrics & Analytics
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<AdminDashboardStatsDTO>> getDashboardStats(
            @RequestAttribute("role") String role) {

        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(403).build();
        }
        AdminDashboardStatsDTO stats = adminService.getDashboardStats();
        return ResponseEntity.ok(ApiResponse.success("Admin dashboard stats retrieved successfully", stats));
    }

    // Remove / Ban a User (Customer or Seller)
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<String>> deleteUser(
            @PathVariable String userId,
            @RequestParam String role,
            @RequestAttribute("role") String adminRole) {

        if (!"ADMIN".equals(adminRole)) {
            return ResponseEntity.status(403).body(ApiResponse.error("Access Denied: Only Admins can perform this action."));
        }
        String result = adminService.deleteUserByAdmin(userId, role);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}