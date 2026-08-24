package com.crimsonlogic.ecommerce.controller;

import com.crimsonlogic.ecommerce.dto.product.ProductRequestDTO;
import com.crimsonlogic.ecommerce.dto.product.ProductResponseDTO;
import com.crimsonlogic.ecommerce.service.ProductService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;


    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(productController)
                .build();
    }


    // ==========================================================
    // ADD PRODUCT
    // ==========================================================

    @Test
    void shouldAddProductSuccessfully()
            throws Exception {

        ProductResponseDTO response =
                new ProductResponseDTO();

        when(productService.addProduct(
                any(ProductRequestDTO.class),
                eq("SEL001"),
                eq("SELLER")
        )).thenReturn(response);

        String requestJson = """
                {
                    "name": "Laptop",
                    "brand": "Dell",
                    "description": "Dell laptop",
                    "price": 65000,
                    "quantity": 10,
                    "categoryName": "Electronics"
                }
                """;

        mockMvc.perform(
                        post("/api/v1/products")
                                .requestAttr("userId", "SEL001")
                                .requestAttr("role", "SELLER")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isCreated());

        verify(productService)
                .addProduct(
                        any(ProductRequestDTO.class),
                        eq("SEL001"),
                        eq("SELLER")
                );
    }


    // ==========================================================
    // UPDATE PRODUCT
    // ==========================================================

    @Test
    void shouldUpdateProductSuccessfully()
            throws Exception {

        ProductResponseDTO response =
                new ProductResponseDTO();

        when(productService.updateProduct(
                eq("PROD001"),
                any(ProductRequestDTO.class),
                eq("SEL001"),
                eq("SELLER")
        )).thenReturn(response);

        String requestJson = """
                {
                    "name": "Updated Laptop",
                    "brand": "Dell",
                    "description": "Updated Dell laptop",
                    "price": 70000,
                    "quantity": 15,
                    "categoryName": "Electronics"
                }
                """;

        mockMvc.perform(
                        put("/api/v1/products/PROD001")
                                .requestAttr("userId", "SEL001")
                                .requestAttr("role", "SELLER")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isOk());

        verify(productService)
                .updateProduct(
                        eq("PROD001"),
                        any(ProductRequestDTO.class),
                        eq("SEL001"),
                        eq("SELLER")
                );
    }


    // ==========================================================
    // GET ALL PRODUCTS
    // ==========================================================

    @Test
    void shouldGetAllProductsSuccessfully()
            throws Exception {

        List<ProductResponseDTO> products =
                Collections.emptyList();

        when(productService.getAllProducts())
                .thenReturn(products);

        mockMvc.perform(
                        get("/api/v1/products")
                )
                .andExpect(status().isOk());

        verify(productService)
                .getAllProducts();
    }


    // ==========================================================
    // GET SELLER PRODUCTS
    // ==========================================================

    @Test
    void shouldGetSellerProductsSuccessfully()
            throws Exception {

        List<ProductResponseDTO> products =
                Collections.emptyList();

        when(productService.getProductsBySeller("SEL001"))
                .thenReturn(products);

        mockMvc.perform(
                        get("/api/v1/products/seller/SEL001")
                )
                .andExpect(status().isOk());

        verify(productService)
                .getProductsBySeller("SEL001");
    }


    // ==========================================================
    // DELETE PRODUCT
    // ==========================================================

    @Test
    void shouldDeleteProductSuccessfully()
            throws Exception {

        when(productService.deleteProduct(
                "PROD001",
                "SEL001",
                "SELLER"
        )).thenReturn("Product deleted successfully");

        mockMvc.perform(
                        delete("/api/v1/products/delete/PROD001")
                                .requestAttr("userId", "SEL001")
                                .requestAttr("role", "SELLER")
                )
                .andExpect(status().isOk());

        verify(productService)
                .deleteProduct(
                        "PROD001",
                        "SEL001",
                        "SELLER"
                );
    }


    // ==========================================================
    // GET MY PRODUCTS
    // ==========================================================

    @Test
    void shouldGetMyProductsSuccessfullyForSeller()
            throws Exception {

        List<ProductResponseDTO> products =
                Collections.emptyList();

        when(productService.getProductsBySeller("SEL001"))
                .thenReturn(products);

        mockMvc.perform(
                        get("/api/v1/products/my-products")
                                .requestAttr("userId", "SEL001")
                                .requestAttr("role", "SELLER")
                )
                .andExpect(status().isOk());

        verify(productService)
                .getProductsBySeller("SEL001");
    }


    @Test
    void shouldRejectMyProductsForCustomer()
            throws Exception {

        mockMvc.perform(
                        get("/api/v1/products/my-products")
                                .requestAttr("userId", "CUS001")
                                .requestAttr("role", "CUSTOMER")
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(productService);
    }


    @Test
    void shouldRejectMyProductsForAdmin()
            throws Exception {

        mockMvc.perform(
                        get("/api/v1/products/my-products")
                                .requestAttr("userId", "ADM001")
                                .requestAttr("role", "ADMIN")
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(productService);
    }


    // ==========================================================
    // PAGINATED PRODUCTS
    // ==========================================================

    @Test
    void shouldGetProductsPaginatedSuccessfully()
            throws Exception {

        Page<ProductResponseDTO> page =
                new PageImpl<>(
                        Collections.emptyList()
                );

        when(productService.getAllProductsPaginated(
                0,
                10,
                "name"
        )).thenReturn(page);

        mockMvc.perform(
                        get("/api/v1/products/paged")
                                .param("page", "0")
                                .param("size", "10")
                                .param("sortBy", "name")
                )
                .andExpect(status().isOk());

        verify(productService)
                .getAllProductsPaginated(
                        0,
                        10,
                        "name"
                );
    }


    // ==========================================================
    // SEARCH PRODUCTS
    // ==========================================================

    @Test
    void shouldSearchProductsSuccessfully()
            throws Exception {

        List<ProductResponseDTO> products =
                Collections.emptyList();

        when(productService.searchProducts("laptop"))
                .thenReturn(products);

        mockMvc.perform(
                        get("/api/v1/products/search")
                                .param("keyword", "laptop")
                )
                .andExpect(status().isOk());

        verify(productService)
                .searchProducts("laptop");
    }


    // ==========================================================
    // FILTER PRODUCTS
    // ==========================================================

    @Test
    void shouldFilterProductsSuccessfully()
            throws Exception {

        Page<ProductResponseDTO> page =
                new PageImpl<>(
                        Collections.emptyList()
                );

        when(productService.filterProducts(
                "laptop",
                "Electronics",
                500.0,
                100000.0,
                0,
                10,
                "price"
        )).thenReturn(page);

        mockMvc.perform(
                        get("/api/v1/products/filter")
                                .param("keyword", "laptop")
                                .param("categoryName", "Electronics")
                                .param("minPrice", "500")
                                .param("maxPrice", "100000")
                                .param("page", "0")
                                .param("size", "10")
                                .param("sortBy", "price")
                )
                .andExpect(status().isOk());

        verify(productService)
                .filterProducts(
                        "laptop",
                        "Electronics",
                        500.0,
                        100000.0,
                        0,
                        10,
                        "price"
                );
    }
}