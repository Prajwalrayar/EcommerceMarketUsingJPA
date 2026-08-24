package com.crimsonlogic.ecommerce.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {

    @Test
    void shouldCreateInventoryWithDefaultValues() {

        Inventory inventory = new Inventory();

        assertNotNull(inventory);
        assertNull(inventory.getId());
        assertNull(inventory.getProduct());
        assertEquals(0, inventory.getQuantity());
    }

    @Test
    void shouldSetAndGetId() {

        Inventory inventory = new Inventory();

        inventory.setId("INV001");

        assertEquals("INV001", inventory.getId());
    }

    @Test
    void shouldSetAndGetProduct() {

        Inventory inventory = new Inventory();
        Product product = new Product();

        inventory.setProduct(product);

        assertEquals(product, inventory.getProduct());
    }

    @Test
    void shouldSetAndGetQuantity() {

        Inventory inventory = new Inventory();

        inventory.setQuantity(50);

        assertEquals(50, inventory.getQuantity());
    }

    @Test
    void shouldSetAndGetCompleteInventory() {

        Inventory inventory = new Inventory();

        Product product = new Product();

        inventory.setId("INV001");
        inventory.setProduct(product);
        inventory.setQuantity(100);

        assertEquals("INV001", inventory.getId());
        assertEquals(product, inventory.getProduct());
        assertEquals(100, inventory.getQuantity());
    }
}