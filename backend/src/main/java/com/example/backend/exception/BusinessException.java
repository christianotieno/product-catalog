package com.example.backend.exception;

/**
 * Exception thrown when business logic validation fails.
 * 
 * Used for consistent error handling when business rules
 * or constraints are violated.
 * 
 * @author Christian Otieno
 * @version 1.0.0
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
} 