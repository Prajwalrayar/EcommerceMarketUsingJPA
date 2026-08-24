package com.crimsonlogic.ecommerce.entity;

import com.crimsonlogic.ecommerce.enumeration.PaymentMethod;
import com.crimsonlogic.ecommerce.enumeration.PaymentStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    @Test
    void shouldCreatePaymentWithDefaultValues() {

        Payment payment = new Payment();

        assertNotNull(payment);

        assertNull(payment.getId());
        assertNull(payment.getTransactionId());
        assertNull(payment.getCustomer());
        assertNull(payment.getOrder());
        assertNull(payment.getPaymentMethod());
        assertNull(payment.getPaymentStatus());
        assertNull(payment.getAmount());
        assertNull(payment.getUpiId());
        assertNull(payment.getPaymentDate());
    }

    @Test
    void shouldSetAndGetId() {

        Payment payment = new Payment();

        payment.setId("PAY001");

        assertEquals("PAY001", payment.getId());
    }

    @Test
    void shouldSetAndGetTransactionId() {

        Payment payment = new Payment();

        payment.setTransactionId("TXN001");

        assertEquals("TXN001", payment.getTransactionId());
    }

    @Test
    void shouldSetAndGetCustomer() {

        Payment payment = new Payment();
        Customer customer = new Customer();

        payment.setCustomer(customer);

        assertEquals(customer, payment.getCustomer());
    }

    @Test
    void shouldSetAndGetOrder() {

        Payment payment = new Payment();
        Order order = new Order();

        payment.setOrder(order);

        assertEquals(order, payment.getOrder());
    }

    @Test
    void shouldSetAndGetPaymentMethod() {

        Payment payment = new Payment();

        payment.setPaymentMethod(PaymentMethod.UPI);

        assertEquals(
                PaymentMethod.UPI,
                payment.getPaymentMethod()
        );
    }

    @Test
    void shouldSetAndGetPaymentStatus() {

        Payment payment = new Payment();

        payment.setPaymentStatus(PaymentStatus.SUCCESS);

        assertEquals(
                PaymentStatus.SUCCESS,
                payment.getPaymentStatus()
        );
    }

    @Test
    void shouldSetAndGetAmount() {

        Payment payment = new Payment();

        payment.setAmount(5000.00);

        assertEquals(5000.00, payment.getAmount());
    }

    @Test
    void shouldSetAndGetUpiId() {

        Payment payment = new Payment();

        payment.setUpiId("prajwal@upi");

        assertEquals("prajwal@upi", payment.getUpiId());
    }

    @Test
    void shouldSetAndGetPaymentDate() {

        Payment payment = new Payment();

        LocalDateTime paymentDate =
                LocalDateTime.of(2026, 8, 24, 14, 30);

        payment.setPaymentDate(paymentDate);

        assertEquals(paymentDate, payment.getPaymentDate());
    }

    @Test
    void shouldSetAndGetCompletePayment() {

        Payment payment = new Payment();

        Customer customer = new Customer();
        Order order = new Order();

        LocalDateTime paymentDate =
                LocalDateTime.of(2026, 8, 24, 14, 30);

        payment.setId("PAY001");
        payment.setTransactionId("TXN001");
        payment.setCustomer(customer);
        payment.setOrder(order);
        payment.setPaymentMethod(PaymentMethod.UPI);
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setAmount(5000.00);
        payment.setUpiId("prajwal@upi");
        payment.setPaymentDate(paymentDate);

        assertEquals("PAY001", payment.getId());
        assertEquals("TXN001", payment.getTransactionId());
        assertEquals(customer, payment.getCustomer());
        assertEquals(order, payment.getOrder());
        assertEquals(PaymentMethod.UPI, payment.getPaymentMethod());
        assertEquals(PaymentStatus.SUCCESS, payment.getPaymentStatus());
        assertEquals(5000.00, payment.getAmount());
        assertEquals("ram@upi", payment.getUpiId());
        assertEquals(paymentDate, payment.getPaymentDate());
    }
}