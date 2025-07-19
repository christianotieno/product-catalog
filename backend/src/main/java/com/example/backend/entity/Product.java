package com.example.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Product entity representing items in the e-commerce catalog.
 * 
 * This entity supports full CRUD operations and includes comprehensive
 * validation, auditing, and business logic for product management.
 * 
 * @author Christian Otieno
 * @version 1.0.0
 */
@Entity
@Table(name = "products")
@EntityListeners(AuditingEntityListener.class)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 255, message = "Product name must be between 2 and 255 characters")
    @Column(nullable = false)
    private String name;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @DecimalMax(value = "999999.99", message = "Price cannot exceed 999,999.99")
    @Digits(integer = 6, fraction = 2, message = "Price must have up to 6 digits before decimal and 2 after")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Size(max = 100, message = "Category cannot exceed 100 characters")
    @Column(length = 100)
    private String category;

    @Min(value = 0, message = "Stock quantity cannot be negative")
    @Max(value = 999999, message = "Stock quantity cannot exceed 999,999")
    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity = 0;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Product() {}

    public Product(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Product(String name, String description, BigDecimal price, String category, Integer stockQuantity) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.stockQuantity = stockQuantity != null ? stockQuantity : 0;
    }

    /**
     * Checks if the product is in stock.
     * @return true if stock quantity is greater than 0
     */
    public boolean isInStock() {
        return stockQuantity > 0;
    }

    /**
     * Checks if the product has low stock (less than 10 items).
     * @return true if stock quantity is less than 10
     */
    public boolean isLowStock() {
        return stockQuantity < 10;
    }

    /**
     * Decreases stock quantity by the specified amount.
     * @param quantity the amount to decrease
     * @throws IllegalArgumentException if quantity is negative or exceeds available stock
     */
    public void decreaseStock(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (quantity > stockQuantity) {
            throw new IllegalArgumentException("Insufficient stock");
        }
        this.stockQuantity -= quantity;
    }

    /**
     * Increases stock quantity by the specified amount.
     * @param quantity the amount to increase
     * @throws IllegalArgumentException if quantity is negative
     */
    public void increaseStock(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        this.stockQuantity += quantity;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity != null ? stockQuantity : 0;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", stockQuantity=" + stockQuantity +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id != null && id.equals(product.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
} 