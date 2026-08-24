package com.crimsonlogic.ecommerce.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    void shouldCreateCategoryWithNullValues() {

        Category category = new Category();

        assertNotNull(category);

        assertNull(category.getId());
        assertNull(category.getName());
        assertNull(category.getDescription());
    }

    @Test
    void shouldSetAndGetId() {

        Category category = new Category();

        category.setId("CAT001");

        assertEquals("CAT001", category.getId());
    }

    @Test
    void shouldSetAndGetName() {

        Category category = new Category();

        category.setName("Electronics");

        assertEquals("Electronics", category.getName());
    }

    @Test
    void shouldSetAndGetDescription() {

        Category category = new Category();

        category.setDescription("Electronic products and accessories");

        assertEquals(
                "Electronic products and accessories",
                category.getDescription()
        );
    }

    @Test
    void shouldSetAndGetCompleteCategory() {

        Category category = new Category();

        category.setId("CAT001");
        category.setName("Electronics");
        category.setDescription("Electronic products and accessories");

        assertEquals("CAT001", category.getId());
        assertEquals("Electronics", category.getName());
        assertEquals(
                "Electronic products and accessories",
                category.getDescription()
        );
    }
}