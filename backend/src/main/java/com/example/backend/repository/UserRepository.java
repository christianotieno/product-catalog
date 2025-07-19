package com.example.backend.repository;

import com.example.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for User entity operations.
 * 
 * Provides data access methods for user management including
 * authentication and authorization operations.
 * 
 * @author Christian Otieno
 * @version 1.0.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find a user by email address.
     * 
     * @param email the email address to search for
     * @return Optional containing the user if found, empty otherwise
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if a user exists with the given email address.
     * 
     * @param email the email address to check
     * @return true if a user exists with the email, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Find users by role.
     * 
     * @param role the role to search for
     * @return list of users with the specified role
     */
    @Query("SELECT u FROM User u WHERE u.role = :role")
    java.util.List<User> findByRole(@Param("role") User.Role role);

    /**
     * Find users by email containing the specified text (case-insensitive).
     * 
     * @param email the email text to search for
     * @return list of users whose email contains the specified text
     */
    @Query("SELECT u FROM User u WHERE LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%'))")
    java.util.List<User> findByEmailContainingIgnoreCase(@Param("email") String email);

    /**
     * Count users by role.
     * 
     * @param role the role to count
     * @return the number of users with the specified role
     */
    long countByRole(User.Role role);
} 