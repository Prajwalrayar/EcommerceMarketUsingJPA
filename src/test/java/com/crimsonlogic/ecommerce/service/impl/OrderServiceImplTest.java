package com.crimsonlogic.ecommerce.service.impl;

import com.crimsonlogic.ecommerce.dto.order.CheckoutRequestDTO;
import com.crimsonlogic.ecommerce.dto.order.OrderResponseDTO;
import com.crimsonlogic.ecommerce.entity.Address;
import com.crimsonlogic.ecommerce.entity.Cart;
import com.crimsonlogic.ecommerce.entity.Customer;
import com.crimsonlogic.ecommerce.entity.Inventory;
import com.crimsonlogic.ecommerce.entity.Order;
import com.crimsonlogic.ecommerce.entity.Payment;
import com.crimsonlogic.ecommerce.entity.Product;
import com.crimsonlogic.ecommerce.entity.Seller;
import com.crimsonlogic.ecommerce.enumeration.OrderStatus;
import com.crimsonlogic.ecommerce.enumeration.ProductStatus;
import com.crimsonlogic.ecommerce.exception.ValidationException;
import com.crimsonlogic.ecommerce.repository.AddressRepository;
import com.crimsonlogic.ecommerce.repository.CartRepository;
import com.crimsonlogic.ecommerce.repository.CustomerRepository;
import com.crimsonlogic.ecommerce.repository.InventoryRepository;
import com.crimsonlogic.ecommerce.repository.OrderRepository;
import com.crimsonlogic.ecommerce.repository.PaymentRepository;
import com.crimsonlogic.ecommerce.repository.ProductRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Customer customer;
    private Address address;
    private Product product;
    private Seller seller;
    private Inventory inventory;
    private Cart cart;
    private Order order;

    @BeforeEach
    void setUp() {

        // ======================================================
        // CUSTOMER
        // ======================================================

        customer = new Customer();
        customer.setId("CUS001");
        customer.setName("Prajwal");
        customer.setEmail("prajwal@gmail.com");
        customer.setPhone("9876543210");
        customer.setWalletBalance(BigDecimal.valueOf(10000));

        // ======================================================
        // ADDRESS
        // ======================================================

        address = new Address();
        address.setId("ADR001");
        address.setHouseNumber("12A");
        address.setStreet("MG Road");
        address.setCity("Bengaluru");
        address.setState("Karnataka");
        address.setCountry("India");
        address.setZipCode("560001");

        customer.addAddress(address);

        // ======================================================
        // SELLER
        // ======================================================

        seller = new Seller();
        seller.setId("SEL001");
        seller.setShopName("Prajwal Electronics");

        // ======================================================
        // PRODUCT
        // ======================================================

        product = new Product();
        product.setId("PRO001");
        product.setName("Laptop");
        product.setBrand("Dell");
        product.setPrice(50000.0);
        product.setStatus(ProductStatus.AVAILABLE);
        product.setSeller(seller);

        // ======================================================
        // INVENTORY
        // ======================================================

        inventory = new Inventory();
        inventory.setId("INV001");
        inventory.setProduct(product);
        inventory.setQuantity(10);

        // ======================================================
        // CART
        // ======================================================

        cart = new Cart();
        cart.setId("CRT001");
        cart.setCustomer(customer);
        cart.setProduct(product);
        cart.setQuantity(2);

        // ======================================================
        // ORDER
        // ======================================================

        order = new Order();
        order.setId("ORD001");
        order.setCustomer(customer);
        order.setProduct(product);
        order.setQuantity(2);
        order.setTotalPrice(100000.0);
        order.setStatus(OrderStatus.PENDING_APPROVAL);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(address);
    }

    // ==========================================================
    // CHECKOUT
    // ==========================================================

    @Test
    void shouldCheckoutSuccessfullyUsingExistingAddress() {

        CheckoutRequestDTO request = new CheckoutRequestDTO();

        request.setAddressId("ADR001");
        request.setPaymentMethod("CASH_ON_DELIVERY");

        when(customerRepository.findById("CUS001"))
                .thenReturn(Optional.of(customer));

        when(addressRepository.findById("ADR001"))
                .thenReturn(Optional.of(address));

        when(cartRepository.findByCustomerId("CUS001"))
                .thenReturn(Arrays.asList(cart));

        when(inventoryRepository.findByProductId("PRO001"))
                .thenReturn(Optional.of(inventory));

        when(orderRepository.saveAll(anyList()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        String result =
                orderService.checkout(
                        "CUS001",
                        request
                );

        assertNotNull(result);

        assertTrue(
                result.contains("Checkout successful!")
        );

        assertTrue(
                result.contains("1 order(s) placed")
        );

        assertEquals(
                8,
                inventory.getQuantity()
        );

        verify(customerRepository)
                .findById("CUS001");

        verify(addressRepository)
                .findById("ADR001");

        verify(cartRepository)
                .findByCustomerId("CUS001");

        verify(inventoryRepository)
                .findByProductId("PRO001");

        verify(inventoryRepository)
                .save(inventory);

        verify(orderRepository)
                .saveAll(anyList());

        verify(paymentRepository)
                .save(any(Payment.class));

        verify(cartRepository)
                .deleteAll(anyList());
    }

    @Test
    void shouldCheckoutSuccessfullyUsingNewAddress() {

        CheckoutRequestDTO request = new CheckoutRequestDTO();

        request.setPaymentMethod("CASH_ON_DELIVERY");

        Address newAddress = new Address();
        newAddress.setHouseNumber("25B");
        newAddress.setStreet("ITPL Road");
        newAddress.setCity("Bengaluru");
        newAddress.setState("Karnataka");
        newAddress.setCountry("India");
        newAddress.setZipCode("560066");

        request.setNewAddress(
                new com.crimsonlogic.ecommerce.dto.address.AddressRequestDTO()
        );

        request.getNewAddress().setHouseNumber("25B");
        request.getNewAddress().setStreet("ITPL Road");
        request.getNewAddress().setCity("Bengaluru");
        request.getNewAddress().setState("Karnataka");
        request.getNewAddress().setCountry("India");
        request.getNewAddress().setZipCode("560066");

        when(customerRepository.findById("CUS001"))
                .thenReturn(Optional.of(customer));

        when(cartRepository.findByCustomerId("CUS001"))
                .thenReturn(Arrays.asList(cart));

        when(inventoryRepository.findByProductId("PRO001"))
                .thenReturn(Optional.of(inventory));

        when(orderRepository.saveAll(anyList()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        String result =
                orderService.checkout(
                        "CUS001",
                        request
                );

        assertNotNull(result);

        assertTrue(
                result.contains("Checkout successful!")
        );

        verify(addressRepository)
                .save(any(Address.class));

        verify(customerRepository)
                .save(customer);

        verify(orderRepository)
                .saveAll(anyList());

        verify(paymentRepository)
                .save(any(Payment.class));

        verify(cartRepository)
                .deleteAll(anyList());
    }

    @Test
    void shouldRejectCheckoutWhenCustomerDoesNotExist() {

        CheckoutRequestDTO request = new CheckoutRequestDTO();

        when(customerRepository.findById("CUS999"))
                .thenReturn(Optional.empty());

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> orderService.checkout(
                                "CUS999",
                                request
                        )
                );

        assertEquals(
                "Customer not found.",
                exception.getMessage()
        );

        verify(customerRepository)
                .findById("CUS999");

        verifyNoInteractions(
                cartRepository,
                addressRepository,
                inventoryRepository,
                orderRepository,
                paymentRepository
        );
    }

    @Test
    void shouldRejectCheckoutWhenAddressDoesNotExist() {

        CheckoutRequestDTO request = new CheckoutRequestDTO();

        request.setAddressId("ADR999");
        request.setPaymentMethod("CASH_ON_DELIVERY");

        when(customerRepository.findById("CUS001"))
                .thenReturn(Optional.of(customer));

        when(addressRepository.findById("ADR999"))
                .thenReturn(Optional.empty());

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> orderService.checkout(
                                "CUS001",
                                request
                        )
                );

        assertEquals(
                "Selected address not found.",
                exception.getMessage()
        );

        verify(addressRepository)
                .findById("ADR999");

        verifyNoInteractions(
                cartRepository,
                inventoryRepository,
                orderRepository,
                paymentRepository
        );
    }

    @Test
    void shouldRejectCheckoutWhenAddressDoesNotBelongToCustomer() {

        CheckoutRequestDTO request = new CheckoutRequestDTO();

        request.setAddressId("ADR002");
        request.setPaymentMethod("CASH_ON_DELIVERY");

        Address anotherAddress = new Address();
        anotherAddress.setId("ADR002");

        when(customerRepository.findById("CUS001"))
                .thenReturn(Optional.of(customer));

        when(addressRepository.findById("ADR002"))
                .thenReturn(Optional.of(anotherAddress));

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> orderService.checkout(
                                "CUS001",
                                request
                        )
                );

        assertEquals(
                "Access Denied: You can only ship to your own saved addresses.",
                exception.getMessage()
        );

        verify(addressRepository)
                .findById("ADR002");

        verifyNoInteractions(
                cartRepository,
                inventoryRepository,
                orderRepository,
                paymentRepository
        );
    }

    @Test
    void shouldRejectCheckoutWhenNoAddressIsProvided() {

        CheckoutRequestDTO request = new CheckoutRequestDTO();

        request.setPaymentMethod("CASH_ON_DELIVERY");

        when(customerRepository.findById("CUS001"))
                .thenReturn(Optional.of(customer));

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> orderService.checkout(
                                "CUS001",
                                request
                        )
                );

        assertEquals(
                "You must provide either an existing 'addressId' or fill out a 'newAddress' to checkout.",
                exception.getMessage()
        );

        verify(customerRepository)
                .findById("CUS001");

        verifyNoInteractions(
                cartRepository,
                inventoryRepository,
                orderRepository,
                paymentRepository
        );
    }

    @Test
    void shouldRejectCheckoutWhenNewAddressHasNoHouseNumber() {

        CheckoutRequestDTO request = new CheckoutRequestDTO();

        request.setPaymentMethod("CASH_ON_DELIVERY");

        request.setNewAddress(
                new com.crimsonlogic.ecommerce.dto.address.AddressRequestDTO()
        );

        request.getNewAddress().setStreet("MG Road");
        request.getNewAddress().setCity("Bengaluru");
        request.getNewAddress().setState("Karnataka");
        request.getNewAddress().setCountry("India");
        request.getNewAddress().setZipCode("560001");

        when(customerRepository.findById("CUS001"))
                .thenReturn(Optional.of(customer));

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> orderService.checkout(
                                "CUS001",
                                request
                        )
                );

        assertEquals(
                "House number is compulsory for customers.",
                exception.getMessage()
        );

        verifyNoInteractions(
                cartRepository,
                inventoryRepository,
                orderRepository,
                paymentRepository
        );
    }

    @Test
    void shouldRejectCheckoutWhenCartIsEmpty() {

        CheckoutRequestDTO request = new CheckoutRequestDTO();

        request.setAddressId("ADR001");
        request.setPaymentMethod("CASH_ON_DELIVERY");

        when(customerRepository.findById("CUS001"))
                .thenReturn(Optional.of(customer));

        when(addressRepository.findById("ADR001"))
                .thenReturn(Optional.of(address));

        when(cartRepository.findByCustomerId("CUS001"))
                .thenReturn(Collections.emptyList());

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> orderService.checkout(
                                "CUS001",
                                request
                        )
                );

        assertEquals(
                "Your cart is empty. Please add items before checking out.",
                exception.getMessage()
        );

        verify(cartRepository)
                .findByCustomerId("CUS001");

        verifyNoInteractions(
                inventoryRepository,
                orderRepository,
                paymentRepository
        );
    }

    @Test
    void shouldRejectCheckoutWhenInventoryIsMissing() {

        CheckoutRequestDTO request = new CheckoutRequestDTO();

        request.setAddressId("ADR001");
        request.setPaymentMethod("CASH_ON_DELIVERY");

        when(customerRepository.findById("CUS001"))
                .thenReturn(Optional.of(customer));

        when(addressRepository.findById("ADR001"))
                .thenReturn(Optional.of(address));

        when(cartRepository.findByCustomerId("CUS001"))
                .thenReturn(Arrays.asList(cart));

        when(inventoryRepository.findByProductId("PRO001"))
                .thenReturn(Optional.empty());

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> orderService.checkout(
                                "CUS001",
                                request
                        )
                );

        assertEquals(
                "Inventory missing for product: Laptop",
                exception.getMessage()
        );

        verify(inventoryRepository)
                .findByProductId("PRO001");

        verifyNoInteractions(
                orderRepository,
                paymentRepository
        );
    }

    @Test
    void shouldRejectCheckoutWhenStockIsInsufficient() {

        CheckoutRequestDTO request = new CheckoutRequestDTO();

        request.setAddressId("ADR001");
        request.setPaymentMethod("CASH_ON_DELIVERY");

        inventory.setQuantity(1);

        when(customerRepository.findById("CUS001"))
                .thenReturn(Optional.of(customer));

        when(addressRepository.findById("ADR001"))
                .thenReturn(Optional.of(address));

        when(cartRepository.findByCustomerId("CUS001"))
                .thenReturn(Arrays.asList(cart));

        when(inventoryRepository.findByProductId("PRO001"))
                .thenReturn(Optional.of(inventory));

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> orderService.checkout(
                                "CUS001",
                                request
                        )
                );

        assertEquals(
                "Insufficient stock for Laptop. Only 1 left.",
                exception.getMessage()
        );

        verify(orderRepository, never())
                .saveAll(anyList());

        verify(paymentRepository, never())
                .save(any(Payment.class));
    }

    // ==========================================================
    // WALLET
    // ==========================================================

    @Test
    void shouldCheckoutSuccessfullyUsingWallet() {

        CheckoutRequestDTO request = new CheckoutRequestDTO();

        request.setAddressId("ADR001");
        request.setPaymentMethod("WALLET");

        customer.setWalletBalance(
                BigDecimal.valueOf(200000)
        );

        when(customerRepository.findById("CUS001"))
                .thenReturn(Optional.of(customer));

        when(addressRepository.findById("ADR001"))
                .thenReturn(Optional.of(address));

        when(cartRepository.findByCustomerId("CUS001"))
                .thenReturn(Arrays.asList(cart));

        when(inventoryRepository.findByProductId("PRO001"))
                .thenReturn(Optional.of(inventory));

        when(orderRepository.saveAll(anyList()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        String result =
                orderService.checkout(
                        "CUS001",
                        request
                );

        assertTrue(
                result.contains("Checkout successful!")
        );

        assertEquals(
                BigDecimal.valueOf(100000.0),
                customer.getWalletBalance()
        );

        verify(customerRepository)
                .save(customer);

        verify(paymentRepository)
                .save(any(Payment.class));

        verify(cartRepository)
                .deleteAll(anyList());
    }

    @Test
    void shouldRejectCheckoutWhenWalletBalanceIsInsufficient() {

        CheckoutRequestDTO request = new CheckoutRequestDTO();

        request.setAddressId("ADR001");
        request.setPaymentMethod("WALLET");

        customer.setWalletBalance(
                BigDecimal.valueOf(1000)
        );

        when(customerRepository.findById("CUS001"))
                .thenReturn(Optional.of(customer));

        when(addressRepository.findById("ADR001"))
                .thenReturn(Optional.of(address));

        when(cartRepository.findByCustomerId("CUS001"))
                .thenReturn(Arrays.asList(cart));

        when(inventoryRepository.findByProductId("PRO001"))
                .thenReturn(Optional.of(inventory));

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> orderService.checkout(
                                "CUS001",
                                request
                        )
                );

        assertTrue(
                exception.getMessage()
                        .contains("Insufficient wallet balance")
        );

        verify(customerRepository, never())
                .save(any(Customer.class));

        verify(paymentRepository, never())
                .save(any(Payment.class));
    }

    // ==========================================================
    // GET MY ORDERS
    // ==========================================================

    @Test
    void shouldGetCustomerOrdersSuccessfully() {

        when(orderRepository
                .findByCustomerIdOrderByOrderDateDesc("CUS001"))
                .thenReturn(Arrays.asList(order));

        List<OrderResponseDTO> result =
                orderService.getMyOrders(
                        "CUS001",
                        "CUSTOMER"
                );

        assertNotNull(result);

        assertEquals(
                1,
                result.size()
        );

        assertEquals(
                "ORD001",
                result.get(0).getOrderId()
        );

        assertEquals(
                "Laptop",
                result.get(0).getProductName()
        );

        assertEquals(
                2,
                result.get(0).getQuantity()
        );

        verify(orderRepository)
                .findByCustomerIdOrderByOrderDateDesc("CUS001");
    }

    @Test
    void shouldGetSellerOrdersSuccessfully() {

        when(orderRepository
                .findByProductSellerIdOrderByOrderDateDesc("SEL001"))
                .thenReturn(Arrays.asList(order));

        List<OrderResponseDTO> result =
                orderService.getMyOrders(
                        "SEL001",
                        "SELLER"
                );

        assertNotNull(result);

        assertEquals(
                1,
                result.size()
        );

        assertEquals(
                "ORD001",
                result.get(0).getOrderId()
        );

        verify(orderRepository)
                .findByProductSellerIdOrderByOrderDateDesc("SEL001");
    }

    @Test
    void shouldReturnEmptyOrdersWhenCustomerHasNoOrders() {

        when(orderRepository
                .findByCustomerIdOrderByOrderDateDesc("CUS001"))
                .thenReturn(Collections.emptyList());

        List<OrderResponseDTO> result =
                orderService.getMyOrders(
                        "CUS001",
                        "CUSTOMER"
                );

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(orderRepository)
                .findByCustomerIdOrderByOrderDateDesc("CUS001");
    }

    // ==========================================================
    // UPDATE ORDER STATUS
    // ==========================================================

    @Test
    void shouldConfirmOrderSuccessfullyBySeller() {

        order.setStatus(OrderStatus.PENDING_APPROVAL);

        when(orderRepository.findById("ORD001"))
                .thenReturn(Optional.of(order));

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        String result =
                orderService.updateOrderStatus(
                        "ORD001",
                        "CONFIRMED",
                        "SEL001",
                        "SELLER"
                );

        assertTrue(
                result.contains("CONFIRMED")
        );

        assertEquals(
                OrderStatus.CONFIRMED,
                order.getStatus()
        );

        verify(orderRepository)
                .save(order);
    }

    @Test
    void shouldRejectOrderSuccessfullyByAdmin() {

        order.setStatus(OrderStatus.PENDING_APPROVAL);

        when(orderRepository.findById("ORD001"))
                .thenReturn(Optional.of(order));

        // REQUIRED because REJECTED triggers restoreInventory()
        when(inventoryRepository.findByProductId("PRO001"))
                .thenReturn(Optional.of(inventory));

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        when(inventoryRepository.save(any(Inventory.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        String result =
                orderService.updateOrderStatus(
                        "ORD001",
                        "REJECTED",
                        "ADM001",
                        "ADMIN"
                );

        assertEquals(
                OrderStatus.REJECTED,
                order.getStatus()
        );

        assertTrue(
                result.contains("REJECTED")
        );

        // Inventory was restored
        assertEquals(
                12,
                inventory.getQuantity()
        );

        verify(orderRepository)
                .save(order);

        verify(inventoryRepository)
                .findByProductId("PRO001");

        verify(inventoryRepository)
                .save(inventory);
    }

    @Test
    void shouldRejectSellerUpdatingAnotherSellersOrder() {

        Seller anotherSeller = new Seller();
        anotherSeller.setId("SEL002");

        product.setSeller(anotherSeller);

        order.setStatus(OrderStatus.PENDING_APPROVAL);

        when(orderRepository.findById("ORD001"))
                .thenReturn(Optional.of(order));

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> orderService.updateOrderStatus(
                                "ORD001",
                                "CONFIRMED",
                                "SEL001",
                                "SELLER"
                        )
                );

        assertEquals(
                "Access Denied: You can only update orders for your own products.",
                exception.getMessage()
        );

        verify(orderRepository, never())
                .save(any(Order.class));
    }

    @Test
    void shouldRejectCustomerUpdatingOrderStatus() {

        when(orderRepository.findById("ORD001"))
                .thenReturn(Optional.of(order));

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> orderService.updateOrderStatus(
                                "ORD001",
                                "CONFIRMED",
                                "CUS001",
                                "CUSTOMER"
                        )
                );

        assertEquals(
                "Access Denied: Only Sellers and Admins can update order statuses.",
                exception.getMessage()
        );

        verify(orderRepository, never())
                .save(any(Order.class));
    }

    @Test
    void shouldRejectInvalidOrderStatus() {

        when(orderRepository.findById("ORD001"))
                .thenReturn(Optional.of(order));

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> orderService.updateOrderStatus(
                                "ORD001",
                                "INVALID_STATUS",
                                "ADM001",
                                "ADMIN"
                        )
                );

        assertEquals(
                "Invalid status provided.",
                exception.getMessage()
        );

        verify(orderRepository, never())
                .save(any(Order.class));
    }

    @Test
    void shouldRejectInvalidStatusTransition() {

        order.setStatus(OrderStatus.PENDING_APPROVAL);

        when(orderRepository.findById("ORD001"))
                .thenReturn(Optional.of(order));

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> orderService.updateOrderStatus(
                                "ORD001",
                                "DELIVERED",
                                "ADM001",
                                "ADMIN"
                        )
                );

        assertTrue(
                exception.getMessage()
                        .contains("Invalid status transition")
        );

        verify(orderRepository, never())
                .save(any(Order.class));
    }

    // ==========================================================
    // CANCEL ORDER
    // ==========================================================

    @Test
    void shouldCancelOrderSuccessfully() {

        order.setStatus(OrderStatus.CONFIRMED);

        when(orderRepository.findById("ORD001"))
                .thenReturn(Optional.of(order));

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        when(inventoryRepository.findByProductId("PRO001"))
                .thenReturn(Optional.of(inventory));

        String result =
                orderService.cancelOrder(
                        "ORD001",
                        "CUS001"
                );

        assertEquals(
                OrderStatus.CANCELLED,
                order.getStatus()
        );

        assertTrue(
                result.contains("successfully cancelled")
        );

        assertEquals(
                12,
                inventory.getQuantity()
        );

        verify(orderRepository)
                .save(order);

        verify(inventoryRepository)
                .findByProductId("PRO001");

        verify(inventoryRepository)
                .save(inventory);
    }

    @Test
    void shouldRejectCancelWhenOrderBelongsToAnotherCustomer() {

        Customer anotherCustomer = new Customer();
        anotherCustomer.setId("CUS002");

        order.setCustomer(anotherCustomer);

        when(orderRepository.findById("ORD001"))
                .thenReturn(Optional.of(order));

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> orderService.cancelOrder(
                                "ORD001",
                                "CUS001"
                        )
                );

        assertEquals(
                "Access Denied: You can only cancel your own orders.",
                exception.getMessage()
        );

        verify(orderRepository, never())
                .save(any(Order.class));
    }

    @Test
    void shouldRejectCancelAfterOrderIsShipped() {

        order.setStatus(OrderStatus.SHIPPED);

        when(orderRepository.findById("ORD001"))
                .thenReturn(Optional.of(order));

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> orderService.cancelOrder(
                                "ORD001",
                                "CUS001"
                        )
                );

        assertTrue(
                exception.getMessage()
                        .contains("cannot be cancelled")
        );

        verify(orderRepository, never())
                .save(any(Order.class));
    }

    @Test
    void shouldRejectAlreadyCancelledOrder() {

        order.setStatus(OrderStatus.CANCELLED);

        when(orderRepository.findById("ORD001"))
                .thenReturn(Optional.of(order));

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> orderService.cancelOrder(
                                "ORD001",
                                "CUS001"
                        )
                );

        assertEquals(
                "This order is already cancelled.",
                exception.getMessage()
        );

        verify(orderRepository, never())
                .save(any(Order.class));
    }

    // ==========================================================
    // REQUEST RETURN
    // ==========================================================

    @Test
    void shouldRequestReturnSuccessfullyWithinThreeDays() {

        order.setStatus(OrderStatus.DELIVERED);

        order.setDeliveredDate(
                LocalDateTime.now().minusDays(1)
        );

        when(orderRepository.findById("ORD001"))
                .thenReturn(Optional.of(order));

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        String result =
                orderService.requestReturn(
                        "ORD001",
                        "CUS001"
                );

        assertEquals(
                OrderStatus.RETURN_REQUESTED,
                order.getStatus()
        );

        assertTrue(
                result.contains("return request")
        );

        verify(orderRepository)
                .save(order);
    }

    @Test
    void shouldRejectReturnWhenOrderIsNotDelivered() {

        order.setStatus(OrderStatus.CONFIRMED);

        when(orderRepository.findById("ORD001"))
                .thenReturn(Optional.of(order));

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> orderService.requestReturn(
                                "ORD001",
                                "CUS001"
                        )
                );

        assertEquals(
                "You can only return items that have already been delivered.",
                exception.getMessage()
        );

        verify(orderRepository, never())
                .save(any(Order.class));
    }

    @Test
    void shouldRejectReturnWhenReturnWindowExpired() {

        order.setStatus(OrderStatus.DELIVERED);

        order.setDeliveredDate(
                LocalDateTime.now().minusDays(5)
        );

        when(orderRepository.findById("ORD001"))
                .thenReturn(Optional.of(order));

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> orderService.requestReturn(
                                "ORD001",
                                "CUS001"
                        )
                );

        assertTrue(
                exception.getMessage()
                        .contains("Return window expired")
        );

        verify(orderRepository, never())
                .save(any(Order.class));
    }

    @Test
    void shouldRejectReturnForAnotherCustomer() {

        Customer anotherCustomer = new Customer();
        anotherCustomer.setId("CUS002");

        order.setCustomer(anotherCustomer);

        when(orderRepository.findById("ORD001"))
                .thenReturn(Optional.of(order));

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> orderService.requestReturn(
                                "ORD001",
                                "CUS001"
                        )
                );

        assertEquals(
                "Access Denied: You can only request returns for your own orders.",
                exception.getMessage()
        );

        verify(orderRepository, never())
                .save(any(Order.class));
    }

    // ==========================================================
    // ORDER NOT FOUND
    // ==========================================================

    @Test
    void shouldThrowExceptionWhenOrderNotFoundWhileUpdatingStatus() {

        when(orderRepository.findById("ORD999"))
                .thenReturn(Optional.empty());

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> orderService.updateOrderStatus(
                                "ORD999",
                                "CONFIRMED",
                                "ADM001",
                                "ADMIN"
                        )
                );

        assertEquals(
                "Order not found.",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenOrderNotFoundWhileCancelling() {

        when(orderRepository.findById("ORD999"))
                .thenReturn(Optional.empty());

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> orderService.cancelOrder(
                                "ORD999",
                                "CUS001"
                        )
                );

        assertEquals(
                "Order not found.",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenOrderNotFoundWhileReturning() {

        when(orderRepository.findById("ORD999"))
                .thenReturn(Optional.empty());

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> orderService.requestReturn(
                                "ORD999",
                                "CUS001"
                        )
                );

        assertEquals(
                "Order not found.",
                exception.getMessage()
        );
    }
}