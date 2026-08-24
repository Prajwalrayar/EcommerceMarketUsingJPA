package com.crimsonlogic.ecommerce.service.impl;

import com.crimsonlogic.ecommerce.dto.cart.CartRequestDTO;
import com.crimsonlogic.ecommerce.dto.cart.CartResponseDTO;
import com.crimsonlogic.ecommerce.entity.Cart;
import com.crimsonlogic.ecommerce.entity.Customer;
import com.crimsonlogic.ecommerce.entity.Inventory;
import com.crimsonlogic.ecommerce.entity.Product;
import com.crimsonlogic.ecommerce.exception.ValidationException;
import com.crimsonlogic.ecommerce.repository.CartRepository;
import com.crimsonlogic.ecommerce.repository.CustomerRepository;
import com.crimsonlogic.ecommerce.repository.InventoryRepository;
import com.crimsonlogic.ecommerce.repository.ProductRepository;
import com.crimsonlogic.ecommerce.service.CartService;
import com.crimsonlogic.ecommerce.util.IdGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CartServiceImpl implements CartService {

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

    // Your updated method (Parameter order adjusted slightly to match the Controller)
    public String addToCart(CartRequestDTO request, String customerId) {
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

    // Restored View Cart method
    public List<CartResponseDTO> viewCart(String customerId) {
        List<Cart> cartItems = cartRepository.findByCustomerId(customerId);
        return cartItems.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // Restored Remove from Cart method
    public String removeFromCart(String productName, String customerId) {

        Product product = productRepository
                .findByName(productName.trim())
                .orElseThrow(() ->
                        new ValidationException(
                                "Product '" + productName + "' not found."
                        )
                );

        Cart cart = cartRepository
                .findByCustomerIdAndProductId(
                        customerId,
                        product.getId()
                )
                .orElseThrow(() ->
                        new ValidationException(
                                "Cart item not found."
                        )
                );

        // Remove only ONE quantity
        if (cart.getQuantity() > 1) {

            cart.setQuantity(cart.getQuantity() - 1);

            cartRepository.save(cart);

            return "One quantity of '" +
                    productName +
                    "' removed from cart.";
        }

        // If quantity is already 1, remove the cart item
        cartRepository.delete(cart);

        return "'" + productName +
                "' removed from cart.";
    }

    // Restored DTO Mapper
    private CartResponseDTO mapToDTO(Cart cart) {
        CartResponseDTO dto = new CartResponseDTO();
        dto.setCartId(cart.getId());
        dto.setProductId(cart.getProduct().getId());
        dto.setProductName(cart.getProduct().getName());
        dto.setQuantity(cart.getQuantity());
        dto.setUnitPrice(cart.getProduct().getPrice());
        dto.setTotalPrice(cart.getTotalPrice());
        return dto;
    }
}