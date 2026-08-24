package com.crimsonlogic.ecommerce.service.impl;

import com.crimsonlogic.ecommerce.dto.admin.AdminDashboardStatsDTO;
import com.crimsonlogic.ecommerce.entity.Customer;
import com.crimsonlogic.ecommerce.entity.Order;
import com.crimsonlogic.ecommerce.entity.Seller;
import com.crimsonlogic.ecommerce.exception.ValidationException;
import com.crimsonlogic.ecommerce.repository.CustomerRepository;
import com.crimsonlogic.ecommerce.repository.OrderRepository;
import com.crimsonlogic.ecommerce.repository.ProductRepository;
import com.crimsonlogic.ecommerce.repository.SellerRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;


/**
 * Unit tests for AdminServiceImpl.
 *
 * Mockito is used to mock all repository dependencies.
 *
 * We are testing AdminServiceImpl directly.
 */
@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {

    // ==========================================================
    // MOCK DEPENDENCIES
    // ==========================================================

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderRepository orderRepository;


    // ==========================================================
    // SERVICE UNDER TEST
    // ==========================================================

    @InjectMocks
    private AdminServiceImpl adminService;


    // ==========================================================
    // GET DASHBOARD STATS
    // ==========================================================

    @Test
    void shouldGetDashboardStatsSuccessfully() {

        // ------------------------------------------------------
        // Arrange
        // ------------------------------------------------------

        when(customerRepository.count())
                .thenReturn(10L);

        when(sellerRepository.count())
                .thenReturn(5L);

        when(productRepository.count())
                .thenReturn(50L);

        when(orderRepository.count())
                .thenReturn(20L);


        Order order1 = new Order();
        order1.setTotalPrice(1000.0);

        Order order2 = new Order();
        order2.setTotalPrice(2500.0);

        Order order3 = new Order();
        order3.setTotalPrice(null);


        when(orderRepository.findAll())
                .thenReturn(Arrays.asList(
                        order1,
                        order2,
                        order3
                ));


        // ------------------------------------------------------
        // Act
        // ------------------------------------------------------

        AdminDashboardStatsDTO result =
                adminService.getDashboardStats();


        // ------------------------------------------------------
        // Assert
        // ------------------------------------------------------

        assertEquals(
                10L,
                result.getTotalCustomers()
        );

        assertEquals(
                5L,
                result.getTotalSellers()
        );

        assertEquals(
                50L,
                result.getTotalProducts()
        );

        assertEquals(
                20L,
                result.getTotalOrders()
        );

        /*
         * 1000 + 2500 + 0
         *
         * = 3500
         */
        assertEquals(
                3500.0,
                result.getTotalPlatformSales()
        );


        // ------------------------------------------------------
        // Verify repository calls
        // ------------------------------------------------------

        verify(customerRepository)
                .count();

        verify(sellerRepository)
                .count();

        verify(productRepository)
                .count();

        verify(orderRepository)
                .count();

        verify(orderRepository)
                .findAll();
    }


    // ==========================================================
    // DASHBOARD STATS - NO ORDERS
    // ==========================================================

    @Test
    void shouldReturnZeroSalesWhenThereAreNoOrders() {

        // ------------------------------------------------------
        // Arrange
        // ------------------------------------------------------

        when(customerRepository.count())
                .thenReturn(0L);

        when(sellerRepository.count())
                .thenReturn(0L);

        when(productRepository.count())
                .thenReturn(0L);

        when(orderRepository.count())
                .thenReturn(0L);

        when(orderRepository.findAll())
                .thenReturn(Collections.emptyList());


        // ------------------------------------------------------
        // Act
        // ------------------------------------------------------

        AdminDashboardStatsDTO result =
                adminService.getDashboardStats();


        // ------------------------------------------------------
        // Assert
        // ------------------------------------------------------

        assertEquals(
                0L,
                result.getTotalCustomers()
        );

        assertEquals(
                0L,
                result.getTotalSellers()
        );

        assertEquals(
                0L,
                result.getTotalProducts()
        );

        assertEquals(
                0L,
                result.getTotalOrders()
        );

        assertEquals(
                0.0,
                result.getTotalPlatformSales()
        );
    }


    // ==========================================================
    // DELETE CUSTOMER SUCCESSFULLY
    // ==========================================================

    @Test
    void shouldDeleteCustomerSuccessfully() {

        // ------------------------------------------------------
        // Arrange
        // ------------------------------------------------------

        Customer customer = new Customer();

        customer.setId("CUS001");


        when(customerRepository.findById("CUS001"))
                .thenReturn(Optional.of(customer));


        // ------------------------------------------------------
        // Act
        // ------------------------------------------------------

        String result =
                adminService.deleteUserByAdmin(
                        "CUS001",
                        "CUSTOMER"
                );


        // ------------------------------------------------------
        // Assert
        // ------------------------------------------------------

        assertEquals(
                "Customer account successfully removed by admin.",
                result
        );


        // ------------------------------------------------------
        // Verify
        // ------------------------------------------------------

        verify(customerRepository)
                .findById("CUS001");

        verify(customerRepository)
                .delete(customer);


        // Other repositories should not be touched
        verifyNoInteractions(
                sellerRepository,
                productRepository,
                orderRepository
        );
    }


    // ==========================================================
    // DELETE SELLER SUCCESSFULLY
    // ==========================================================

    @Test
    void shouldDeleteSellerSuccessfully() {

        // ------------------------------------------------------
        // Arrange
        // ------------------------------------------------------

        Seller seller = new Seller();

        seller.setId("SEL001");


        when(sellerRepository.findById("SEL001"))
                .thenReturn(Optional.of(seller));


        // ------------------------------------------------------
        // Act
        // ------------------------------------------------------

        String result =
                adminService.deleteUserByAdmin(
                        "SEL001",
                        "SELLER"
                );


        // ------------------------------------------------------
        // Assert
        // ------------------------------------------------------

        assertEquals(
                "Seller account successfully removed by admin.",
                result
        );


        // ------------------------------------------------------
        // Verify
        // ------------------------------------------------------

        verify(sellerRepository)
                .findById("SEL001");

        verify(sellerRepository)
                .delete(seller);


        verifyNoInteractions(
                customerRepository,
                productRepository,
                orderRepository
        );
    }


    // ==========================================================
    // CUSTOMER NOT FOUND
    // ==========================================================

    @Test
    void shouldThrowExceptionWhenCustomerDoesNotExist() {

        // ------------------------------------------------------
        // Arrange
        // ------------------------------------------------------

        when(customerRepository.findById("CUS999"))
                .thenReturn(Optional.empty());


        // ------------------------------------------------------
        // Act + Assert
        // ------------------------------------------------------

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> adminService.deleteUserByAdmin(
                                "CUS999",
                                "CUSTOMER"
                        )
                );


        // ------------------------------------------------------
        // Verify exception message
        // ------------------------------------------------------

        assertEquals(
                "Customer not found.",
                exception.getMessage()
        );


        // ------------------------------------------------------
        // Verify repository interaction
        // ------------------------------------------------------

        verify(customerRepository)
                .findById("CUS999");


        verify(
                customerRepository,
                never()
        ).delete(any(Customer.class));
    }


    // ==========================================================
    // SELLER NOT FOUND
    // ==========================================================

    @Test
    void shouldThrowExceptionWhenSellerDoesNotExist() {

        // ------------------------------------------------------
        // Arrange
        // ------------------------------------------------------

        when(sellerRepository.findById("SEL999"))
                .thenReturn(Optional.empty());


        // ------------------------------------------------------
        // Act + Assert
        // ------------------------------------------------------

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> adminService.deleteUserByAdmin(
                                "SEL999",
                                "SELLER"
                        )
                );


        // ------------------------------------------------------
        // Assert exception message
        // ------------------------------------------------------

        assertEquals(
                "Seller not found.",
                exception.getMessage()
        );


        // ------------------------------------------------------
        // Verify
        // ------------------------------------------------------

        verify(sellerRepository)
                .findById("SEL999");


        verify(
                sellerRepository,
                never()
        ).delete(any(Seller.class));
    }


    // ==========================================================
    // INVALID ROLE
    // ==========================================================

    @Test
    void shouldThrowExceptionForInvalidRole() {

        // ------------------------------------------------------
        // Act + Assert
        // ------------------------------------------------------

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> adminService.deleteUserByAdmin(
                                "USR001",
                                "ADMIN"
                        )
                );


        // ------------------------------------------------------
        // Assert message
        // ------------------------------------------------------

        assertEquals(
                "Invalid user role specified for deletion.",
                exception.getMessage()
        );


        // ------------------------------------------------------
        // No repository should be called
        // ------------------------------------------------------

        verifyNoInteractions(
                customerRepository,
                sellerRepository,
                productRepository,
                orderRepository
        );
    }


    // ==========================================================
    // CUSTOMER ROLE - CASE INSENSITIVE
    // ==========================================================

    @Test
    void shouldDeleteCustomerWhenRoleIsLowerCase() {

        // ------------------------------------------------------
        // Arrange
        // ------------------------------------------------------

        Customer customer = new Customer();

        customer.setId("CUS001");


        when(customerRepository.findById("CUS001"))
                .thenReturn(Optional.of(customer));


        // ------------------------------------------------------
        // Act
        // ------------------------------------------------------

        String result =
                adminService.deleteUserByAdmin(
                        "CUS001",
                        "customer"
                );


        // ------------------------------------------------------
        // Assert
        // ------------------------------------------------------

        assertEquals(
                "Customer account successfully removed by admin.",
                result
        );


        verify(customerRepository)
                .findById("CUS001");

        verify(customerRepository)
                .delete(customer);
    }


    // ==========================================================
    // SELLER ROLE - CASE INSENSITIVE
    // ==========================================================

    @Test
    void shouldDeleteSellerWhenRoleIsLowerCase() {

        // ------------------------------------------------------
        // Arrange
        // ------------------------------------------------------

        Seller seller = new Seller();

        seller.setId("SEL001");


        when(sellerRepository.findById("SEL001"))
                .thenReturn(Optional.of(seller));


        // ------------------------------------------------------
        // Act
        // ------------------------------------------------------

        String result =
                adminService.deleteUserByAdmin(
                        "SEL001",
                        "seller"
                );


        // ------------------------------------------------------
        // Assert
        // ------------------------------------------------------

        assertEquals(
                "Seller account successfully removed by admin.",
                result
        );


        verify(sellerRepository)
                .findById("SEL001");

        verify(sellerRepository)
                .delete(seller);
    }
}