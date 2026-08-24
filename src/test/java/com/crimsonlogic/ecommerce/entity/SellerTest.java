package com.crimsonlogic.ecommerce.entity;

import com.crimsonlogic.ecommerce.enumeration.Role;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SellerTest {

    @Test
    void shouldCreateSellerWithSellerRole() {

        Seller seller = new Seller();

        assertNotNull(seller);
        assertEquals(Role.SELLER, seller.getRole());
    }

    @Test
    void shouldSetAndGetShopName() {

        Seller seller = new Seller();

        seller.setShopName("Prajwal Electronics");

        assertEquals("Prajwal Electronics", seller.getShopName());
    }

    @Test
    void shouldInitializeAddressesAsEmptySet() {

        Seller seller = new Seller();

        assertNotNull(seller.getAddresses());
        assertTrue(seller.getAddresses().isEmpty());
    }

    @Test
    void shouldSetAndGetAddresses() {

        Seller seller = new Seller();

        Set<Address> addresses = new HashSet<>();

        Address address1 = new Address();
        Address address2 = new Address();

        addresses.add(address1);
        addresses.add(address2);

        seller.setAddresses(addresses);

        assertNotNull(seller.getAddresses());
        assertEquals(2, seller.getAddresses().size());
        assertEquals(addresses, seller.getAddresses());
    }

    @Test
    void shouldAddAddress() {

        Seller seller = new Seller();

        Address address = new Address();

        seller.addAddress(address);

        assertTrue(seller.getAddresses().contains(address));
        assertEquals(1, seller.getAddresses().size());
    }

    @Test
    void shouldNotAddNullAddress() {

        Seller seller = new Seller();

        seller.addAddress(null);

        assertNotNull(seller.getAddresses());
        assertTrue(seller.getAddresses().isEmpty());
    }

    @Test
    void shouldRemoveAddress() {

        Seller seller = new Seller();

        Address address = new Address();

        seller.addAddress(address);

        assertTrue(seller.getAddresses().contains(address));

        seller.removeAddress(address);

        assertFalse(seller.getAddresses().contains(address));
        assertTrue(seller.getAddresses().isEmpty());
    }

    @Test
    void shouldNotRemoveNullAddress() {

        Seller seller = new Seller();

        Address address = new Address();

        seller.addAddress(address);

        seller.removeAddress(null);

        assertTrue(seller.getAddresses().contains(address));
        assertEquals(1, seller.getAddresses().size());
    }

    @Test
    void shouldNotAddDuplicateAddress() {

        Seller seller = new Seller();

        Address address = new Address();

        seller.addAddress(address);
        seller.addAddress(address);

        assertEquals(1, seller.getAddresses().size());
    }
}