package com.crimsonlogic.ecommerce.service;

import com.crimsonlogic.ecommerce.dto.admin.AdminDashboardStatsDTO;

public interface AdminService {

    AdminDashboardStatsDTO getDashboardStats();

    String deleteUserByAdmin(String userId, String role);
}