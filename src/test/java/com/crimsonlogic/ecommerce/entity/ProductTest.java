package com.crimsonlogic.ecommerce.entity;

import com.crimsonlogic.ecommerce.enumeration.ProductStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void shouldCreateProductWithDefaultValues() {

        Product product = new Product();

        assertNotNull(product);

        assertNull(product.getId());
        assertNull(product.getName());
        assertNull(product.getBrand());
        assertNull(product.getDescription());
        assertNull(product.getPrice());

        assertEquals(
                ProductStatus.OUT_OF_STOCK,
                product.getStatus()
        );

        assertNull(product.getCreatedBy());
        assertNull(product.getCategory());
        assertNull(product.getSeller());
    }

    @Test
    void shouldSetAndGetId() {

        Product product = new Product();

        product.setId("PROD001");

        assertEquals("PROD001", product.getId());
    }

    @Test
    void shouldSetAndGetName() {

        Product product = new Product();

        product.setName("Laptop");

        assertEquals("Laptop", product.getName());
    }

    @Test
    void shouldSetAndGetBrand() {

        Product product = new Product();

        product.setBrand("Dell");

        assertEquals("Dell", product.getBrand());
    }

    @Test
    void shouldSetAndGetDescription() {

        Product product = new Product();

        product.setDescription("Dell laptop with 16GB RAM");

        assertEquals(
                "Dell laptop with 16GB RAM",
                product.getDescription()
        );
    }

    @Test
    void shouldSetAndGetPrice() {

        Product product = new Product();

        product.setPrice(65000.0);

        assertEquals(65000.0, product.getPrice());
    }

    @Test
    void shouldSetAndGetStatus() {

        Product product = new Product();

        product.setStatus(ProductStatus.AVAILABLE);

        assertEquals(
                ProductStatus.AVAILABLE,
                product.getStatus()
        );
    }

    @Test
    void shouldSetAndGetCreatedBy() {

        Product product = new Product();

        product.setCreatedBy("ADMIN001");

        assertEquals("ADMIN001", product.getCreatedBy());
    }

    @Test
    void shouldSetAndGetCategory() {

        Product product = new Product();
        Category category = new Category();

        product.setCategory(category);

        assertEquals(category, product.getCategory());
    }

    @Test
    void shouldSetAndGetSeller() {

        Product product = new Product();
        Seller seller = new Seller();

        product.setSeller(seller);

        assertEquals(seller, product.getSeller());
    }

    @Test
    void shouldAllowNullSeller() {

        Product product = new Product();

        product.setSeller(null);

        assertNull(product.getSeller());
    }

    @Test
    void shouldSetAndGetCompleteProduct() {

        Product product = new Product();

        Category category = new Category();
        Seller seller = new Seller();

        product.setId("PROD001");
        product.setName("Laptop");
        product.setBrand("Dell");
        product.setDescription("Dell laptop with 16GB RAM");
        product.setPrice(65000.0);
        product.setStatus(ProductStatus.AVAILABLE);
        product.setCreatedBy("SELLER001");
        product.setCategory(category);
        product.setSeller(seller);

        assertEquals("PROD001", product.getId());
        assertEquals("Laptop", product.getName());
        assertEquals("Dell", product.getBrand());
        assertEquals(
                "Dell laptop with 16GB RAM",
                product.getDescription()
        );
        assertEquals(65000.0, product.getPrice());
        assertEquals(ProductStatus.AVAILABLE, product.getStatus());
        assertEquals("SELLER001", product.getCreatedBy());
        assertEquals(category, product.getCategory());
        assertEquals(seller, product.getSeller());
    }
}