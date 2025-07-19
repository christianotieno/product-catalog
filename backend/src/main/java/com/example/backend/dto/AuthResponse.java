package com.example.backend.dto;

import com.example.backend.entity.User;

/**
 * Data Transfer Object for authentication responses.
 * 
 * Contains JWT token and user information for successful authentication.
 * 
 * @author Christian Otieno
 * @version 1.0.0
 */
public class AuthResponse {

    private String token;
    private String tokenType = "Bearer";
    private Long userId;
    private String email;
    private User.Role role;
    private String message;

    public AuthResponse() {}

    public AuthResponse(String token, User user) {
        this.token = token;
        this.userId = user.getId();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.message = "Authentication successful";
    }

    public AuthResponse(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User.Role getRole() {
        return role;
    }

    public void setRole(User.Role role) {
        this.role = role;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "AuthResponse{" +
                "token='" + (token != null ? "***" : null) + '\'' +
                ", tokenType='" + tokenType + '\'' +
                ", userId=" + userId +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", message='" + message + '\'' +
                '}';
    }
} 