package com.crimsonlogic.ecommerce.service.impl;

import com.crimsonlogic.ecommerce.dto.order.CheckoutRequestDTO;
import com.crimsonlogic.ecommerce.dto.order.OrderResponseDTO;
import com.crimsonlogic.ecommerce.entity.*;
import com.crimsonlogic.ecommerce.enumeration.OrderStatus;
import com.crimsonlogic.ecommerce.enumeration.PaymentMethod;
import com.crimsonlogic.ecommerce.enumeration.PaymentStatus;
import com.crimsonlogic.ecommerce.enumeration.ProductStatus;
import com.crimsonlogic.ecommerce.exception.ValidationException;
import com.crimsonlogic.ecommerce.repository.*;
import com.crimsonlogic.ecommerce.service.OrderService;
import com.crimsonlogic.ecommerce.util.IdGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;
    private final PaymentRepository paymentRepository;

    public OrderServiceImpl(OrderRepository orderRepository, CartRepository cartRepository,
                            InventoryRepository inventoryRepository, ProductRepository productRepository,
                            AddressRepository addressRepository, CustomerRepository customerRepository,
                            PaymentRepository paymentRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
        this.addressRepository = addressRepository;
        this.customerRepository = customerRepository;
        this.paymentRepository = paymentRepository;
    }

    public String checkout(String customerId, CheckoutRequestDTO request) {

        // Fetch the customer right away
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ValidationException("Customer not found."));

        Address shippingAddress = null;

        // LOGIC BRANCH 1: User provided an existing Address ID
        if (request.getAddressId() != null && !request.getAddressId().trim().isEmpty()) {
            shippingAddress = addressRepository.findById(request.getAddressId())
                    .orElseThrow(() -> new ValidationException("Selected address not found."));

            // Verify the customer owns this address by checking their saved list
            boolean ownsAddress = customer.getAddresses().stream()
                    .anyMatch(addr -> addr.getId().equals(request.getAddressId()));

            if (!ownsAddress) {
                throw new ValidationException("Access Denied: You can only ship to your own saved addresses.");
            }
        }
        // LOGIC BRANCH 2: User provided a brand new address object
        else if (request.getNewAddress() != null) {

            if (request.getNewAddress().getHouseNumber() == null || request.getNewAddress().getHouseNumber().trim().isEmpty()) {
                throw new ValidationException("House number is compulsory for customers.");
            }

            shippingAddress = new Address();
            shippingAddress.setId(IdGenerator.generateId("ADR"));
            shippingAddress.setHouseNumber(request.getNewAddress().getHouseNumber());
            shippingAddress.setStreet(request.getNewAddress().getStreet());
            shippingAddress.setCity(request.getNewAddress().getCity());
            shippingAddress.setState(request.getNewAddress().getState());
            shippingAddress.setCountry(request.getNewAddress().getCountry());
            shippingAddress.setZipCode(request.getNewAddress().getZipCode());

            // Save new address and link to customer
            addressRepository.save(shippingAddress);
            customer.addAddress(shippingAddress);
            customerRepository.save(customer);

        }
        // LOGIC BRANCH 3: They sent neither
        else {
            throw new ValidationException("You must provide either an existing 'addressId' or fill out a 'newAddress' to checkout.");
        }

        List<Cart> cartItems = cartRepository.findByCustomerId(customerId);
        if (cartItems.isEmpty()) {
            throw new ValidationException("Your cart is empty. Please add items before checking out.");
        }

        List<Order> newOrders = new ArrayList<>();

        for (Cart cartItem : cartItems) {
            Inventory inventory = inventoryRepository.findByProductId(cartItem.getProduct().getId())
                    .orElseThrow(() -> new ValidationException("Inventory missing for product: " + cartItem.getProduct().getName()));

            if (inventory.getQuantity() < cartItem.getQuantity()) {
                throw new ValidationException("Insufficient stock for " + cartItem.getProduct().getName() +
                        ". Only " + inventory.getQuantity() + " left.");
            }

            Order order = new Order();
            order.setId(IdGenerator.generateId("ORD"));
            order.setCustomer(cartItem.getCustomer());
            order.setProduct(cartItem.getProduct());
            order.setQuantity(cartItem.getQuantity());
            order.setTotalPrice(cartItem.getTotalPrice());
            order.setStatus(OrderStatus.PENDING_APPROVAL);
            order.setOrderDate(LocalDateTime.now());

            // Attach the shipping address
            order.setShippingAddress(shippingAddress);

            newOrders.add(order);

            inventory.setQuantity(inventory.getQuantity() - cartItem.getQuantity());

            if (inventory.getQuantity() == 0) {
                cartItem.getProduct().setStatus(ProductStatus.OUT_OF_STOCK);
                productRepository.save(cartItem.getProduct());
            }
            inventoryRepository.save(inventory);
        }

        orderRepository.saveAll(newOrders);

        // ==========================================================
        // ---> PAYMENT PROCESSING INTEGRATION <---
        // ==========================================================
        // --- PAYMENT PROCESSING LOGIC ---
        double totalCartPrice = newOrders.stream().mapToDouble(Order::getTotalPrice).sum();

        // Convert string to uppercase for safe comparison
        String paymentMethodStr = request.getPaymentMethod() != null ? request.getPaymentMethod().toUpperCase() : "";

        if ("WALLET".equals(paymentMethodStr)) {
            double currentWallet = customer.getWalletBalance() != null ? customer.getWalletBalance().doubleValue() : 0.0;
            if (currentWallet < totalCartPrice) {
                throw new ValidationException("Insufficient wallet balance. Available: ₹" + currentWallet + ", Required: ₹" + totalCartPrice);
            }
            customer.setWalletBalance(customer.getWalletBalance().subtract(java.math.BigDecimal.valueOf(totalCartPrice)));
            customerRepository.save(customer);
        }

        for (Order order : newOrders) {
            Payment payment = new Payment();
            payment.setId(IdGenerator.generateId("PAY"));
            payment.setOrder(order);
            payment.setCustomer(customer);
            payment.setAmount(order.getTotalPrice());

            // Set the Enum value on the Payment entity
            payment.setPaymentMethod(PaymentMethod.valueOf(paymentMethodStr));

            if ("CASH_ON_DELIVERY".equals(paymentMethodStr)) {
                payment.setPaymentStatus(PaymentStatus.PENDING);
            } else {
                payment.setPaymentStatus(PaymentStatus.SUCCESS);
            }

            String shortTxnId = "TXN-" + (System.currentTimeMillis() % 100000000);
            payment.setTransactionId(shortTxnId);

            if (request.getUpiId() != null && !request.getUpiId().trim().isEmpty()) {
                payment.setUpiId(request.getUpiId().trim());
            }

            payment.setPaymentDate(LocalDateTime.now());
            paymentRepository.save(payment);
        }

        // Empty the customer's cart
        cartRepository.deleteAll(cartItems);

        return "Checkout successful! " + newOrders.size() + " order(s) placed using " + request.getPaymentMethod() + ".";
    }

    public List<OrderResponseDTO> getMyOrders(String userId, String role) {
        List<Order> orders;

        if ("SELLER".equals(role)) {
            orders = orderRepository.findByProductSellerIdOrderByOrderDateDesc(userId);
        } else {
            orders = orderRepository.findByCustomerIdOrderByOrderDateDesc(userId);
        }

        return orders.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public String updateOrderStatus(String orderId, String newStatusString, String userId, String role) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ValidationException("Order not found."));

        if ("SELLER".equals(role)) {
            if (order.getProduct().getSeller() == null || !order.getProduct().getSeller().getId().equals(userId)) {
                throw new ValidationException("Access Denied: You can only update orders for your own products.");
            }
        } else if (!"ADMIN".equals(role)) {
            throw new ValidationException("Access Denied: Only Sellers and Admins can update order statuses.");
        }

        OrderStatus targetStatus;
        try {
            targetStatus = OrderStatus.valueOf(newStatusString.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Invalid status provided.");
        }

        OrderStatus currentStatus = order.getStatus();
        boolean isValidTransition = false;

        switch (currentStatus) {
            case PENDING_APPROVAL:
                isValidTransition = (targetStatus == OrderStatus.CONFIRMED || targetStatus == OrderStatus.REJECTED);
                break;
            case CONFIRMED:
                isValidTransition = (targetStatus == OrderStatus.SHIPPED || targetStatus == OrderStatus.CANCELLED);
                break;
            case SHIPPED:
                isValidTransition = (targetStatus == OrderStatus.IN_TRANSIT);
                break;
            case IN_TRANSIT:
                isValidTransition = (targetStatus == OrderStatus.OUT_FOR_DELIVERY);
                break;
            case OUT_FOR_DELIVERY:
                isValidTransition = (targetStatus == OrderStatus.DELIVERED);
                if (isValidTransition) {
                    order.setDeliveredDate(LocalDateTime.now());
                }
                break;
            case DELIVERED:
                isValidTransition = false;
                break;
            case RETURN_REQUESTED:
                isValidTransition = (targetStatus == OrderStatus.RETURN_APPROVED || targetStatus == OrderStatus.RETURN_REJECTED);
                break;
            case RETURN_APPROVED:
                isValidTransition = (targetStatus == OrderStatus.REFUNDED);
                break;
            default:
                isValidTransition = false;
        }

        if (!isValidTransition) {
            throw new ValidationException("Invalid status transition. You cannot change an order from "
                    + currentStatus.name() + " directly to " + targetStatus.name() + ".");
        }

        order.setStatus(targetStatus);
        orderRepository.save(order);

        if (targetStatus == OrderStatus.REJECTED || targetStatus == OrderStatus.REFUNDED) {
            restoreInventory(order);
        }

        return "Order " + orderId + " successfully updated to " + order.getStatus().name();
    }

    public String cancelOrder(String orderId, String customerId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ValidationException("Order not found."));

        if (!order.getCustomer().getId().equals(customerId)) {
            throw new ValidationException("Access Denied: You can only cancel your own orders.");
        }

        OrderStatus currentStatus = order.getStatus();
        if (currentStatus == OrderStatus.SHIPPED ||
                currentStatus == OrderStatus.IN_TRANSIT ||
                currentStatus == OrderStatus.OUT_FOR_DELIVERY ||
                currentStatus == OrderStatus.DELIVERED) {
            throw new ValidationException("Too late! Your order cannot be cancelled because it has already been shipped.");
        }

        if (currentStatus == OrderStatus.CANCELLED || currentStatus == OrderStatus.REJECTED) {
            throw new ValidationException("This order is already cancelled.");
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        restoreInventory(order);

        return "Order " + orderId + " has been successfully cancelled. Your refund will be processed.";
    }

    public String requestReturn(String orderId, String customerId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ValidationException("Order not found."));

        if (!order.getCustomer().getId().equals(customerId)) {
            throw new ValidationException("Access Denied: You can only request returns for your own orders.");
        }

        if (order.getStatus() != OrderStatus.DELIVERED) {
            throw new ValidationException("You can only return items that have already been delivered.");
        }

        if (order.getDeliveredDate() != null && order.getDeliveredDate().plusDays(3).isBefore(LocalDateTime.now())) {
            throw new ValidationException("Return window expired! You cannot return this order because it has been more than 3 days since delivery.");
        }

        order.setStatus(OrderStatus.RETURN_REQUESTED);
        orderRepository.save(order);

        return "Your return request for order " + orderId + " has been successfully submitted to the seller.";
    }

    private void restoreInventory(Order order) {
        Inventory inventory = inventoryRepository.findByProductId(order.getProduct().getId())
                .orElseThrow(() -> new ValidationException("Inventory missing for product."));

        inventory.setQuantity(inventory.getQuantity() + order.getQuantity());

        if (order.getProduct().getStatus() == ProductStatus.OUT_OF_STOCK && inventory.getQuantity() > 0) {
            order.getProduct().setStatus(ProductStatus.AVAILABLE);
            productRepository.save(order.getProduct());
        }

        inventoryRepository.save(inventory);
    }

    private OrderResponseDTO mapToDTO(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setOrderId(order.getId());
        dto.setProductName(order.getProduct() != null ? order.getProduct().getName() : "N/A");
        dto.setQuantity(order.getQuantity());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setStatus(order.getStatus() != null ? order.getStatus().name() : "N/A");

        if (order.getOrderDate() != null) {
            dto.setOrderDate(order.getOrderDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        } else {
            dto.setOrderDate("N/A");
        }
        return dto;
    }
}