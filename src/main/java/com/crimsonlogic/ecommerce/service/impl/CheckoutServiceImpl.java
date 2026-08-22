package com.crimsonlogic.ecommerce.service.impl;

import com.crimsonlogic.ecommerce.dto.order.CheckoutRequestDTO;
import com.crimsonlogic.ecommerce.entity.*;
import com.crimsonlogic.ecommerce.enumeration.OrderStatus;
import com.crimsonlogic.ecommerce.enumeration.PaymentMethod;
import com.crimsonlogic.ecommerce.enumeration.PaymentStatus;
import com.crimsonlogic.ecommerce.exception.ValidationException;
import com.crimsonlogic.ecommerce.repository.*;
import com.crimsonlogic.ecommerce.util.IdGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CheckoutServiceImpl {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final CustomerRepository customerRepository;
    private final InventoryRepository inventoryRepository;

    public CheckoutServiceImpl(CartRepository cartRepository, OrderRepository orderRepository,
                               PaymentRepository paymentRepository, CustomerRepository customerRepository,
                               InventoryRepository inventoryRepository) {
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        this.customerRepository = customerRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    public String checkout(String customerId, CheckoutRequestDTO request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ValidationException("Customer not found."));

        List<Cart> cartItems = cartRepository.findByCustomerId(customerId);
        if (cartItems.isEmpty()) {
            throw new ValidationException("Cart is empty.");
        }

        double totalAmount = cartItems.stream().mapToDouble(Cart::getTotalPrice).sum();

        // 1. Payment Validation
        PaymentMethod method = PaymentMethod.valueOf(request.getPaymentMethod().toUpperCase());
        if (method == PaymentMethod.UPI && (request.getUpiId() == null || request.getUpiId().trim().isEmpty())) {
            throw new ValidationException("UPI ID is required for UPI payments.");
        }

        // 2. Wallet Check
        if (method == PaymentMethod.WALLET) {
            BigDecimal totalDecimal = BigDecimal.valueOf(totalAmount);
            if (customer.getWalletBalance().compareTo(totalDecimal) < 0) {
                throw new ValidationException("Insufficient wallet balance. Require: ₹" + totalAmount);
            }
            customer.setWalletBalance(customer.getWalletBalance().subtract(totalDecimal));
            customerRepository.save(customer);
        }

        // 3. Process Each Cart Item
        for (Cart item : cartItems) {
            Product product = item.getProduct();
            Inventory inventory = inventoryRepository.findByProductId(product.getId())
                    .orElseThrow(() -> new ValidationException("Inventory error."));

            if (inventory.getQuantity() < item.getQuantity()) {
                throw new ValidationException("Product " + product.getName() + " is out of stock.");
            }

            // Deduct Inventory
            inventory.setQuantity(inventory.getQuantity() - item.getQuantity());
            inventoryRepository.save(inventory);

            // Create Order
            Order order = new Order();
            order.setId(IdGenerator.generateId("ORD"));
            order.setCustomer(customer);
            order.setProduct(product);
            order.setQuantity(item.getQuantity());
            order.setTotalPrice(item.getTotalPrice());
            order.setStatus(OrderStatus.PENDING_APPROVAL);
            order.setOrderDate(LocalDateTime.now());
            Order savedOrder = orderRepository.save(order);

            // Create Payment
            Payment payment = new Payment();
            payment.setId(IdGenerator.generateId("PAY"));
            payment.setTransactionId(IdGenerator.generateId("TXN"));
            payment.setCustomer(customer);
            payment.setOrder(savedOrder);
            payment.setPaymentMethod(method);
            payment.setAmount(item.getTotalPrice());
            payment.setUpiId(request.getUpiId());
            payment.setPaymentDate(LocalDateTime.now());
            payment.setPaymentStatus(method == PaymentMethod.CASH_ON_DELIVERY ? PaymentStatus.PENDING : PaymentStatus.SUCCESS);
            paymentRepository.save(payment);
        }

        // 4. Clear Cart
        cartRepository.deleteByCustomerId(customerId);

        return "Checkout successful! Orders have been placed.";
    }
}