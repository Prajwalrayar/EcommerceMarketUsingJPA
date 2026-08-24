package com.crimsonlogic.ecommerce.entity;

import com.crimsonlogic.ecommerce.enumeration.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AdminTest {

    @Test
    void shouldCreateAdminWithAdminRole() {

        Admin admin = new Admin();

        assertNotNull(admin);
        assertEquals(Role.ADMIN, admin.getRole());
    }
}