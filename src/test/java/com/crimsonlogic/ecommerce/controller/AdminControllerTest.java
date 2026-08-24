package com.crimsonlogic.ecommerce.controller;

import com.crimsonlogic.ecommerce.dto.admin.AdminDashboardStatsDTO;
import com.crimsonlogic.ecommerce.service.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(adminController)
                .build();
    }

    // ==========================================================
    // GET DASHBOARD STATS
    // ==========================================================

    @Test
    void shouldGetDashboardStatsWhenRoleIsAdmin()
            throws Exception {

        AdminDashboardStatsDTO stats =
                new AdminDashboardStatsDTO();

        when(adminService.getDashboardStats())
                .thenReturn(stats);

        mockMvc.perform(
                        get("/api/v1/admin/stats")
                                .requestAttr("role", "ADMIN")
                )
                .andExpect(status().isOk());

        verify(adminService)
                .getDashboardStats();
    }

    @Test
    void shouldRejectDashboardStatsWhenRoleIsNotAdmin()
            throws Exception {

        mockMvc.perform(
                        get("/api/v1/admin/stats")
                                .requestAttr("role", "CUSTOMER")
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(adminService);
    }

    @Test
    void shouldRejectDashboardStatsWhenRoleIsSeller()
            throws Exception {

        mockMvc.perform(
                        get("/api/v1/admin/stats")
                                .requestAttr("role", "SELLER")
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(adminService);
    }

    // ==========================================================
    // DELETE / BAN USER
    // ==========================================================

    @Test
    void shouldDeleteUserWhenRoleIsAdmin()
            throws Exception {

        when(adminService.deleteUserByAdmin(
                "CUS001",
                "CUSTOMER"
        )).thenReturn("User deleted successfully");

        mockMvc.perform(
                        delete("/api/v1/admin/user/CUS001")
                                .param("role", "CUSTOMER")
                                .requestAttr("role", "ADMIN")
                )
                .andExpect(status().isOk());

        verify(adminService)
                .deleteUserByAdmin(
                        "CUS001",
                        "CUSTOMER"
                );
    }

    @Test
    void shouldDeleteSellerWhenRoleIsAdmin()
            throws Exception {

        when(adminService.deleteUserByAdmin(
                "SEL001",
                "SELLER"
        )).thenReturn("User deleted successfully");

        mockMvc.perform(
                        delete("/api/v1/admin/user/SEL001")
                                .param("role", "SELLER")
                                .requestAttr("role", "ADMIN")
                )
                .andExpect(status().isOk());

        verify(adminService)
                .deleteUserByAdmin(
                        "SEL001",
                        "SELLER"
                );
    }

    @Test
    void shouldRejectDeleteUserWhenRoleIsCustomer()
            throws Exception {

        mockMvc.perform(
                        delete("/api/v1/admin/user/CUS001")
                                .param("role", "CUSTOMER")
                                .requestAttr("role", "CUSTOMER")
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(adminService);
    }

    @Test
    void shouldRejectDeleteUserWhenRoleIsSeller()
            throws Exception {

        mockMvc.perform(
                        delete("/api/v1/admin/user/SEL001")
                                .param("role", "SELLER")
                                .requestAttr("role", "SELLER")
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(adminService);
    }
}