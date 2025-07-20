package com.example.backend.exception;

/**
 * Exception thrown when a requested resource is not found.
 * 
 * Used for consistent error handling when entities or resources
 * cannot be located in the system.
 * 
 * @author Christian Otieno
 * @version 1.0.0
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
} 