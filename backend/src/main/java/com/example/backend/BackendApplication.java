package com.example.backend;

import com.example.backend.entity.Product;
import com.example.backend.entity.User;
import com.example.backend.repository.ProductRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Main Spring Boot application class for the Product Catalog Backend.
 * 
 * This application provides a RESTful API for managing products and users
 * with JWT-based authentication and role-based authorization.
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

    @Bean
    public CommandLineRunner initializeData(UserRepository userRepository, 
                                          ProductRepository productRepository,
                                          PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByEmail("admin@example.com").isEmpty()) {
                User admin = new User();
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("password"));
                admin.setRole(User.Role.ADMIN);
                admin.setCreatedAt(LocalDateTime.now());
                userRepository.save(admin);
                System.out.println("Admin user created: admin@example.com");
            }

            if (userRepository.findByEmail("user@example.com").isEmpty()) {
                User user = new User();
                user.setEmail("user@example.com");
                user.setPassword(passwordEncoder.encode("password"));
                user.setRole(User.Role.USER);
                user.setCreatedAt(LocalDateTime.now());
                userRepository.save(user);
                System.out.println("User created: user@example.com");
            }

            if (productRepository.count() == 0) {
                createSampleProducts(productRepository);
                System.out.println("Sample products created");
            }
        };
    }

    private void createSampleProducts(ProductRepository productRepository) {
        Product[] products = {
            new Product("MacBook Pro 16\"", "Apple MacBook Pro with M2 Pro chip, 16GB RAM, 512GB SSD", 
                       new BigDecimal("2499.99"), "Electronics", 15),
            new Product("iPhone 15 Pro", "Apple iPhone 15 Pro with A17 Pro chip, 128GB storage", 
                       new BigDecimal("999.99"), "Electronics", 25),
            new Product("Sony WH-1000XM5", "Wireless noise-canceling headphones with 30-hour battery life", 
                       new BigDecimal("349.99"), "Electronics", 30),
            new Product("Nike Air Max 270", "Comfortable running shoes with Air Max technology", 
                       new BigDecimal("129.99"), "Sports", 50),
            new Product("Adidas Ultraboost 22", "Premium running shoes with Boost midsole technology", 
                       new BigDecimal("179.99"), "Sports", 35),
            new Product("Samsung 65\" QLED TV", "4K QLED Smart TV with Quantum Dot technology", 
                       new BigDecimal("1299.99"), "Electronics", 10),
            new Product("Dell XPS 13", "13-inch laptop with Intel i7 processor, 16GB RAM", 
                       new BigDecimal("1199.99"), "Electronics", 20),
            new Product("Canon EOS R6", "Full-frame mirrorless camera with 20MP sensor", 
                       new BigDecimal("2499.99"), "Electronics", 8),
            new Product("Yoga Mat Premium", "Non-slip yoga mat with alignment lines", 
                       new BigDecimal("49.99"), "Sports", 100),
            new Product("Wireless Charger", "Fast wireless charging pad compatible with all devices", 
                       new BigDecimal("29.99"), "Electronics", 75)
        };

        for (Product product : products) {
            product.setCreatedAt(LocalDateTime.now());
            product.setUpdatedAt(LocalDateTime.now());
            productRepository.save(product);
        }
    }
} 