package com.crimsonlogic.ecommerce.handler;

import com.crimsonlogic.ecommerce.exception.DuplicateUserException;
import com.crimsonlogic.ecommerce.exception.InvalidCredentialsException;
import com.crimsonlogic.ecommerce.exception.ValidationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {

        exceptionHandler =
                new GlobalExceptionHandler();
    }


    // ==========================================================
    // VALIDATION EXCEPTION
    // ==========================================================

    @Test
    void shouldHandleValidationException() {

        ValidationException exception =
                new ValidationException(
                        "Customer not found."
                );

        ResponseEntity<ApiResponse<Object>> response =
                exceptionHandler.handleBusinessValidation(
                        exception
                );

        assertEquals(
                HttpStatus.BAD_REQUEST,
                response.getStatusCode()
        );

        assertNotNull(response.getBody());
    }


    // ==========================================================
    // DUPLICATE USER EXCEPTION
    // ==========================================================

    @Test
    void shouldHandleDuplicateUserException() {

        DuplicateUserException exception =
                new DuplicateUserException(
                        "Email is already registered."
                );

        ResponseEntity<ApiResponse<Object>> response =
                exceptionHandler.handleBusinessValidation(
                        exception
                );

        assertEquals(
                HttpStatus.BAD_REQUEST,
                response.getStatusCode()
        );

        assertNotNull(response.getBody());
    }


    // ==========================================================
    // INVALID CREDENTIALS
    // ==========================================================

    @Test
    void shouldHandleInvalidCredentialsException() {

        InvalidCredentialsException exception =
                new InvalidCredentialsException(
                        "Invalid Email or Password."
                );

        ResponseEntity<ApiResponse<Object>> response =
                exceptionHandler.handleCredentials(
                        exception
                );

        assertEquals(
                HttpStatus.UNAUTHORIZED,
                response.getStatusCode()
        );

        assertNotNull(response.getBody());
    }


    // ==========================================================
    // VALIDATION RESPONSE
    // ==========================================================

    @Test
    void shouldReturnBadRequestForValidationFailure() {

        ValidationException exception =
                new ValidationException(
                        "Product not found."
                );

        ResponseEntity<ApiResponse<Object>> response =
                exceptionHandler.handleBusinessValidation(
                        exception
                );

        assertEquals(
                400,
                response.getStatusCodeValue()
        );

        assertNotNull(response.getBody());
    }


    // ==========================================================
    // DUPLICATE EMAIL MESSAGE
    // ==========================================================

    @Test
    void shouldHandleDuplicateEmailWithBadRequest() {

        DuplicateUserException exception =
                new DuplicateUserException(
                        "Email is already registered."
                );

        ResponseEntity<ApiResponse<Object>> response =
                exceptionHandler.handleBusinessValidation(
                        exception
                );

        assertEquals(
                400,
                response.getStatusCodeValue()
        );

        assertNotNull(response.getBody());
    }


    // ==========================================================
    // INVALID LOGIN MESSAGE
    // ==========================================================

    @Test
    void shouldReturnUnauthorizedForInvalidLogin() {

        InvalidCredentialsException exception =
                new InvalidCredentialsException(
                        "Invalid Email or Password."
                );

        ResponseEntity<ApiResponse<Object>> response =
                exceptionHandler.handleCredentials(
                        exception
                );

        assertEquals(
                401,
                response.getStatusCodeValue()
        );

        assertNotNull(response.getBody());
    }
}