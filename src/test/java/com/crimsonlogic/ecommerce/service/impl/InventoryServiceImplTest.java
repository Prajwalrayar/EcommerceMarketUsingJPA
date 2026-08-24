package com.crimsonlogic.ecommerce.service.impl;

import com.crimsonlogic.ecommerce.dto.inventory.InventoryResponseDTO;
import com.crimsonlogic.ecommerce.dto.inventory.InventoryUpdateRequestDTO;
import com.crimsonlogic.ecommerce.entity.Inventory;
import com.crimsonlogic.ecommerce.entity.Product;
import com.crimsonlogic.ecommerce.entity.Seller;
import com.crimsonlogic.ecommerce.enumeration.ProductStatus;
import com.crimsonlogic.ecommerce.exception.ValidationException;
import com.crimsonlogic.ecommerce.repository.InventoryRepository;
import com.crimsonlogic.ecommerce.repository.ProductRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceImplTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    private Product product;
    private Inventory inventory;
    private Seller seller;

    @BeforeEach
    void setUp() {

        seller = new Seller();
        seller.setId("SEL001");

        product = new Product();
        product.setId("PROD001");
        product.setName("Laptop");
        product.setStatus(ProductStatus.OUT_OF_STOCK);

        inventory = new Inventory();
        inventory.setId("INV001");
        inventory.setProduct(product);
        inventory.setQuantity(10);
    }

    // ==========================================================
    // UPDATE BY PRODUCT ID - SELLER
    // ==========================================================

    @Test
    void shouldUpdateInventoryByIdForOwnSellerProduct() {

        product.setSeller(seller);

        InventoryUpdateRequestDTO request =
                new InventoryUpdateRequestDTO();

        request.setQuantity(25);

        when(productRepository.findById("PROD001"))
                .thenReturn(Optional.of(product));

        when(inventoryRepository.findByProductId("PROD001"))
                .thenReturn(Optional.of(inventory));

        when(productRepository.save(any(Product.class)))
                .thenReturn(product);

        when(inventoryRepository.save(any(Inventory.class)))
                .thenReturn(inventory);

        InventoryResponseDTO result =
                inventoryService.updateQuantityById(
                        "PROD001",
                        request,
                        "SEL001",
                        "SELLER"
                );

        assertNotNull(result);

        assertEquals(25, inventory.getQuantity());

        assertEquals(
                ProductStatus.AVAILABLE,
                product.getStatus()
        );

        verify(productRepository)
                .findById("PROD001");

        verify(inventoryRepository)
                .findByProductId("PROD001");

        verify(productRepository)
                .save(product);

        verify(inventoryRepository)
                .save(inventory);
    }

    // ==========================================================
    // UPDATE BY ID - ADMIN PRODUCT
    // ==========================================================

    @Test
    void shouldUpdateAdminProductForAdmin() {

        // seller == null means admin-created product
        product.setSeller(null);

        InventoryUpdateRequestDTO request =
                new InventoryUpdateRequestDTO();

        request.setQuantity(20);

        when(productRepository.findById("PROD001"))
                .thenReturn(Optional.of(product));

        when(inventoryRepository.findByProductId("PROD001"))
                .thenReturn(Optional.of(inventory));

        when(productRepository.save(any(Product.class)))
                .thenReturn(product);

        when(inventoryRepository.save(any(Inventory.class)))
                .thenReturn(inventory);

        InventoryResponseDTO result =
                inventoryService.updateQuantityById(
                        "PROD001",
                        request,
                        "ADM001",
                        "ADMIN"
                );

        assertNotNull(result);

        assertEquals(20, inventory.getQuantity());

        assertEquals(
                ProductStatus.AVAILABLE,
                product.getStatus()
        );

        verify(productRepository)
                .save(product);

        verify(inventoryRepository)
                .save(inventory);
    }

    // ==========================================================
    // UPDATE BY NAME
    // ==========================================================

    @Test
    void shouldUpdateInventoryByProductName() {

        product.setSeller(seller);

        InventoryUpdateRequestDTO request =
                new InventoryUpdateRequestDTO();

        request.setQuantity(15);

        when(productRepository.findByName("Laptop"))
                .thenReturn(Optional.of(product));

        when(inventoryRepository.findByProductId("PROD001"))
                .thenReturn(Optional.of(inventory));

        when(productRepository.save(any(Product.class)))
                .thenReturn(product);

        when(inventoryRepository.save(any(Inventory.class)))
                .thenReturn(inventory);

        InventoryResponseDTO result =
                inventoryService.updateQuantityByName(
                        "Laptop",
                        request,
                        "SEL001",
                        "SELLER"
                );

        assertNotNull(result);

        assertEquals(
                15,
                inventory.getQuantity()
        );

        verify(productRepository)
                .findByName("Laptop");

        verify(inventoryRepository)
                .save(inventory);
    }

    // ==========================================================
    // PRODUCT ID NOT FOUND
    // ==========================================================

    @Test
    void shouldThrowExceptionWhenProductIdDoesNotExist() {

        InventoryUpdateRequestDTO request =
                new InventoryUpdateRequestDTO();

        request.setQuantity(10);

        when(productRepository.findById("PROD999"))
                .thenReturn(Optional.empty());

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> inventoryService.updateQuantityById(
                                "PROD999",
                                request,
                                "ADM001",
                                "ADMIN"
                        )
                );

        assertEquals(
                "Product with ID 'PROD999' not found.",
                exception.getMessage()
        );

        verify(productRepository)
                .findById("PROD999");

        verifyNoInteractions(inventoryRepository);
    }

    // ==========================================================
    // PRODUCT NAME NOT FOUND
    // ==========================================================

    @Test
    void shouldThrowExceptionWhenProductNameDoesNotExist() {

        InventoryUpdateRequestDTO request =
                new InventoryUpdateRequestDTO();

        request.setQuantity(10);

        when(productRepository.findByName("Mobile"))
                .thenReturn(Optional.empty());

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> inventoryService.updateQuantityByName(
                                "Mobile",
                                request,
                                "ADM001",
                                "ADMIN"
                        )
                );

        assertEquals(
                "Product 'Mobile' not found.",
                exception.getMessage()
        );

        verify(productRepository)
                .findByName("Mobile");

        verifyNoInteractions(inventoryRepository);
    }

    // ==========================================================
    // SELLER CANNOT UPDATE ANOTHER SELLER'S PRODUCT
    // ==========================================================

    @Test
    void shouldRejectSellerUpdatingAnotherSellersProduct() {

        Seller anotherSeller = new Seller();
        anotherSeller.setId("SEL002");

        product.setSeller(anotherSeller);

        InventoryUpdateRequestDTO request =
                new InventoryUpdateRequestDTO();

        request.setQuantity(20);

        when(productRepository.findById("PROD001"))
                .thenReturn(Optional.of(product));

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> inventoryService.updateQuantityById(
                                "PROD001",
                                request,
                                "SEL001",
                                "SELLER"
                        )
                );

        assertEquals(
                "You can only manage inventory for your own products.",
                exception.getMessage()
        );

        verify(productRepository)
                .findById("PROD001");

        verifyNoInteractions(inventoryRepository);

        verify(productRepository, never())
                .save(any(Product.class));
    }

    // ==========================================================
    // ADMIN CANNOT UPDATE SELLER PRODUCT
    // ==========================================================

    @Test
    void shouldRejectAdminUpdatingSellerProduct() {

        product.setSeller(seller);

        InventoryUpdateRequestDTO request =
                new InventoryUpdateRequestDTO();

        request.setQuantity(20);

        when(productRepository.findById("PROD001"))
                .thenReturn(Optional.of(product));

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> inventoryService.updateQuantityById(
                                "PROD001",
                                request,
                                "ADM001",
                                "ADMIN"
                        )
                );

        assertEquals(
                "Admins can only manage inventory for admin-created products.",
                exception.getMessage()
        );

        verifyNoInteractions(inventoryRepository);

        verify(productRepository, never())
                .save(any(Product.class));
    }

    // ==========================================================
    // CREATE INVENTORY IF IT DOES NOT EXIST
    // ==========================================================

    @Test
    void shouldCreateInventoryWhenInventoryDoesNotExist() {

        product.setSeller(null);

        InventoryUpdateRequestDTO request =
                new InventoryUpdateRequestDTO();

        request.setQuantity(30);

        when(productRepository.findById("PROD001"))
                .thenReturn(Optional.of(product));

        when(inventoryRepository.findByProductId("PROD001"))
                .thenReturn(Optional.empty());

        when(productRepository.save(any(Product.class)))
                .thenReturn(product);

        when(inventoryRepository.save(any(Inventory.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        InventoryResponseDTO result =
                inventoryService.updateQuantityById(
                        "PROD001",
                        request,
                        "ADM001",
                        "ADMIN"
                );

        assertNotNull(result);

        ArgumentCaptor<Inventory> captor =
                ArgumentCaptor.forClass(Inventory.class);

        verify(inventoryRepository)
                .save(captor.capture());

        Inventory savedInventory =
                captor.getValue();

        assertEquals(
                "PROD001",
                savedInventory.getProduct().getId()
        );

        assertEquals(
                30,
                savedInventory.getQuantity()
        );

        assertNotNull(
                savedInventory.getId()
        );
    }

    // ==========================================================
    // QUANTITY > 0 -> AVAILABLE
    // ==========================================================

    @Test
    void shouldSetProductAvailableWhenQuantityIsGreaterThanZero() {

        product.setSeller(null);

        InventoryUpdateRequestDTO request =
                new InventoryUpdateRequestDTO();

        request.setQuantity(5);

        when(productRepository.findById("PROD001"))
                .thenReturn(Optional.of(product));

        when(inventoryRepository.findByProductId("PROD001"))
                .thenReturn(Optional.of(inventory));

        when(productRepository.save(any(Product.class)))
                .thenReturn(product);

        when(inventoryRepository.save(any(Inventory.class)))
                .thenReturn(inventory);

        inventoryService.updateQuantityById(
                "PROD001",
                request,
                "ADM001",
                "ADMIN"
        );

        assertEquals(
                ProductStatus.AVAILABLE,
                product.getStatus()
        );
    }

    // ==========================================================
    // QUANTITY = 0 -> OUT OF STOCK
    // ==========================================================

    @Test
    void shouldSetProductOutOfStockWhenQuantityIsZero() {

        product.setSeller(null);

        InventoryUpdateRequestDTO request =
                new InventoryUpdateRequestDTO();

        request.setQuantity(0);

        when(productRepository.findById("PROD001"))
                .thenReturn(Optional.of(product));

        when(inventoryRepository.findByProductId("PROD001"))
                .thenReturn(Optional.of(inventory));

        when(productRepository.save(any(Product.class)))
                .thenReturn(product);

        when(inventoryRepository.save(any(Inventory.class)))
                .thenReturn(inventory);

        inventoryService.updateQuantityById(
                "PROD001",
                request,
                "ADM001",
                "ADMIN"
        );

        assertEquals(
                ProductStatus.OUT_OF_STOCK,
                product.getStatus()
        );
    }

    // ==========================================================
    // SELLER INVENTORY
    // ==========================================================

    @Test
    void shouldGetSellerInventory() {

        product.setSeller(seller);

        when(inventoryRepository.findByProductSellerId("SEL001"))
                .thenReturn(Arrays.asList(inventory));

        List<InventoryResponseDTO> result =
                inventoryService.getInventoryByRole(
                        "SEL001",
                        "SELLER"
                );

        assertNotNull(result);

        assertEquals(
                1,
                result.size()
        );

        assertEquals(
                "INV001",
                result.get(0).getInventoryId()
        );

        assertEquals(
                "PROD001",
                result.get(0).getProductId()
        );

        assertEquals(
                "Laptop",
                result.get(0).getProductName()
        );

        assertEquals(
                10,
                result.get(0).getQuantity()
        );

        assertEquals(
                product.getStatus().name(),
                result.get(0).getProductStatus()
        );

        verify(inventoryRepository)
                .findByProductSellerId("SEL001");
    }

    // ==========================================================
    // ADMIN INVENTORY
    // ==========================================================

    @Test
    void shouldGetAdminInventory() {

        product.setSeller(null);

        when(inventoryRepository.findByProductSellerIsNull())
                .thenReturn(Arrays.asList(inventory));

        List<InventoryResponseDTO> result =
                inventoryService.getInventoryByRole(
                        "ADM001",
                        "ADMIN"
                );

        assertNotNull(result);

        assertEquals(
                1,
                result.size()
        );

        assertEquals(
                "INV001",
                result.get(0).getInventoryId()
        );

        verify(inventoryRepository)
                .findByProductSellerIsNull();
    }

    // ==========================================================
    // EMPTY INVENTORY
    // ==========================================================

    @Test
    void shouldReturnEmptyInventoryWhenNoProductsExist() {

        when(inventoryRepository.findByProductSellerIsNull())
                .thenReturn(Collections.emptyList());

        List<InventoryResponseDTO> result =
                inventoryService.getInventoryByRole(
                        "ADM001",
                        "ADMIN"
                );

        assertNotNull(result);

        assertTrue(result.isEmpty());

        verify(inventoryRepository)
                .findByProductSellerIsNull();
    }
}