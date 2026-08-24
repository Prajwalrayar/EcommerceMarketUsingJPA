package com.crimsonlogic.ecommerce.component;

import com.crimsonlogic.ecommerce.entity.Admin;
import com.crimsonlogic.ecommerce.repository.AdminRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminDataLoaderTest {

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private AdminDataLoader adminDataLoader;


    // ==========================================================
    // DATABASE EMPTY
    // ==========================================================

    @Test
    void shouldLoadDefaultAdminsWhenDatabaseIsEmpty() {

        // Given
        when(adminRepository.count())
                .thenReturn(0L);

        // When
        adminDataLoader.loadAdmins();

        // Then
        ArgumentCaptor<List<Admin>> captor =
                ArgumentCaptor.forClass(List.class);

        verify(adminRepository)
                .saveAll(captor.capture());

        List<Admin> admins = captor.getValue();

        assertNotNull(admins);

        // Three default admins should be created
        assertEquals(3, admins.size());

        // Verify first admin
        Admin admin1 = admins.get(0);

        assertEquals(
                "ADMINISTRATOR",
                admin1.getName()
        );

        assertEquals(
                "admin@ecommerce.com",
                admin1.getEmail()
        );

        assertEquals(
                "9876543210",
                admin1.getPhone()
        );

        assertNotNull(admin1.getId());
        assertNotNull(admin1.getPassword());


        // Verify second admin
        Admin admin2 = admins.get(1);

        assertEquals(
                "Naveen",
                admin2.getName()
        );

        assertEquals(
                "naveen@ecommerce.com",
                admin2.getEmail()
        );

        assertEquals(
                "9876543211",
                admin2.getPhone()
        );

        assertNotNull(admin2.getId());
        assertNotNull(admin2.getPassword());


        // Verify third admin
        Admin admin3 = admins.get(2);

        assertEquals(
                "Rahul",
                admin3.getName()
        );

        assertEquals(
                "rahul@ecommerce.com",
                admin3.getEmail()
        );

        assertEquals(
                "9876543212",
                admin3.getPhone()
        );

        assertNotNull(admin3.getId());
        assertNotNull(admin3.getPassword());
    }


    // ==========================================================
    // DATABASE ALREADY HAS ADMINS
    // ==========================================================

    @Test
    void shouldNotLoadAdminsWhenDatabaseAlreadyContainsAdmins() {

        // Given
        when(adminRepository.count())
                .thenReturn(3L);

        // When
        adminDataLoader.loadAdmins();

        // Then
        verify(adminRepository)
                .count();

        verify(adminRepository, never())
                .saveAll(anyList());
    }


    // ==========================================================
    // DATABASE CONTAINS ONE OR MORE ADMINS
    // ==========================================================

    @Test
    void shouldNotLoadAdminsWhenAtLeastOneAdminExists() {

        // Given
        when(adminRepository.count())
                .thenReturn(1L);

        // When
        adminDataLoader.loadAdmins();

        // Then
        verify(adminRepository)
                .count();

        verify(adminRepository, never())
                .saveAll(anyList());
    }
}