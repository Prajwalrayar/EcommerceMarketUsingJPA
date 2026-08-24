package com.crimsonlogic.ecommerce.service.impl;

import com.crimsonlogic.ecommerce.dto.order.CheckoutRequestDTO;
import com.crimsonlogic.ecommerce.entity.Cart;
import com.crimsonlogic.ecommerce.entity.Customer;
import com.crimsonlogic.ecommerce.entity.Inventory;
import com.crimsonlogic.ecommerce.entity.Order;
import com.crimsonlogic.ecommerce.entity.Product;
import com.crimsonlogic.ecommerce.entity.Payment;
import com.crimsonlogic.ecommerce.enumeration.OrderStatus;
import com.crimsonlogic.ecommerce.enumeration.PaymentMethod;
import com.crimsonlogic.ecommerce.enumeration.PaymentStatus;
import com.crimsonlogic.ecommerce.exception.ValidationException;
import com.crimsonlogic.ecommerce.repository.CartRepository;
import com.crimsonlogic.ecommerce.repository.CustomerRepository;
import com.crimsonlogic.ecommerce.repository.InventoryRepository;
import com.crimsonlogic.ecommerce.repository.OrderRepository;
import com.crimsonlogic.ecommerce.repository.PaymentRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private CheckoutServiceImpl checkoutService;

    private Customer customer;
    private Product product;
    private Inventory inventory;
    private Cart cart;

    @BeforeEach
    void setUp() {

        customer = new Customer();
        customer.setId("CUS001");
        customer.setName("Prajwal");
        customer.setWalletBalance(
                BigDecimal.valueOf(100000)
        );

        product = new Product();
        product.setId("PROD001");
        product.setName("Laptop");
        product.setPrice(65000.0);

        inventory = new Inventory();
        inventory.setId("INV001");
        inventory.setProduct(product);
        inventory.setQuantity(10);

        cart = new Cart();
        cart.setId("CRT001");
        cart.setCustomer(customer);
        cart.setProduct(product);
        cart.setQuantity(2);
    }

    // ==========================================================
    // SUCCESSFUL CHECKOUT - CASH ON DELIVERY
    // ==========================================================

    @Test
    void shouldCheckoutSuccessfullyUsingCashOnDelivery()
            throws Exception {

        CheckoutRequestDTO request =
                new CheckoutRequestDTO();

        request.setPaymentMethod(
                "CASH_ON_DELIVERY"
        );

        when(customerRepository.findById("CUS001"))
                .thenReturn(Optional.of(customer));

        when(cartRepository.findByCustomerId("CUS001"))
                .thenReturn(Arrays.asList(cart));

        when(inventoryRepository.findByProductId("PROD001"))
                .thenReturn(Optional.of(inventory));

        Order savedOrder = new Order();
        savedOrder.setId("ORD001");

        when(orderRepository.save(any(Order.class)))
                .thenReturn(savedOrder);

        String result =
                checkoutService.checkout(
                        "CUS001",
                        request
                );

        assertEquals(
                "Checkout successful! Orders have been placed.",
                result
        );

        assertEquals(
                8,
                inventory.getQuantity()
        );

        verify(customerRepository)
                .findById("CUS001");

        verify(cartRepository)
                .findByCustomerId("CUS001");

        verify(inventoryRepository)
                .findByProductId("PROD001");

        verify(inventoryRepository)
                .save(inventory);

        verify(orderRepository)
                .save(any(Order.class));

        verify(paymentRepository)
                .save(any(Payment.class));

        verify(cartRepository)
                .deleteByCustomerId("CUS001");
    }

    // ==========================================================
    // SUCCESSFUL CHECKOUT - UPI
    // ==========================================================

    @Test
    void shouldCheckoutSuccessfullyUsingUPI() {

        CheckoutRequestDTO request =
                new CheckoutRequestDTO();

        request.setPaymentMethod("UPI");
        request.setUpiId("prajwal@upi");

        when(customerRepository.findById("CUS001"))
                .thenReturn(Optional.of(customer));

        when(cartRepository.findByCustomerId("CUS001"))
                .thenReturn(Arrays.asList(cart));

        when(inventoryRepository.findByProductId("PROD001"))
                .thenReturn(Optional.of(inventory));

        Order savedOrder = new Order();
        savedOrder.setId("ORD001");

        when(orderRepository.save(any(Order.class)))
                .thenReturn(savedOrder);

        String result =
                checkoutService.checkout(
                        "CUS001",
                        request
                );

        assertEquals(
                "Checkout successful! Orders have been placed.",
                result
        );

        verify(paymentRepository)
                .save(any(Payment.class));

        verify(cartRepository)
                .deleteByCustomerId("CUS001");
    }

    // ==========================================================
    // SUCCESSFUL CHECKOUT - WALLET
    // ==========================================================

    @Test
    void shouldCheckoutSuccessfullyUsingWallet() {

        customer.setWalletBalance(
                BigDecimal.valueOf(200000)
        );

        CheckoutRequestDTO request =
                new CheckoutRequestDTO();

        request.setPaymentMethod("WALLET");

        when(customerRepository.findById("CUS001"))
                .thenReturn(Optional.of(customer));

        when(cartRepository.findByCustomerId("CUS001"))
                .thenReturn(Arrays.asList(cart));

        when(inventoryRepository.findByProductId("PROD001"))
                .thenReturn(Optional.of(inventory));

        Order savedOrder = new Order();
        savedOrder.setId("ORD001");

        when(orderRepository.save(any(Order.class)))
                .thenReturn(savedOrder);

        checkoutService.checkout(
                "CUS001",
                request
        );

        // 2 × ₹65,000 = ₹130,000
        assertEquals(
                BigDecimal.valueOf(70000.0),
                customer.getWalletBalance()
        );

        verify(customerRepository)
                .save(customer);

        verify(paymentRepository)
                .save(any(Payment.class));
    }

    // ==========================================================
    // CUSTOMER NOT FOUND
    // ==========================================================

    @Test
    void shouldThrowExceptionWhenCustomerDoesNotExist() {

        CheckoutRequestDTO request =
                new CheckoutRequestDTO();

        request.setPaymentMethod("UPI");
        request.setUpiId("prajwal@upi");

        when(customerRepository.findById("CUS001"))
                .thenReturn(Optional.empty());

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> checkoutService.checkout(
                                "CUS001",
                                request
                        )
                );

        assertEquals(
                "Customer not found.",
                exception.getMessage()
        );

        verify(customerRepository)
                .findById("CUS001");

        verifyNoInteractions(
                cartRepository,
                orderRepository,
                paymentRepository,
                inventoryRepository
        );
    }

    // ==========================================================
    // EMPTY CART
    // ==========================================================

    @Test
    void shouldThrowExceptionWhenCartIsEmpty() {

        CheckoutRequestDTO request =
                new CheckoutRequestDTO();

        request.setPaymentMethod("UPI");
        request.setUpiId("prajwal@upi");

        when(customerRepository.findById("CUS001"))
                .thenReturn(Optional.of(customer));

        when(cartRepository.findByCustomerId("CUS001"))
                .thenReturn(Collections.emptyList());

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> checkoutService.checkout(
                                "CUS001",
                                request
                        )
                );

        assertEquals(
                "Cart is empty.",
                exception.getMessage()
        );

        verify(cartRepository)
                .findByCustomerId("CUS001");

        verifyNoInteractions(
                orderRepository,
                paymentRepository,
                inventoryRepository
        );
    }

    // ==========================================================
    // UPI ID MISSING
    // ==========================================================

    @Test
    void shouldThrowExceptionWhenUPIIdIsMissing() {

        CheckoutRequestDTO request =
                new CheckoutRequestDTO();

        request.setPaymentMethod("UPI");
        request.setUpiId(null);

        when(customerRepository.findById("CUS001"))
                .thenReturn(Optional.of(customer));

        when(cartRepository.findByCustomerId("CUS001"))
                .thenReturn(Arrays.asList(cart));

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> checkoutService.checkout(
                                "CUS001",
                                request
                        )
                );

        assertEquals(
                "UPI ID is required for UPI payments.",
                exception.getMessage()
        );

        verifyNoInteractions(
                orderRepository,
                paymentRepository,
                inventoryRepository
        );
    }

    // ==========================================================
    // INSUFFICIENT WALLET BALANCE
    // ==========================================================

    @Test
    void shouldThrowExceptionWhenWalletBalanceIsInsufficient() {

        customer.setWalletBalance(
                BigDecimal.valueOf(50000)
        );

        CheckoutRequestDTO request =
                new CheckoutRequestDTO();

        request.setPaymentMethod("WALLET");

        when(customerRepository.findById("CUS001"))
                .thenReturn(Optional.of(customer));

        when(cartRepository.findByCustomerId("CUS001"))
                .thenReturn(Arrays.asList(cart));

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> checkoutService.checkout(
                                "CUS001",
                                request
                        )
                );

        assertTrue(
                exception.getMessage()
                        .startsWith(
                                "Insufficient wallet balance."
                        )
        );

        verify(customerRepository, never())
                .save(any(Customer.class));

        verifyNoInteractions(
                inventoryRepository,
                orderRepository,
                paymentRepository
        );
    }

    // ==========================================================
    // INVENTORY NOT FOUND
    // ==========================================================

    @Test
    void shouldThrowExceptionWhenInventoryDoesNotExist() {

        CheckoutRequestDTO request =
                new CheckoutRequestDTO();

        request.setPaymentMethod("UPI");
        request.setUpiId("prajwal@upi");

        when(customerRepository.findById("CUS001"))
                .thenReturn(Optional.of(customer));

        when(cartRepository.findByCustomerId("CUS001"))
                .thenReturn(Arrays.asList(cart));

        when(inventoryRepository.findByProductId("PROD001"))
                .thenReturn(Optional.empty());

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> checkoutService.checkout(
                                "CUS001",
                                request
                        )
                );

        assertEquals(
                "Inventory error.",
                exception.getMessage()
        );

        verify(inventoryRepository)
                .findByProductId("PROD001");

        verifyNoInteractions(
                orderRepository,
                paymentRepository
        );
    }

    // ==========================================================
    // INSUFFICIENT INVENTORY
    // ==========================================================

    @Test
    void shouldThrowExceptionWhenProductIsOutOfStock() {

        inventory.setQuantity(1);

        CheckoutRequestDTO request =
                new CheckoutRequestDTO();

        request.setPaymentMethod("UPI");
        request.setUpiId("prajwal@upi");

        when(customerRepository.findById("CUS001"))
                .thenReturn(Optional.of(customer));

        when(cartRepository.findByCustomerId("CUS001"))
                .thenReturn(Arrays.asList(cart));

        when(inventoryRepository.findByProductId("PROD001"))
                .thenReturn(Optional.of(inventory));

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> checkoutService.checkout(
                                "CUS001",
                                request
                        )
                );

        assertEquals(
                "Product Laptop is out of stock.",
                exception.getMessage()
        );

        verify(inventoryRepository)
                .findByProductId("PROD001");

        verify(inventoryRepository, never())
                .save(any(Inventory.class));

        verifyNoInteractions(
                orderRepository,
                paymentRepository
        );
    }

    // ==========================================================
    // VERIFY ORDER DETAILS
    // ==========================================================

    @Test
    void shouldCreateOrderWithCorrectDetails() {

        CheckoutRequestDTO request =
                new CheckoutRequestDTO();

        request.setPaymentMethod("UPI");
        request.setUpiId("prajwal@upi");

        when(customerRepository.findById("CUS001"))
                .thenReturn(Optional.of(customer));

        when(cartRepository.findByCustomerId("CUS001"))
                .thenReturn(Arrays.asList(cart));

        when(inventoryRepository.findByProductId("PROD001"))
                .thenReturn(Optional.of(inventory));

        Order savedOrder = new Order();
        savedOrder.setId("ORD001");

        when(orderRepository.save(any(Order.class)))
                .thenReturn(savedOrder);

        checkoutService.checkout(
                "CUS001",
                request
        );

        ArgumentCaptor<Order> captor =
                ArgumentCaptor.forClass(Order.class);

        verify(orderRepository)
                .save(captor.capture());

        Order order = captor.getValue();

        assertEquals("CUS001", order.getCustomer().getId());
        assertEquals("PROD001", order.getProduct().getId());
        assertEquals(2, order.getQuantity());
        assertEquals(130000.0, order.getTotalPrice());
        assertEquals(
                OrderStatus.PENDING_APPROVAL,
                order.getStatus()
        );

        assertNotNull(order.getOrderDate());
    }

    // ==========================================================
    // VERIFY PAYMENT DETAILS
    // ==========================================================

    @Test
    void shouldCreateSuccessfulUPIPayment() {

        CheckoutRequestDTO request =
                new CheckoutRequestDTO();

        request.setPaymentMethod("UPI");
        request.setUpiId("prajwal@upi");

        when(customerRepository.findById("CUS001"))
                .thenReturn(Optional.of(customer));

        when(cartRepository.findByCustomerId("CUS001"))
                .thenReturn(Arrays.asList(cart));

        when(inventoryRepository.findByProductId("PROD001"))
                .thenReturn(Optional.of(inventory));

        Order savedOrder = new Order();
        savedOrder.setId("ORD001");

        when(orderRepository.save(any(Order.class)))
                .thenReturn(savedOrder);

        checkoutService.checkout(
                "CUS001",
                request
        );

        ArgumentCaptor<Payment> captor =
                ArgumentCaptor.forClass(Payment.class);

        verify(paymentRepository)
                .save(captor.capture());

        Payment payment = captor.getValue();

        assertEquals(
                PaymentMethod.UPI,
                payment.getPaymentMethod()
        );

        assertEquals(
                PaymentStatus.SUCCESS,
                payment.getPaymentStatus()
        );

        assertEquals(
                "prajwal@upi",
                payment.getUpiId()
        );

        assertEquals(
                130000.0,
                payment.getAmount()
        );
    }

    // ==========================================================
    // VERIFY CASH ON DELIVERY PAYMENT
    // ==========================================================

    @Test
    void shouldCreatePendingPaymentForCashOnDelivery() {

        CheckoutRequestDTO request =
                new CheckoutRequestDTO();

        request.setPaymentMethod(
                "CASH_ON_DELIVERY"
        );

        when(customerRepository.findById("CUS001"))
                .thenReturn(Optional.of(customer));

        when(cartRepository.findByCustomerId("CUS001"))
                .thenReturn(Arrays.asList(cart));

        when(inventoryRepository.findByProductId("PROD001"))
                .thenReturn(Optional.of(inventory));

        Order savedOrder = new Order();
        savedOrder.setId("ORD001");

        when(orderRepository.save(any(Order.class)))
                .thenReturn(savedOrder);

        checkoutService.checkout(
                "CUS001",
                request
        );

        ArgumentCaptor<Payment> captor =
                ArgumentCaptor.forClass(Payment.class);

        verify(paymentRepository)
                .save(captor.capture());

        Payment payment = captor.getValue();

        assertEquals(
                PaymentMethod.CASH_ON_DELIVERY,
                payment.getPaymentMethod()
        );

        assertEquals(
                PaymentStatus.PENDING,
                payment.getPaymentStatus()
        );
    }

    // ==========================================================
    // CART IS CLEARED AFTER CHECKOUT
    // ==========================================================

    @Test
    void shouldClearCartAfterSuccessfulCheckout() {

        CheckoutRequestDTO request =
                new CheckoutRequestDTO();

        request.setPaymentMethod("UPI");
        request.setUpiId("prajwal@upi");

        when(customerRepository.findById("CUS001"))
                .thenReturn(Optional.of(customer));

        when(cartRepository.findByCustomerId("CUS001"))
                .thenReturn(Arrays.asList(cart));

        when(inventoryRepository.findByProductId("PROD001"))
                .thenReturn(Optional.of(inventory));

        Order savedOrder = new Order();
        savedOrder.setId("ORD001");

        when(orderRepository.save(any(Order.class)))
                .thenReturn(savedOrder);

        checkoutService.checkout(
                "CUS001",
                request
        );

        verify(cartRepository)
                .deleteByCustomerId("CUS001");
    }
}