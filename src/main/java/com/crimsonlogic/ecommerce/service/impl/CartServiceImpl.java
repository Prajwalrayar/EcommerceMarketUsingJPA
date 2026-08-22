package com.crimsonlogic.ecommerce.service.impl;

import com.crimsonlogic.ecommerce.dto.cart.CartRequestDTO;
import com.crimsonlogic.ecommerce.entity.Cart;
import com.crimsonlogic.ecommerce.entity.Customer;
import com.crimsonlogic.ecommerce.entity.Inventory;
import com.crimsonlogic.ecommerce.entity.Product;
import com.crimsonlogic.ecommerce.exception.ValidationException;
import com.crimsonlogic.ecommerce.repository.CartRepository;
import com.crimsonlogic.ecommerce.repository.CustomerRepository;
import com.crimsonlogic.ecommerce.repository.InventoryRepository;
import com.crimsonlogic.ecommerce.repository.ProductRepository;
import com.crimsonlogic.ecommerce.util.IdGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CartServiceImpl {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final InventoryRepository inventoryRepository;

    public CartServiceImpl(CartRepository cartRepository, ProductRepository productRepository,
                           CustomerRepository customerRepository, InventoryRepository inventoryRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public String addToCart(String customerId, CartRequestDTO request) {
        Product product = productRepository.findByName(request.getProductName())
                .orElseThrow(() -> new ValidationException("Product '" +
                        request.getProductName() + "' not found."));

        Inventory inventory = inventoryRepository.findByProductId(product.getId())
                .orElseThrow(() -> new ValidationException("Inventory missing for product."));

        if (inventory.getQuantity() < request.getQuantity()) {
            throw new ValidationException("Insufficient stock. Only " + inventory.getQuantity() + " available.");
        }

        Cart cart = cartRepository.findByCustomerIdAndProductId(customerId, product.getId())
                .orElse(new Cart());

        if (cart.getId() == null) {
            Customer customer = customerRepository.findById(customerId).get();
            cart.setId(IdGenerator.generateId("CRT"));
            cart.setCustomer(customer);
            cart.setProduct(product);
            cart.setQuantity(request.getQuantity());
        } else {
            int newQuantity = cart.getQuantity() + request.getQuantity();
            if (newQuantity > inventory.getQuantity()) {
                throw new ValidationException("Requested quantity exceeds available stock.");
            }
            cart.setQuantity(newQuantity);
        }

        cartRepository.save(cart);
        return "Product added to cart successfully.";
    }
}