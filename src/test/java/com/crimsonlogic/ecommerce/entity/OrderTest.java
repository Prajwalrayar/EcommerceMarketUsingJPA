package com.crimsonlogic.ecommerce.entity;

import com.crimsonlogic.ecommerce.enumeration.OrderStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void shouldCreateOrderWithDefaultValues() {

        Order order = new Order();

        assertNotNull(order);
        assertNull(order.getId());
        assertNull(order.getCustomer());
        assertNull(order.getProduct());
        assertEquals(0, order.getQuantity());
        assertNull(order.getTotalPrice());
        assertNull(order.getStatus());
        assertNull(order.getOrderDate());
        assertNull(order.getDeliveredDate());
        assertNull(order.getTrackingNumber());
        assertNull(order.getShippingAddress());
    }

    @Test
    void shouldSetAndGetId() {

        Order order = new Order();

        order.setId("ORD001");

        assertEquals("ORD001", order.getId());
    }

    @Test
    void shouldSetAndGetCustomer() {

        Order order = new Order();
        Customer customer = new Customer();

        order.setCustomer(customer);

        assertEquals(customer, order.getCustomer());
    }

    @Test
    void shouldSetAndGetProduct() {

        Order order = new Order();
        Product product = new Product();

        order.setProduct(product);

        assertEquals(product, order.getProduct());
    }

    @Test
    void shouldSetAndGetQuantity() {

        Order order = new Order();

        order.setQuantity(5);

        assertEquals(5, order.getQuantity());
    }

    @Test
    void shouldSetAndGetTotalPrice() {

        Order order = new Order();

        order.setTotalPrice(3250.50);

        assertEquals(3250.50, order.getTotalPrice());
    }

    @Test
    void shouldSetAndGetStatus() {

        Order order = new Order();

        order.setStatus(OrderStatus.PENDING_APPROVAL);

        assertEquals(OrderStatus.PENDING_APPROVAL, order.getStatus());
    }

    @Test
    void shouldSetAndGetOrderDate() {

        Order order = new Order();

        LocalDateTime orderDate =
                LocalDateTime.of(2026, 8, 24, 10, 30);

        order.setOrderDate(orderDate);

        assertEquals(orderDate, order.getOrderDate());
    }

    @Test
    void shouldSetAndGetDeliveredDate() {

        Order order = new Order();

        LocalDateTime deliveredDate =
                LocalDateTime.of(2026, 8, 26, 15, 45);

        order.setDeliveredDate(deliveredDate);

        assertEquals(deliveredDate, order.getDeliveredDate());
    }

    @Test
    void shouldSetAndGetTrackingNumber() {

        Order order = new Order();

        order.setTrackingNumber("TRK123456");

        assertEquals("TRK123456", order.getTrackingNumber());
    }

    @Test
    void shouldSetAndGetShippingAddress() {

        Order order = new Order();
        Address address = new Address();

        order.setShippingAddress(address);

        assertEquals(address, order.getShippingAddress());
    }

    @Test
    void shouldSetAndGetCompleteOrder() {

        Order order = new Order();

        Customer customer = new Customer();
        Product product = new Product();
        Address address = new Address();

        LocalDateTime orderDate =
                LocalDateTime.of(2026, 8, 24, 10, 30);

        LocalDateTime deliveredDate =
                LocalDateTime.of(2026, 8, 26, 15, 45);

        order.setId("ORD001");
        order.setCustomer(customer);
        order.setProduct(product);
        order.setQuantity(2);
        order.setTotalPrice(5000.00);
        order.setStatus(OrderStatus.PENDING_APPROVAL);
        order.setOrderDate(orderDate);
        order.setDeliveredDate(deliveredDate);
        order.setTrackingNumber("TRK123456");
        order.setShippingAddress(address);

        assertEquals("ORD001", order.getId());
        assertEquals(customer, order.getCustomer());
        assertEquals(product, order.getProduct());
        assertEquals(2, order.getQuantity());
        assertEquals(5000.00, order.getTotalPrice());
        assertEquals(OrderStatus.PENDING_APPROVAL, order.getStatus());
        assertEquals(orderDate, order.getOrderDate());
        assertEquals(deliveredDate, order.getDeliveredDate());
        assertEquals("TRK123456", order.getTrackingNumber());
        assertEquals(address, order.getShippingAddress());
    }
}