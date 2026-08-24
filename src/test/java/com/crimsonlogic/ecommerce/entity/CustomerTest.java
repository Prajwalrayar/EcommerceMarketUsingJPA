package com.crimsonlogic.ecommerce.entity;

import com.crimsonlogic.ecommerce.enumeration.Role;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    void shouldCreateCustomerWithCustomerRole() {

        Customer customer = new Customer();

        assertNotNull(customer);
        assertEquals(Role.CUSTOMER, customer.getRole());
    }

    @Test
    void shouldInitializeWalletBalanceToZero() {

        Customer customer = new Customer();

        assertEquals(BigDecimal.ZERO, customer.getWalletBalance());
    }

    @Test
    void shouldSetAndGetWalletBalance() {

        Customer customer = new Customer();

        BigDecimal balance = new BigDecimal("5000.00");

        customer.setWalletBalance(balance);

        assertEquals(balance, customer.getWalletBalance());
    }

    @Test
    void shouldInitializeAddressesAsEmptySet() {

        Customer customer = new Customer();

        assertNotNull(customer.getAddresses());
        assertTrue(customer.getAddresses().isEmpty());
    }

    @Test
    void shouldSetAndGetAddresses() {

        Customer customer = new Customer();

        Set<Address> addresses = new HashSet<>();

        Address address1 = new Address();
        Address address2 = new Address();

        addresses.add(address1);
        addresses.add(address2);

        customer.setAddresses(addresses);

        assertEquals(addresses, customer.getAddresses());
        assertEquals(2, customer.getAddresses().size());
    }

    @Test
    void shouldAddAddress() {

        Customer customer = new Customer();

        Address address = new Address();

        customer.addAddress(address);

        assertTrue(customer.getAddresses().contains(address));
        assertEquals(1, customer.getAddresses().size());
    }

    @Test
    void shouldNotAddNullAddress() {

        Customer customer = new Customer();

        customer.addAddress(null);

        assertTrue(customer.getAddresses().isEmpty());
    }

    @Test
    void shouldRemoveAddress() {

        Customer customer = new Customer();

        Address address = new Address();

        customer.addAddress(address);

        assertTrue(customer.getAddresses().contains(address));

        customer.removeAddress(address);

        assertFalse(customer.getAddresses().contains(address));
        assertTrue(customer.getAddresses().isEmpty());
    }

    @Test
    void shouldNotRemoveNullAddress() {

        Customer customer = new Customer();

        Address address = new Address();

        customer.addAddress(address);

        customer.removeAddress(null);

        assertTrue(customer.getAddresses().contains(address));
        assertEquals(1, customer.getAddresses().size());
    }

    @Test
    void shouldNotAddDuplicateAddress() {

        Customer customer = new Customer();

        Address address = new Address();

        customer.addAddress(address);
        customer.addAddress(address);

        assertEquals(1, customer.getAddresses().size());
    }
}