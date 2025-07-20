package com.example.backend.service;

import com.example.backend.dto.AuthRequest;
import com.example.backend.dto.AuthResponse;
import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service for handling authentication and user management operations.
 * 
 * Provides methods for user login, registration, and user management
 * with proper security practices and validation.
 * 
 * @author Christian Otieno
 * @version 1.0.0
 */
@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Authenticate user and generate JWT token.
     * 
     * @param authRequest the authentication request
     * @return authentication response with JWT token
     * @throws BadCredentialsException if authentication fails
     */
    public AuthResponse authenticate(AuthRequest authRequest) {
        logger.debug("Authenticating user: {}", authRequest.getEmail());
        
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    authRequest.getEmail(), 
                    authRequest.getPassword()
                )
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getEmail());
            String token = jwtUtil.generateToken(userDetails);

            User user = userRepository.findByEmail(authRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found after authentication"));

            logger.info("User '{}' authenticated successfully", authRequest.getEmail());
            return new AuthResponse(token, user);

        } catch (BadCredentialsException e) {
            logger.warn("Authentication failed for user: {}", authRequest.getEmail());
            throw new BadCredentialsException("Invalid email or password");
        } catch (Exception e) {
            logger.error("Error during authentication: {}", e.getMessage());
            throw new RuntimeException("Authentication failed", e);
        }
    }

    /**
     * Register a new user.
     * 
     * @param authRequest the registration request
     * @return authentication response with JWT token
     * @throws RuntimeException if user already exists or registration fails
     */
    public AuthResponse register(AuthRequest authRequest) {
        logger.debug("Registering new user: {}", authRequest.getEmail());
        
        if (userRepository.existsByEmail(authRequest.getEmail())) {
            logger.warn("User registration failed - email already exists: {}", authRequest.getEmail());
            throw new RuntimeException("User with this email already exists");
        }

        try {
            User user = new User();
            user.setEmail(authRequest.getEmail());
            user.setPassword(passwordEncoder.encode(authRequest.getPassword()));
            user.setRole(User.Role.USER);

            User savedUser = userRepository.save(user);

            String token = jwtUtil.generateToken(savedUser);

            logger.info("User '{}' registered successfully", authRequest.getEmail());
            return new AuthResponse(token, savedUser);

        } catch (Exception e) {
            logger.error("Error during user registration: {}", e.getMessage());
            throw new RuntimeException("Registration failed", e);
        }
    }

    /**
     * Get all users (admin only).
     * 
     * @return list of all users
     */
    public List<User> getAllUsers() {
        logger.debug("Retrieving all users");
        return userRepository.findAll();
    }

    /**
     * Get user by ID.
     * 
     * @param userId the user ID
     * @return optional containing the user if found
     */
    public Optional<User> getUserById(Long userId) {
        logger.debug("Retrieving user by ID: {}", userId);
        return userRepository.findById(userId);
    }

    /**
     * Get user by email.
     * 
     * @param email the user email
     * @return optional containing the user if found
     */
    public Optional<User> getUserByEmail(String email) {
        logger.debug("Retrieving user by email: {}", email);
        return userRepository.findByEmail(email);
    }

    /**
     * Update user role (admin only).
     * 
     * @param userId the user ID
     * @param role the new role
     * @return the updated user
     * @throws RuntimeException if user not found
     */
    public User updateUserRole(Long userId, User.Role role) {
        logger.debug("Updating user role - ID: {}, Role: {}", userId, role);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setRole(role);
        User updatedUser = userRepository.save(user);

        logger.info("User role updated - ID: {}, New Role: {}", userId, role);
        return updatedUser;
    }

    /**
     * Delete user (admin only).
     * 
     * @param userId the user ID
     * @throws RuntimeException if user not found
     */
    public void deleteUser(Long userId) {
        logger.debug("Deleting user: {}", userId);
        
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }

        userRepository.deleteById(userId);
        logger.info("User deleted: {}", userId);
    }

    /**
     * Get users by role.
     * 
     * @param role the role to filter by
     * @return list of users with the specified role
     */
    public List<User> getUsersByRole(User.Role role) {
        logger.debug("Retrieving users by role: {}", role);
        return userRepository.findByRole(role);
    }

    /**
     * Count users by role.
     * 
     * @param role the role to count
     * @return the number of users with the specified role
     */
    public long countUsersByRole(User.Role role) {
        logger.debug("Counting users by role: {}", role);
        return userRepository.countByRole(role);
    }
} 