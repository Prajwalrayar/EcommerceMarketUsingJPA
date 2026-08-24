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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    private Product product;
    private Inventory inventory;
    private Customer customer;

    @BeforeEach
    void setUp() {

        product = new Product();
        product.setId("PROD001");
        product.setName("Laptop");
        product.setPrice(65000.0);

        inventory = new Inventory();
        inventory.setId("INV001");
        inventory.setProduct(product);
        inventory.setQuantity(10);

        customer = new Customer();
        customer.setId("CUS001");
        customer.setName("Prajwal");
    }

    // ==========================================================
    // ADD TO CART
    // ==========================================================

    @Test
    void shouldAddNewProductToCartSuccessfully() {

        CartRequestDTO request = new CartRequestDTO();
        request.setProductName("Laptop");
        request.setQuantity(2);

        when(productRepository.findByName("Laptop"))
                .thenReturn(Optional.of(product));

        when(inventoryRepository.findByProductId("PROD001"))
                .thenReturn(Optional.of(inventory));

        when(cartRepository.findByCustomerIdAndProductId(
                "CUS001",
                "PROD001"
        )).thenReturn(Optional.empty());

        when(customerRepository.findById("CUS001"))
                .thenReturn(Optional.of(customer));

        String result = cartService.addToCart(request, "CUS001");

        assertEquals(
                "Product added to cart successfully.",
                result
        );

        verify(productRepository)
                .findByName("Laptop");

        verify(inventoryRepository)
                .findByProductId("PROD001");

        verify(customerRepository)
                .findById("CUS001");

        verify(cartRepository)
                .save(any(Cart.class));
    }

    // ==========================================================
    // ADD TO EXISTING CART
    // ==========================================================

    @Test
    void shouldIncreaseQuantityWhenProductAlreadyExistsInCart() {

        CartRequestDTO request = new CartRequestDTO();
        request.setProductName("Laptop");
        request.setQuantity(2);

        Cart existingCart = new Cart();
        existingCart.setId("CRT001");
        existingCart.setCustomer(customer);
        existingCart.setProduct(product);
        existingCart.setQuantity(3);

        when(productRepository.findByName("Laptop"))
                .thenReturn(Optional.of(product));

        when(inventoryRepository.findByProductId("PROD001"))
                .thenReturn(Optional.of(inventory));

        when(cartRepository.findByCustomerIdAndProductId(
                "CUS001",
                "PROD001"
        )).thenReturn(Optional.of(existingCart));

        String result =
                cartService.addToCart(request, "CUS001");

        assertEquals(
                "Product added to cart successfully.",
                result
        );

        assertEquals(
                5,
                existingCart.getQuantity()
        );

        verify(cartRepository)
                .save(existingCart);

        verify(customerRepository, never())
                .findById(anyString());
    }

    // ==========================================================
    // PRODUCT NOT FOUND
    // ==========================================================

    @Test
    void shouldThrowExceptionWhenProductDoesNotExist() {

        CartRequestDTO request = new CartRequestDTO();
        request.setProductName("Mobile");
        request.setQuantity(2);

        when(productRepository.findByName("Mobile"))
                .thenReturn(Optional.empty());

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> cartService.addToCart(
                                request,
                                "CUS001"
                        )
                );

        assertEquals(
                "Product 'Mobile' not found.",
                exception.getMessage()
        );

        verify(productRepository)
                .findByName("Mobile");

        verifyNoInteractions(inventoryRepository);
        verifyNoInteractions(cartRepository);
    }

    // ==========================================================
    // INVENTORY NOT FOUND
    // ==========================================================

    @Test
    void shouldThrowExceptionWhenInventoryDoesNotExist() {

        CartRequestDTO request = new CartRequestDTO();
        request.setProductName("Laptop");
        request.setQuantity(2);

        when(productRepository.findByName("Laptop"))
                .thenReturn(Optional.of(product));

        when(inventoryRepository.findByProductId("PROD001"))
                .thenReturn(Optional.empty());

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> cartService.addToCart(
                                request,
                                "CUS001"
                        )
                );

        assertEquals(
                "Inventory missing for product.",
                exception.getMessage()
        );

        verifyNoInteractions(cartRepository);
    }

    // ==========================================================
    // INSUFFICIENT STOCK
    // ==========================================================

    @Test
    void shouldThrowExceptionWhenStockIsInsufficient() {

        CartRequestDTO request = new CartRequestDTO();
        request.setProductName("Laptop");
        request.setQuantity(15);

        when(productRepository.findByName("Laptop"))
                .thenReturn(Optional.of(product));

        when(inventoryRepository.findByProductId("PROD001"))
                .thenReturn(Optional.of(inventory));

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> cartService.addToCart(
                                request,
                                "CUS001"
                        )
                );

        assertEquals(
                "Insufficient stock. Only 10 available.",
                exception.getMessage()
        );

        verifyNoInteractions(cartRepository);
    }

    // ==========================================================
    // EXISTING CART + EXCEEDS STOCK
    // ==========================================================

    @Test
    void shouldThrowExceptionWhenUpdatedQuantityExceedsStock() {

        CartRequestDTO request = new CartRequestDTO();
        request.setProductName("Laptop");
        request.setQuantity(8);

        Cart existingCart = new Cart();
        existingCart.setId("CRT001");
        existingCart.setCustomer(customer);
        existingCart.setProduct(product);
        existingCart.setQuantity(5);

        when(productRepository.findByName("Laptop"))
                .thenReturn(Optional.of(product));

        when(inventoryRepository.findByProductId("PROD001"))
                .thenReturn(Optional.of(inventory));

        when(cartRepository.findByCustomerIdAndProductId(
                "CUS001",
                "PROD001"
        )).thenReturn(Optional.of(existingCart));

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> cartService.addToCart(
                                request,
                                "CUS001"
                        )
                );

        assertEquals(
                "Requested quantity exceeds available stock.",
                exception.getMessage()
        );

        verify(cartRepository, never())
                .save(any(Cart.class));
    }

    // ==========================================================
    // VIEW CART
    // ==========================================================

    @Test
    void shouldViewCustomerCartSuccessfully() {

        Cart cart = new Cart();

        cart.setId("CRT001");
        cart.setCustomer(customer);
        cart.setProduct(product);
        cart.setQuantity(2);

        when(cartRepository.findByCustomerId("CUS001"))
                .thenReturn(Arrays.asList(cart));

        List<CartResponseDTO> result =
                cartService.viewCart("CUS001");

        assertNotNull(result);
        assertEquals(1, result.size());

        CartResponseDTO dto = result.get(0);

        assertEquals("CRT001", dto.getCartId());
        assertEquals("PROD001", dto.getProductId());
        assertEquals("Laptop", dto.getProductName());
        assertEquals(2, dto.getQuantity());
        assertEquals(65000.0, dto.getUnitPrice());

        verify(cartRepository)
                .findByCustomerId("CUS001");
    }

    // ==========================================================
    // VIEW EMPTY CART
    // ==========================================================

    @Test
    void shouldReturnEmptyCartWhenCustomerHasNoItems() {

        when(cartRepository.findByCustomerId("CUS001"))
                .thenReturn(Collections.emptyList());

        List<CartResponseDTO> result =
                cartService.viewCart("CUS001");

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(cartRepository)
                .findByCustomerId("CUS001");
    }

    // ==========================================================
    // REMOVE CART ITEM
    // ==========================================================

    @Test
    void shouldRemoveCartItemSuccessfully() {

        Cart cart = new Cart();

        cart.setId("CRT001");
        cart.setCustomer(customer);
        cart.setProduct(product);
        cart.setQuantity(2);

        when(cartRepository.findById("CRT001"))
                .thenReturn(Optional.of(cart));

        String result =
                cartService.removeFromCart(
                        "CRT001",
                        "CUS001"
                );

        assertEquals(
                "Item removed from cart.",
                result
        );

        verify(cartRepository)
                .findById("CRT001");

        verify(cartRepository)
                .delete(cart);
    }

    // ==========================================================
    // CART ITEM NOT FOUND
    // ==========================================================

    @Test
    void shouldThrowExceptionWhenCartItemDoesNotExist() {

        when(cartRepository.findById("CRT001"))
                .thenReturn(Optional.empty());

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> cartService.removeFromCart(
                                "CRT001",
                                "CUS001"
                        )
                );

        assertEquals(
                "Cart item not found.",
                exception.getMessage()
        );

        verify(cartRepository)
                .findById("CRT001");

        verify(cartRepository, never())
                .delete(any(Cart.class));
    }

    // ==========================================================
    // REMOVE SOMEONE ELSE'S CART ITEM
    // ==========================================================

    @Test
    void shouldNotAllowCustomerToRemoveAnotherCustomersCartItem() {

        Customer anotherCustomer = new Customer();
        anotherCustomer.setId("CUS002");

        Cart cart = new Cart();

        cart.setId("CRT001");
        cart.setCustomer(anotherCustomer);
        cart.setProduct(product);
        cart.setQuantity(2);

        when(cartRepository.findById("CRT001"))
                .thenReturn(Optional.of(cart));

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> cartService.removeFromCart(
                                "CRT001",
                                "CUS001"
                        )
                );

        assertEquals(
                "You can only remove items from your own cart.",
                exception.getMessage()
        );

        verify(cartRepository, never())
                .delete(any(Cart.class));
    }
}