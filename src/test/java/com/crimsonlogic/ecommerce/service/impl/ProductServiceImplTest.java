package com.crimsonlogic.ecommerce.service.impl;

import com.crimsonlogic.ecommerce.dto.product.ProductRequestDTO;
import com.crimsonlogic.ecommerce.dto.product.ProductResponseDTO;
import com.crimsonlogic.ecommerce.entity.Category;
import com.crimsonlogic.ecommerce.entity.Inventory;
import com.crimsonlogic.ecommerce.entity.Product;
import com.crimsonlogic.ecommerce.entity.Seller;
import com.crimsonlogic.ecommerce.enumeration.ProductStatus;
import com.crimsonlogic.ecommerce.exception.ValidationException;
import com.crimsonlogic.ecommerce.repository.CategoryRepository;
import com.crimsonlogic.ecommerce.repository.InventoryRepository;
import com.crimsonlogic.ecommerce.repository.ProductRepository;
import com.crimsonlogic.ecommerce.repository.SellerRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Category category;
    private Seller seller;
    private Product sellerProduct;
    private Product adminProduct;

    @BeforeEach
    void setUp() {

        // -----------------------------
        // Category
        // -----------------------------

        category = new Category();
        category.setId("CAT001");
        category.setName("Electronics");
        category.setDescription("Electronic products");

        // -----------------------------
        // Seller
        // -----------------------------

        seller = new Seller();
        seller.setId("SEL001");
        seller.setShopName("Prajwal Electronics");

        // -----------------------------
        // Seller Product
        // -----------------------------

        sellerProduct = new Product();
        sellerProduct.setId("PRO001");
        sellerProduct.setName("Laptop");
        sellerProduct.setBrand("Dell");
        sellerProduct.setDescription("Dell Laptop");
        sellerProduct.setPrice(65000.0);
        sellerProduct.setCategory(category);
        sellerProduct.setSeller(seller);
        sellerProduct.setCreatedBy("SEL001");
        sellerProduct.setStatus(ProductStatus.AVAILABLE);

        // -----------------------------
        // Admin Product
        // -----------------------------

        adminProduct = new Product();
        adminProduct.setId("PRO002");
        adminProduct.setName("Keyboard");
        adminProduct.setBrand("Logitech");
        adminProduct.setDescription("Wireless Keyboard");
        adminProduct.setPrice(2000.0);
        adminProduct.setCategory(category);
        adminProduct.setSeller(null);
        adminProduct.setCreatedBy("ADM001");
        adminProduct.setStatus(ProductStatus.AVAILABLE);
    }

    // ==========================================================
    // ADD PRODUCT
    // ==========================================================

    @Test
    void shouldAddProductSuccessfullyForAdmin() {

        ProductRequestDTO request = new ProductRequestDTO();

        request.setName("Keyboard");
        request.setBrand("Logitech");
        request.setDescription("Wireless Keyboard");
        request.setPrice(2000.0);
        request.setCategoryName("Electronics");
        request.setQuantity(10);

        when(categoryRepository.findByName("Electronics"))
                .thenReturn(Optional.of(category));

        when(productRepository.save(any(Product.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        when(inventoryRepository.save(any(Inventory.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        ProductResponseDTO result =
                productService.addProduct(
                        request,
                        "ADM001",
                        "ADMIN"
                );

        assertNotNull(result);
        assertEquals("Keyboard", result.getName());
        assertEquals("Logitech", result.getBrand());
        assertEquals(2000.0, result.getPrice());
        assertEquals("Electronics", result.getCategoryName());
        assertEquals("Admin", result.getSellerName());
        assertEquals("AVAILABLE", result.getStatus());

        verify(categoryRepository)
                .findByName("Electronics");

        verify(productRepository)
                .save(any(Product.class));

        verify(inventoryRepository)
                .save(any(Inventory.class));

        verifyNoInteractions(sellerRepository);
    }

    @Test
    void shouldAddProductSuccessfullyForSeller() {

        ProductRequestDTO request = new ProductRequestDTO();

        request.setName("Laptop");
        request.setBrand("Dell");
        request.setDescription("Dell Laptop");
        request.setPrice(65000.0);
        request.setCategoryName("Electronics");
        request.setQuantity(10);

        when(categoryRepository.findByName("Electronics"))
                .thenReturn(Optional.of(category));

        when(sellerRepository.findById("SEL001"))
                .thenReturn(Optional.of(seller));

        when(productRepository.save(any(Product.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        when(inventoryRepository.save(any(Inventory.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        ProductResponseDTO result =
                productService.addProduct(
                        request,
                        "SEL001",
                        "SELLER"
                );

        assertNotNull(result);
        assertEquals("Laptop", result.getName());
        assertEquals("Dell", result.getBrand());
        assertEquals(65000.0, result.getPrice());
        assertEquals("Prajwal Electronics", result.getSellerName());
        assertEquals("AVAILABLE", result.getStatus());

        verify(categoryRepository)
                .findByName("Electronics");

        verify(sellerRepository)
                .findById("SEL001");

        verify(productRepository)
                .save(any(Product.class));

        verify(inventoryRepository)
                .save(any(Inventory.class));
    }

    @Test
    void shouldThrowExceptionWhenCategoryNotFoundWhileAddingProduct() {

        ProductRequestDTO request = new ProductRequestDTO();

        request.setName("Laptop");
        request.setBrand("Dell");
        request.setDescription("Laptop");
        request.setPrice(65000.0);
        request.setCategoryName("Unknown");
        request.setQuantity(10);

        when(categoryRepository.findByName("Unknown"))
                .thenReturn(Optional.empty());

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> productService.addProduct(
                                request,
                                "ADM001",
                                "ADMIN"
                        )
                );

        assertEquals(
                "Category 'Unknown' not found.",
                exception.getMessage()
        );

        verify(categoryRepository)
                .findByName("Unknown");

        verifyNoInteractions(productRepository);
        verifyNoInteractions(inventoryRepository);
        verifyNoInteractions(sellerRepository);
    }

    @Test
    void shouldThrowExceptionWhenSellerNotFoundWhileAddingProduct() {

        ProductRequestDTO request = new ProductRequestDTO();

        request.setName("Laptop");
        request.setBrand("Dell");
        request.setDescription("Laptop");
        request.setPrice(65000.0);
        request.setCategoryName("Electronics");
        request.setQuantity(10);

        when(categoryRepository.findByName("Electronics"))
                .thenReturn(Optional.of(category));

        when(sellerRepository.findById("SEL999"))
                .thenReturn(Optional.empty());

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> productService.addProduct(
                                request,
                                "SEL999",
                                "SELLER"
                        )
                );

        assertEquals(
                "Seller not found.",
                exception.getMessage()
        );

        verify(sellerRepository)
                .findById("SEL999");

        verify(productRepository, never())
                .save(any(Product.class));

        verify(inventoryRepository, never())
                .save(any(Inventory.class));
    }

    // ==========================================================
    // UPDATE PRODUCT
    // ==========================================================

    @Test
    void shouldUpdateOwnProductSuccessfullyForSeller() {

        ProductRequestDTO request = new ProductRequestDTO();

        request.setName("Updated Laptop");
        request.setBrand("HP");
        request.setDescription("Updated Laptop");
        request.setPrice(70000.0);
        request.setCategoryName("Electronics");
        request.setQuantity(10);

        when(productRepository.findById("PRO001"))
                .thenReturn(Optional.of(sellerProduct));

        when(categoryRepository.findByName("Electronics"))
                .thenReturn(Optional.of(category));

        when(productRepository.save(any(Product.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        ProductResponseDTO result =
                productService.updateProduct(
                        "PRO001",
                        request,
                        "SEL001",
                        "SELLER"
                );

        assertNotNull(result);

        assertEquals(
                "Updated Laptop",
                result.getName()
        );

        assertEquals(
                "HP",
                result.getBrand()
        );

        assertEquals(
                70000.0,
                result.getPrice()
        );

        verify(productRepository)
                .findById("PRO001");

        verify(categoryRepository)
                .findByName("Electronics");

        verify(productRepository)
                .save(sellerProduct);
    }

    @Test
    void shouldUpdateAdminProductSuccessfullyForAdmin() {

        ProductRequestDTO request = new ProductRequestDTO();

        request.setName("Updated Keyboard");
        request.setBrand("HP");
        request.setDescription("Updated Keyboard");
        request.setPrice(3000.0);
        request.setCategoryName("Electronics");
        request.setQuantity(10);

        when(productRepository.findById("PRO002"))
                .thenReturn(Optional.of(adminProduct));

        when(categoryRepository.findByName("Electronics"))
                .thenReturn(Optional.of(category));

        when(productRepository.save(any(Product.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        ProductResponseDTO result =
                productService.updateProduct(
                        "PRO002",
                        request,
                        "ADM001",
                        "ADMIN"
                );

        assertNotNull(result);

        assertEquals(
                "Updated Keyboard",
                result.getName()
        );

        assertEquals(
                "HP",
                result.getBrand()
        );

        assertEquals(
                3000.0,
                result.getPrice()
        );

        verify(productRepository)
                .save(adminProduct);
    }

    @Test
    void shouldRejectSellerUpdatingAnotherSellersProduct() {

        Seller anotherSeller = new Seller();
        anotherSeller.setId("SEL002");
        anotherSeller.setShopName("Another Shop");

        sellerProduct.setSeller(anotherSeller);

        ProductRequestDTO request = new ProductRequestDTO();

        request.setName("Updated Laptop");
        request.setBrand("HP");
        request.setDescription("Updated");
        request.setPrice(70000.0);
        request.setCategoryName("Electronics");
        request.setQuantity(10);

        when(productRepository.findById("PRO001"))
                .thenReturn(Optional.of(sellerProduct));

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> productService.updateProduct(
                                "PRO001",
                                request,
                                "SEL001",
                                "SELLER"
                        )
                );

        assertEquals(
                "You can only modify your own products.",
                exception.getMessage()
        );

        verify(productRepository)
                .findById("PRO001");

        verify(productRepository, never())
                .save(any(Product.class));

        verifyNoInteractions(categoryRepository);
    }

    @Test
    void shouldRejectAdminUpdatingSellerProduct() {

        ProductRequestDTO request = new ProductRequestDTO();

        request.setName("Updated Laptop");
        request.setBrand("HP");
        request.setDescription("Updated");
        request.setPrice(70000.0);
        request.setCategoryName("Electronics");
        request.setQuantity(10);

        when(productRepository.findById("PRO001"))
                .thenReturn(Optional.of(sellerProduct));

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> productService.updateProduct(
                                "PRO001",
                                request,
                                "ADM001",
                                "ADMIN"
                        )
                );

        assertEquals(
                "Admins can only edit admin-created products.",
                exception.getMessage()
        );

        verify(productRepository)
                .findById("PRO001");

        verify(productRepository, never())
                .save(any(Product.class));

        verifyNoInteractions(categoryRepository);
    }

    @Test
    void shouldThrowExceptionWhenProductNotFoundWhileUpdating() {

        ProductRequestDTO request = new ProductRequestDTO();

        when(productRepository.findById("PRO999"))
                .thenReturn(Optional.empty());

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> productService.updateProduct(
                                "PRO999",
                                request,
                                "ADM001",
                                "ADMIN"
                        )
                );

        assertEquals(
                "Product not found.",
                exception.getMessage()
        );

        verify(productRepository)
                .findById("PRO999");

        verifyNoInteractions(categoryRepository);
    }

    @Test
    void shouldThrowExceptionWhenCategoryNotFoundWhileUpdating() {

        ProductRequestDTO request = new ProductRequestDTO();

        request.setName("Updated Laptop");
        request.setBrand("HP");
        request.setDescription("Updated");
        request.setPrice(70000.0);
        request.setCategoryName("Unknown");
        request.setQuantity(10);

        when(productRepository.findById("PRO001"))
                .thenReturn(Optional.of(sellerProduct));

        when(categoryRepository.findByName("Unknown"))
                .thenReturn(Optional.empty());

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> productService.updateProduct(
                                "PRO001",
                                request,
                                "SEL001",
                                "SELLER"
                        )
                );

        assertEquals(
                "Category 'Unknown' not found.",
                exception.getMessage()
        );

        verify(productRepository, never())
                .save(any(Product.class));
    }

    // ==========================================================
    // GET ALL PRODUCTS
    // ==========================================================

    @Test
    void shouldGetAllProductsSuccessfully() {

        when(productRepository.findAll())
                .thenReturn(Arrays.asList(
                        sellerProduct,
                        adminProduct
                ));

        List<ProductResponseDTO> result =
                productService.getAllProducts();

        assertNotNull(result);

        assertEquals(
                2,
                result.size()
        );

        assertEquals(
                "Laptop",
                result.get(0).getName()
        );

        assertEquals(
                "Keyboard",
                result.get(1).getName()
        );

        verify(productRepository)
                .findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoProductsExist() {

        when(productRepository.findAll())
                .thenReturn(Collections.emptyList());

        List<ProductResponseDTO> result =
                productService.getAllProducts();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(productRepository)
                .findAll();
    }

    // ==========================================================
    // GET SELLER PRODUCTS
    // ==========================================================

    @Test
    void shouldGetSellerProductsSuccessfully() {

        when(productRepository.findBySellerId("SEL001"))
                .thenReturn(Arrays.asList(sellerProduct));

        List<ProductResponseDTO> result =
                productService.getProductsBySeller("SEL001");

        assertNotNull(result);

        assertEquals(
                1,
                result.size()
        );

        assertEquals(
                "PRO001",
                result.get(0).getId()
        );

        assertEquals(
                "Laptop",
                result.get(0).getName()
        );

        verify(productRepository)
                .findBySellerId("SEL001");
    }

    @Test
    void shouldReturnEmptyListWhenSellerHasNoProducts() {

        when(productRepository.findBySellerId("SEL999"))
                .thenReturn(Collections.emptyList());

        List<ProductResponseDTO> result =
                productService.getProductsBySeller("SEL999");

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(productRepository)
                .findBySellerId("SEL999");
    }

    // ==========================================================
    // DELETE PRODUCT
    // ==========================================================

    @Test
    void shouldDeleteOwnProductSuccessfullyForSeller() {

        Inventory inventory = new Inventory();
        inventory.setId("INV001");
        inventory.setProduct(sellerProduct);
        inventory.setQuantity(10);

        when(productRepository.findById("PRO001"))
                .thenReturn(Optional.of(sellerProduct));

        when(inventoryRepository.findByProductId("PRO001"))
                .thenReturn(Optional.of(inventory));

        String result =
                productService.deleteProduct(
                        "PRO001",
                        "SEL001",
                        "SELLER"
                );

        assertEquals(
                "Product deleted successfully.",
                result
        );

        verify(inventoryRepository)
                .findByProductId("PRO001");

        verify(inventoryRepository)
                .delete(inventory);

        verify(productRepository)
                .delete(sellerProduct);
    }

    @Test
    void shouldDeleteAdminProductSuccessfullyForAdmin() {

        when(productRepository.findById("PRO002"))
                .thenReturn(Optional.of(adminProduct));

        when(inventoryRepository.findByProductId("PRO002"))
                .thenReturn(Optional.empty());

        String result =
                productService.deleteProduct(
                        "PRO002",
                        "ADM001",
                        "ADMIN"
                );

        assertEquals(
                "Product deleted successfully.",
                result
        );

        verify(inventoryRepository)
                .findByProductId("PRO002");

        verify(productRepository)
                .delete(adminProduct);
    }

    @Test
    void shouldRejectSellerDeletingAnotherSellersProduct() {

        Seller anotherSeller = new Seller();
        anotherSeller.setId("SEL002");

        sellerProduct.setSeller(anotherSeller);

        when(productRepository.findById("PRO001"))
                .thenReturn(Optional.of(sellerProduct));

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> productService.deleteProduct(
                                "PRO001",
                                "SEL001",
                                "SELLER"
                        )
                );

        assertEquals(
                "You can only delete your own products.",
                exception.getMessage()
        );

        verify(productRepository, never())
                .delete(any(Product.class));

        verifyNoInteractions(inventoryRepository);
    }

    @Test
    void shouldRejectAdminDeletingSellerProduct() {

        when(productRepository.findById("PRO001"))
                .thenReturn(Optional.of(sellerProduct));

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> productService.deleteProduct(
                                "PRO001",
                                "ADM001",
                                "ADMIN"
                        )
                );

        assertEquals(
                "Admins can only delete admin-created products.",
                exception.getMessage()
        );

        verify(productRepository, never())
                .delete(any(Product.class));

        verifyNoInteractions(inventoryRepository);
    }

    @Test
    void shouldThrowExceptionWhenProductNotFoundWhileDeleting() {

        when(productRepository.findById("PRO999"))
                .thenReturn(Optional.empty());

        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> productService.deleteProduct(
                                "PRO999",
                                "ADM001",
                                "ADMIN"
                        )
                );

        assertEquals(
                "Product not found.",
                exception.getMessage()
        );

        verify(productRepository)
                .findById("PRO999");

        verifyNoInteractions(inventoryRepository);
    }

    @Test
    void shouldDeleteProductWhenInventoryDoesNotExist() {

        when(productRepository.findById("PRO002"))
                .thenReturn(Optional.of(adminProduct));

        when(inventoryRepository.findByProductId("PRO002"))
                .thenReturn(Optional.empty());

        String result =
                productService.deleteProduct(
                        "PRO002",
                        "ADM001",
                        "ADMIN"
                );

        assertEquals(
                "Product deleted successfully.",
                result
        );

        verify(inventoryRepository)
                .findByProductId("PRO002");

        verify(inventoryRepository, never())
                .delete(any(Inventory.class));

        verify(productRepository)
                .delete(adminProduct);
    }

    // ==========================================================
    // PAGINATION
    // ==========================================================

    @Test
    void shouldGetProductsWithPagination() {

        Page<Product> productPage =
                new PageImpl<>(
                        Arrays.asList(
                                sellerProduct,
                                adminProduct
                        )
                );

        when(productRepository.findAll(any(Pageable.class)))
                .thenReturn(productPage);

        Page<ProductResponseDTO> result =
                productService.getAllProductsPaginated(
                        0,
                        10,
                        "price"
                );

        assertNotNull(result);

        assertEquals(
                2,
                result.getTotalElements()
        );

        assertEquals(
                "Laptop",
                result.getContent()
                        .get(0)
                        .getName()
        );

        verify(productRepository)
                .findAll(any(Pageable.class));
    }

    // ==========================================================
    // SEARCH
    // ==========================================================

    @Test
    void shouldSearchProductsSuccessfully() {

        Page<Product> productPage =
                new PageImpl<>(
                        Arrays.asList(sellerProduct)
                );

        when(productRepository
                .findByNameContainingIgnoreCaseOrBrandContainingIgnoreCase(
                        eq("Dell"),
                        eq("Dell"),
                        any(Pageable.class)
                ))
                .thenReturn(productPage);

        List<ProductResponseDTO> result =
                productService.searchProducts("Dell");

        assertNotNull(result);

        assertEquals(1, result.size());

        assertEquals(
                "PRO001",
                result.get(0).getId()
        );

        assertEquals(
                "Laptop",
                result.get(0).getName()
        );

        assertEquals(
                "Dell",
                result.get(0).getBrand()
        );

        verify(productRepository)
                .findByNameContainingIgnoreCaseOrBrandContainingIgnoreCase(
                        eq("Dell"),
                        eq("Dell"),
                        any(Pageable.class)
                );
    }

    @Test
    void shouldReturnEmptyListWhenSearchHasNoResults() {

        Page<Product> emptyPage =
                new PageImpl<>(
                        Collections.emptyList()
                );

        when(productRepository
                .findByNameContainingIgnoreCaseOrBrandContainingIgnoreCase(
                        eq("XYZ"),
                        eq("XYZ"),
                        any(Pageable.class)
                ))
                .thenReturn(emptyPage);

        List<ProductResponseDTO> result =
                productService.searchProducts("XYZ");

        assertNotNull(result);

        assertTrue(result.isEmpty());

        verify(productRepository)
                .findByNameContainingIgnoreCaseOrBrandContainingIgnoreCase(
                        eq("XYZ"),
                        eq("XYZ"),
                        any(Pageable.class)
                );
    }

    // ==========================================================
    // FILTER
    // ==========================================================

    @Test
    void shouldFilterProductsSuccessfully() {

        Page<Product> productPage =
                new PageImpl<>(
                        Arrays.asList(sellerProduct)
                );

        when(productRepository.searchAndFilterProducts(
                eq("Laptop"),
                eq("Electronics"),
                eq(50000.0),
                eq(100000.0),
                any(Pageable.class)
        )).thenReturn(productPage);

        Page<ProductResponseDTO> result =
                productService.filterProducts(
                        "Laptop",
                        "Electronics",
                        50000.0,
                        100000.0,
                        0,
                        10,
                        "price"
                );

        assertNotNull(result);

        assertEquals(
                1,
                result.getTotalElements()
        );

        assertEquals(
                "Laptop",
                result.getContent()
                        .get(0)
                        .getName()
        );

        verify(productRepository)
                .searchAndFilterProducts(
                        eq("Laptop"),
                        eq("Electronics"),
                        eq(50000.0),
                        eq(100000.0),
                        any(Pageable.class)
                );
    }

    @Test
    void shouldReturnEmptyPageWhenFilterHasNoResults() {

        Page<Product> emptyPage =
                new PageImpl<>(
                        Collections.emptyList()
                );

        when(productRepository.searchAndFilterProducts(
                eq("XYZ"),
                eq("Unknown"),
                eq(50000.0),
                eq(100000.0),
                any(Pageable.class)
        )).thenReturn(emptyPage);

        Page<ProductResponseDTO> result =
                productService.filterProducts(
                        "XYZ",
                        "Unknown",
                        50000.0,
                        100000.0,
                        0,
                        10,
                        "price"
                );

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(productRepository)
                .searchAndFilterProducts(
                        eq("XYZ"),
                        eq("Unknown"),
                        eq(50000.0),
                        eq(100000.0),
                        any(Pageable.class)
                );
    }
}