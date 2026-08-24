package com.crimsonlogic.ecommerce.interceptor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticationInterceptorTest {

    private AuthenticationInterceptor authenticationInterceptor;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {

        authenticationInterceptor =
                new AuthenticationInterceptor();

        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }


    // ==========================================================
    // NO SESSION
    // ==========================================================

    @Test
    void shouldRejectRequestWhenSessionDoesNotExist()
            throws Exception {

        request.setRequestURI(
                "/api/v1/customer/cart"
        );

        boolean result =
                authenticationInterceptor.preHandle(
                        request,
                        response,
                        new Object()
                );

        assertFalse(result);

        assertEquals(
                401,
                response.getStatus()
        );
    }


    // ==========================================================
    // SESSION EXISTS BUT USER ID IS MISSING
    // ==========================================================

    @Test
    void shouldRejectRequestWhenUserIdIsMissing()
            throws Exception {

        MockHttpSession session =
                new MockHttpSession();

        session.setAttribute(
                "role",
                "CUSTOMER"
        );

        request.setSession(session);

        request.setRequestURI(
                "/api/v1/customer/cart"
        );

        boolean result =
                authenticationInterceptor.preHandle(
                        request,
                        response,
                        new Object()
                );

        assertFalse(result);

        assertEquals(
                401,
                response.getStatus()
        );
    }


    // ==========================================================
    // SESSION EXISTS BUT ROLE IS MISSING
    // ==========================================================

    @Test
    void shouldRejectRequestWhenRoleIsMissing()
            throws Exception {

        MockHttpSession session =
                new MockHttpSession();

        session.setAttribute(
                "userId",
                "CUS001"
        );

        request.setSession(session);

        request.setRequestURI(
                "/api/v1/customer/cart"
        );

        boolean result =
                authenticationInterceptor.preHandle(
                        request,
                        response,
                        new Object()
                );

        assertFalse(result);

        assertEquals(
                401,
                response.getStatus()
        );
    }


    // ==========================================================
    // ADMIN
    // ==========================================================

    @Test
    void shouldAllowAdminToAccessAdminEndpoint()
            throws Exception {

        createSession(
                "ADM001",
                "ADMIN"
        );

        request.setRequestURI(
                "/api/v1/admin/stats"
        );

        boolean result =
                authenticationInterceptor.preHandle(
                        request,
                        response,
                        new Object()
                );

        assertTrue(result);

        assertEquals(
                "ADM001",
                request.getAttribute("userId")
        );

        assertEquals(
                "ADMIN",
                request.getAttribute("role")
        );
    }


    @Test
    void shouldRejectCustomerFromAdminEndpoint()
            throws Exception {

        createSession(
                "CUS001",
                "CUSTOMER"
        );

        request.setRequestURI(
                "/api/v1/admin/stats"
        );

        boolean result =
                authenticationInterceptor.preHandle(
                        request,
                        response,
                        new Object()
                );

        assertFalse(result);

        assertEquals(
                403,
                response.getStatus()
        );
    }


    @Test
    void shouldRejectSellerFromAdminEndpoint()
            throws Exception {

        createSession(
                "SEL001",
                "SELLER"
        );

        request.setRequestURI(
                "/api/v1/admin/stats"
        );

        boolean result =
                authenticationInterceptor.preHandle(
                        request,
                        response,
                        new Object()
                );

        assertFalse(result);

        assertEquals(
                403,
                response.getStatus()
        );
    }


    // ==========================================================
    // SELLER
    // ==========================================================

    @Test
    void shouldAllowSellerToAccessSellerEndpoint()
            throws Exception {

        createSession(
                "SEL001",
                "SELLER"
        );

        request.setRequestURI(
                "/api/v1/seller/products"
        );

        boolean result =
                authenticationInterceptor.preHandle(
                        request,
                        response,
                        new Object()
                );

        assertTrue(result);

        assertEquals(
                "SEL001",
                request.getAttribute("userId")
        );

        assertEquals(
                "SELLER",
                request.getAttribute("role")
        );
    }


    @Test
    void shouldRejectCustomerFromSellerEndpoint()
            throws Exception {

        createSession(
                "CUS001",
                "CUSTOMER"
        );

        request.setRequestURI(
                "/api/v1/seller/products"
        );

        boolean result =
                authenticationInterceptor.preHandle(
                        request,
                        response,
                        new Object()
                );

        assertFalse(result);

        assertEquals(
                403,
                response.getStatus()
        );
    }


    @Test
    void shouldRejectAdminFromSellerEndpoint()
            throws Exception {

        createSession(
                "ADM001",
                "ADMIN"
        );

        request.setRequestURI(
                "/api/v1/seller/products"
        );

        boolean result =
                authenticationInterceptor.preHandle(
                        request,
                        response,
                        new Object()
                );

        assertFalse(result);

        assertEquals(
                403,
                response.getStatus()
        );
    }


    // ==========================================================
    // CUSTOMER
    // ==========================================================

    @Test
    void shouldAllowCustomerToAccessCustomerEndpoint()
            throws Exception {

        createSession(
                "CUS001",
                "CUSTOMER"
        );

        request.setRequestURI(
                "/api/v1/customer/cart"
        );

        boolean result =
                authenticationInterceptor.preHandle(
                        request,
                        response,
                        new Object()
                );

        assertTrue(result);

        assertEquals(
                "CUS001",
                request.getAttribute("userId")
        );

        assertEquals(
                "CUSTOMER",
                request.getAttribute("role")
        );
    }


    @Test
    void shouldRejectSellerFromCustomerEndpoint()
            throws Exception {

        createSession(
                "SEL001",
                "SELLER"
        );

        request.setRequestURI(
                "/api/v1/customer/cart"
        );

        boolean result =
                authenticationInterceptor.preHandle(
                        request,
                        response,
                        new Object()
                );

        assertFalse(result);

        assertEquals(
                403,
                response.getStatus()
        );
    }


    @Test
    void shouldRejectAdminFromCustomerEndpoint()
            throws Exception {

        createSession(
                "ADM001",
                "ADMIN"
        );

        request.setRequestURI(
                "/api/v1/customer/cart"
        );

        boolean result =
                authenticationInterceptor.preHandle(
                        request,
                        response,
                        new Object()
                );

        assertFalse(result);

        assertEquals(
                403,
                response.getStatus()
        );
    }


    // ==========================================================
    // REQUEST ATTRIBUTES
    // ==========================================================

    @Test
    void shouldSetUserIdAndRoleAsRequestAttributes()
            throws Exception {

        createSession(
                "CUS001",
                "CUSTOMER"
        );

        request.setRequestURI(
                "/api/v1/customer/profile"
        );

        boolean result =
                authenticationInterceptor.preHandle(
                        request,
                        response,
                        new Object()
                );

        assertTrue(result);

        assertEquals(
                "CUS001",
                request.getAttribute("userId")
        );

        assertEquals(
                "CUSTOMER",
                request.getAttribute("role")
        );
    }


    // ==========================================================
    // CASE INSENSITIVE ROLE
    // ==========================================================

    @Test
    void shouldAllowAdminRoleCaseInsensitively()
            throws Exception {

        createSession(
                "ADM001",
                "admin"
        );

        request.setRequestURI(
                "/api/v1/admin/stats"
        );

        boolean result =
                authenticationInterceptor.preHandle(
                        request,
                        response,
                        new Object()
                );

        assertTrue(result);

        assertEquals(
                "ADM001",
                request.getAttribute("userId")
        );

        assertEquals(
                "admin",
                request.getAttribute("role")
        );
    }


    // ==========================================================
    // PUBLIC ENDPOINT
    // ==========================================================

    @Test
    void shouldAllowAuthenticatedUserToAccessPublicEndpoint()
            throws Exception {

        createSession(
                "CUS001",
                "CUSTOMER"
        );

        request.setRequestURI(
                "/api/v1/products"
        );

        boolean result =
                authenticationInterceptor.preHandle(
                        request,
                        response,
                        new Object()
                );

        assertTrue(result);

        assertEquals(
                "CUS001",
                request.getAttribute("userId")
        );

        assertEquals(
                "CUSTOMER",
                request.getAttribute("role")
        );
    }


    // ==========================================================
    // HELPER METHOD
    // ==========================================================

    private void createSession(
            String userId,
            String role) {

        MockHttpSession session =
                new MockHttpSession();

        session.setAttribute(
                "userId",
                userId
        );

        session.setAttribute(
                "role",
                role
        );

        request.setSession(session);
    }
}