package com.crimsonlogic.ecommerce.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.crimsonlogic.ecommerce.exception.DuplicateUserException;
import com.crimsonlogic.ecommerce.exception.InvalidCredentialsException;
import com.crimsonlogic.ecommerce.exception.ValidationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ApiResponse<Map<String, String>> response = ApiResponse.error("Validation failed", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler({ValidationException.class, DuplicateUserException.class})
    public ResponseEntity<ApiResponse<Object>> handleBusinessValidation(RuntimeException exception) {
        ApiResponse<Object> response = ApiResponse.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>> handleCredentials(InvalidCredentialsException exception) {
        ApiResponse<Object> response = ApiResponse.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
    
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Object>>
    handleMethodNotAllowed(
            HttpRequestMethodNotSupportedException exception) {

        ApiResponse<Object> response =
                ApiResponse.error(
                        "Request method '" +
                        exception.getMethod() +
                        "' is not supported for this endpoint."
                );

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(response);
    }
    
 // ==========================================================
    // 415 - UNSUPPORTED MEDIA TYPE
    // ==========================================================

    @ExceptionHandler(
            HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiResponse<Object>>
    handleUnsupportedMediaType(
            HttpMediaTypeNotSupportedException exception) {

        ApiResponse<Object> response =
                ApiResponse.error(
                        "Unsupported media type. " +
                        "Please use Content-Type: application/json."
                );

        return ResponseEntity
                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(response);
    }
    
    
    
 // ==========================================================
 // 404 - ENDPOINT NOT FOUND
 // ==========================================================

 @ExceptionHandler(NoHandlerFoundException.class)
 public ResponseEntity<ApiResponse<Object>> handleNotFound(
         NoHandlerFoundException exception) {
	 
     ApiResponse<Object> response =
             ApiResponse.error(
                     "The requested endpoint was not found. Please check the URL."
             );

     return ResponseEntity
             .status(HttpStatus.NOT_FOUND)
             .body(response);
 }
 
 
 @ExceptionHandler(HttpMessageNotReadableException.class)
 public ResponseEntity<ApiResponse<Object>> handleInvalidRequestBody(
         HttpMessageNotReadableException exception) {

     return ResponseEntity
             .status(HttpStatus.BAD_REQUEST)
             .body(ApiResponse.error(
                     "Invalid request body. Please check your JSON format."
             ));
 }
}