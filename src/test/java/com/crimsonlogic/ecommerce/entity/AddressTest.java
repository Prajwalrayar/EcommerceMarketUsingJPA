package com.crimsonlogic.ecommerce.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class AddressTest {

    @Test
    void shouldCreateAddressWithNullValues() {

        Address address = new Address();

        assertNotNull(address);

        assertNull(address.getId());
        assertNull(address.getHouseNumber());
        assertNull(address.getStreet());
        assertNull(address.getCity());
        assertNull(address.getState());
        assertNull(address.getCountry());
        assertNull(address.getZipCode());
    }

    @Test
    void shouldSetAndGetId() {

        Address address = new Address();

        address.setId("ADDR001");

        assertEquals("ADDR001", address.getId());
    }

    @Test
    void shouldSetAndGetHouseNumber() {

        Address address = new Address();

        address.setHouseNumber("12A");

        assertEquals("12A", address.getHouseNumber());
    }

    @Test
    void shouldSetAndGetStreet() {

        Address address = new Address();

        address.setStreet("MG Road");

        assertEquals("MG Road", address.getStreet());
    }

    @Test
    void shouldSetAndGetCity() {

        Address address = new Address();

        address.setCity("Bengaluru");

        assertEquals("Bengaluru", address.getCity());
    }

    @Test
    void shouldSetAndGetState() {

        Address address = new Address();

        address.setState("Karnataka");

        assertEquals("Karnataka", address.getState());
    }

    @Test
    void shouldSetAndGetCountry() {

        Address address = new Address();

        address.setCountry("India");

        assertEquals("India", address.getCountry());
    }

    @Test
    void shouldSetAndGetZipCode() {

        Address address = new Address();

        address.setZipCode("560001");

        assertEquals("560001", address.getZipCode());
    }

    @Test
    void shouldSetAndGetCompleteAddress() {

        Address address = new Address();

        address.setId("ADDR001");
        address.setHouseNumber("12A");
        address.setStreet("MG Road");
        address.setCity("Bengaluru");
        address.setState("Karnataka");
        address.setCountry("India");
        address.setZipCode("560001");

        assertEquals("ADDR001", address.getId());
        assertEquals("12A", address.getHouseNumber());
        assertEquals("MG Road", address.getStreet());
        assertEquals("Bengaluru", address.getCity());
        assertEquals("Karnataka", address.getState());
        assertEquals("India", address.getCountry());
        assertEquals("560001", address.getZipCode());
    }
}