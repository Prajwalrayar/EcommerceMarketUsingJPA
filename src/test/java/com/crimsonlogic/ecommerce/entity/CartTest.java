package com.crimsonlogic.ecommerce.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CartTest {

    @Test
    void shouldSetAndGetId() {

        Cart cart = new Cart();

        cart.setId("CART001");

        assertEquals("CART001", cart.getId());
    }

    @Test
    void shouldSetAndGetCustomer() {

        Cart cart = new Cart();
        Customer customer = new Customer();

        cart.setCustomer(customer);

        assertEquals(customer, cart.getCustomer());
    }

    @Test
    void shouldSetAndGetProduct() {

        Cart cart = new Cart();
        Product product = new Product();

        cart.setProduct(product);

        assertEquals(product, cart.getProduct());
    }

    @Test
    void shouldSetAndGetQuantity() {

        Cart cart = new Cart();

        cart.setQuantity(5);

        assertEquals(5, cart.getQuantity());
    }

    @Test
    void shouldCalculateTotalPrice() {

        Cart cart = new Cart();

        Product product = new Product();
        product.setPrice(500.0);

        cart.setProduct(product);
        cart.setQuantity(3);

        assertEquals(1500.0, cart.getTotalPrice(), 0.001);
    }

    @Test
    void shouldReturnZeroWhenProductIsNull() {

        Cart cart = new Cart();

        cart.setQuantity(3);
        cart.setProduct(null);

        assertEquals(0.0, cart.getTotalPrice(), 0.001);
    }

    @Test
    void shouldReturnZeroWhenProductPriceIsNull() {

        Cart cart = new Cart();

        Product product = new Product();
        product.setPrice(null);

        cart.setProduct(product);
        cart.setQuantity(3);

        assertEquals(0.0, cart.getTotalPrice(), 0.001);
    }

    @Test
    void shouldCalculateTotalPriceForQuantityOne() {

        Cart cart = new Cart();

        Product product = new Product();
        product.setPrice(65000.0);

        cart.setProduct(product);
        cart.setQuantity(1);

        assertEquals(65000.0, cart.getTotalPrice(), 0.001);
    }

    @Test
    void shouldCalculateZeroTotalForZeroQuantity() {

        Cart cart = new Cart();

        Product product = new Product();
        product.setPrice(500.0);

        cart.setProduct(product);
        cart.setQuantity(0);

        assertEquals(0.0, cart.getTotalPrice(), 0.001);
    }
}