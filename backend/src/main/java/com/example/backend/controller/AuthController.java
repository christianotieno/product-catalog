package com.example.backend.controller;

import com.example.backend.dto.AuthRequest;
import com.example.backend.dto.AuthResponse;
import com.example.backend.entity.User;
import com.example.backend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for authentication and user management operations.
 * 
 * Provides endpoints for user login, registration, and user management
 * with proper validation, error handling, and API documentation.
 * 
 * @author Christian Otieno
 * @version 1.0.0
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication and user management endpoints")
@CrossOrigin(origins = "*")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    /**
     * Authenticate user and generate JWT token.
     * 
     * @param authRequest the authentication request
     * @return authentication response with JWT token
     */
    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and return JWT token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "401", description = "Invalid credentials"),
        @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<AuthResponse> login(
            @Parameter(description = "Login credentials", required = true)
            @Valid @RequestBody AuthRequest authRequest) {
        
        logger.info("Login attempt for user: {}", authRequest.getEmail());
        
        try {
            AuthResponse response = authService.authenticate(authRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.warn("Login failed for user {}: {}", authRequest.getEmail(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse("Invalid email or password"));
        }
    }

    /**
     * Register a new user.
     * 
     * @param authRequest the registration request
     * @return authentication response with JWT token
     */
    @PostMapping("/register")
    @Operation(summary = "User registration", description = "Register a new user and return JWT token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Registration successful",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request data or user already exists")
    })
    public ResponseEntity<AuthResponse> register(
            @Parameter(description = "Registration data", required = true)
            @Valid @RequestBody AuthRequest authRequest) {
        
        logger.info("Registration attempt for user: {}", authRequest.getEmail());
        
        try {
            AuthResponse response = authService.register(authRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.warn("Registration failed for user {}: {}", authRequest.getEmail(), e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new AuthResponse(e.getMessage()));
        }
    }

    /**
     * Get all users (admin only).
     * 
     * @return list of all users
     */
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all users", description = "Retrieve all users (admin only)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<List<User>> getAllUsers() {
        logger.debug("Retrieving all users");
        List<User> users = authService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Get user by ID (admin only).
     * 
     * @param userId the user ID
     * @return the user if found
     */
    @GetMapping("/users/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get user by ID", description = "Retrieve user by ID (admin only)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<User> getUserById(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long userId) {
        
        logger.debug("Retrieving user by ID: {}", userId);
        
        Optional<User> user = authService.getUserById(userId);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get user by email (admin only).
     * 
     * @param email the user email
     * @return the user if found
     */
    @GetMapping("/users/email/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get user by email", description = "Retrieve user by email (admin only)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<User> getUserByEmail(
            @Parameter(description = "User email", required = true)
            @PathVariable String email) {
        
        logger.debug("Retrieving user by email: {}", email);
        
        Optional<User> user = authService.getUserByEmail(email);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Update user role (admin only).
     * 
     * @param userId the user ID
     * @param role the new role
     * @return the updated user
     */
    @PutMapping("/users/{userId}/role")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update user role", description = "Update user role (admin only)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User role updated successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<User> updateUserRole(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long userId,
            @Parameter(description = "New role", required = true)
            @RequestParam User.Role role) {
        
        logger.info("Updating user role - ID: {}, Role: {}", userId, role);
        
        try {
            User updatedUser = authService.updateUserRole(userId, role);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            logger.warn("Failed to update user role: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete user (admin only).
     * 
     * @param userId the user ID
     * @return no content response
     */
    @DeleteMapping("/users/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete user", description = "Delete user (admin only)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long userId) {
        
        logger.info("Deleting user: {}", userId);
        
        try {
            authService.deleteUser(userId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.warn("Failed to delete user: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get users by role (admin only).
     * 
     * @param role the role to filter by
     * @return list of users with the specified role
     */
    @GetMapping("/users/role/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get users by role", description = "Retrieve users by role (admin only)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<List<User>> getUsersByRole(
            @Parameter(description = "User role", required = true)
            @PathVariable User.Role role) {
        
        logger.debug("Retrieving users by role: {}", role);
        List<User> users = authService.getUsersByRole(role);
        return ResponseEntity.ok(users);
    }

    /**
     * Get user count by role (admin only).
     * 
     * @param role the role to count
     * @return the number of users with the specified role
     */
    @GetMapping("/users/count/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Count users by role", description = "Count users by role (admin only)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Count retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<Long> countUsersByRole(
            @Parameter(description = "User role", required = true)
            @PathVariable User.Role role) {
        
        logger.debug("Counting users by role: {}", role);
        long count = authService.countUsersByRole(role);
        return ResponseEntity.ok(count);
    }
} 