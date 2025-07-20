package com.example.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Main Spring Boot application class for the backend API.
 * 
 * This application provides a RESTful API for product catalog management
 * with authentication, authorization, and comprehensive CRUD operations.
 * 
 * Features:
 * - JWT-based authentication
 * - Role-based authorization (ADMIN, USER)
 * - Product catalog CRUD operations
 * - Data validation and error handling
 * - API documentation with Swagger/OpenAPI
 * - H2 in-memory database for development
 * 
 * @author Christian Otieno
 * @version 1.0.0
 */
@SpringBootApplication
@EnableJpaAuditing
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
} 