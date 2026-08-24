package com.crimsonlogic.ecommerce.entity.abstraction;
import com.crimsonlogic.ecommerce.enumeration.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserTest {

    // Concrete class for testing the abstract User class
    static class TestUser extends User {

        public TestUser() {
            super();
        }
    }

    @Test
    void shouldSetAndGetId() {

        TestUser user = new TestUser();

        user.setId("USR001");

        assertEquals("USR001", user.getId());
    }

    @Test
    void shouldSetAndGetName() {

        TestUser user = new TestUser();

        user.setName("Prajwal");

        assertEquals("Prajwal", user.getName());
    }

    @Test
    void shouldSetAndGetEmail() {

        TestUser user = new TestUser();

        user.setEmail("prajwal@gmail.com");

        assertEquals("prajwal@gmail.com", user.getEmail());
    }

    @Test
    void shouldSetAndGetPhone() {

        TestUser user = new TestUser();

        user.setPhone("9876543210");

        assertEquals("9876543210", user.getPhone());
    }

    @Test
    void shouldSetAndGetPassword() {

        TestUser user = new TestUser();

        user.setPassword("password123");

        assertEquals("password123", user.getPassword());
    }

    @Test
    void shouldSetAndGetRole() {

        TestUser user = new TestUser();

        user.setRole(Role.CUSTOMER);

        assertEquals(Role.CUSTOMER, user.getRole());
    }

    @Test
    void shouldInitiallyHaveNullValues() {

        TestUser user = new TestUser();

        assertNull(user.getId());
        assertNull(user.getName());
        assertNull(user.getEmail());
        assertNull(user.getPhone());
        assertNull(user.getPassword());
        assertNull(user.getRole());
    }
}
