package com.example.backend.repository;

import com.example.backend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repository interface for Product entity operations.
 * 
 * Provides data access methods for product catalog management including
 * search, filtering, and pagination capabilities.
 * 
 * @author Christian Otieno
 * @version 1.0.0
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Find products by category.
     * 
     * @param category the category to search for
     * @return list of products in the specified category
     */
    List<Product> findByCategory(String category);

    /**
     * Find products by category with pagination.
     * 
     * @param category the category to search for
     * @param pageable pagination information
     * @return page of products in the specified category
     */
    Page<Product> findByCategory(String category, Pageable pageable);

    /**
     * Find products with stock quantity greater than the specified value.
     * 
     * @param stockQuantity the minimum stock quantity
     * @return list of products with sufficient stock
     */
    List<Product> findByStockQuantityGreaterThan(Integer stockQuantity);

    /**
     * Find products with low stock (less than 10 items).
     * 
     * @return list of products with low stock
     */
    @Query("SELECT p FROM Product p WHERE p.stockQuantity < 10")
    List<Product> findLowStockProducts();

    /**
     * Find products by price range.
     * 
     * @param minPrice minimum price (inclusive)
     * @param maxPrice maximum price (inclusive)
     * @return list of products within the price range
     */
    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice")
    List<Product> findByPriceBetween(@Param("minPrice") BigDecimal minPrice, 
                                   @Param("maxPrice") BigDecimal maxPrice);

    /**
     * Find products by name containing the specified text (case-insensitive).
     * 
     * @param name the name text to search for
     * @return list of products whose name contains the specified text
     */
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Product> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find products by description containing the specified text (case-insensitive).
     * 
     * @param description the description text to search for
     * @return list of products whose description contains the specified text
     */
    @Query("SELECT p FROM Product p WHERE LOWER(p.description) LIKE LOWER(CONCAT('%', :description, '%'))")
    List<Product> findByDescriptionContainingIgnoreCase(@Param("description") String description);

    /**
     * Search products by multiple criteria.
     * 
     * @param name product name (optional)
     * @param category product category (optional)
     * @param minPrice minimum price (optional)
     * @param maxPrice maximum price (optional)
     * @param inStock whether to include only products in stock (optional)
     * @return list of products matching the criteria
     */
    @Query("SELECT p FROM Product p WHERE " +
           "(:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:category IS NULL OR p.category = :category) AND " +
           "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
           "(:inStock IS NULL OR (:inStock = true AND p.stockQuantity > 0) OR (:inStock = false))")
    List<Product> searchProducts(@Param("name") String name,
                               @Param("category") String category,
                               @Param("minPrice") BigDecimal minPrice,
                               @Param("maxPrice") BigDecimal maxPrice,
                               @Param("inStock") Boolean inStock);

    /**
     * Find all unique categories.
     * 
     * @return list of unique category names
     */
    @Query("SELECT DISTINCT p.category FROM Product p WHERE p.category IS NOT NULL")
    List<String> findAllCategories();

    /**
     * Count products by category.
     * 
     * @param category the category to count
     * @return the number of products in the specified category
     */
    long countByCategory(String category);

    /**
     * Find products ordered by price in ascending order.
     * 
     * @return list of products ordered by price (lowest first)
     */
    @Query("SELECT p FROM Product p ORDER BY p.price ASC")
    List<Product> findAllOrderByPriceAsc();

    /**
     * Find products ordered by price in descending order.
     * 
     * @return list of products ordered by price (highest first)
     */
    @Query("SELECT p FROM Product p ORDER BY p.price DESC")
    List<Product> findAllOrderByPriceDesc();

    /**
     * Find products ordered by creation date (newest first).
     * 
     * @return list of products ordered by creation date
     */
    @Query("SELECT p FROM Product p ORDER BY p.createdAt DESC")
    List<Product> findAllOrderByCreatedAtDesc();
} 