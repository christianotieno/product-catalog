package com.example.backend.service;

import com.example.backend.dto.ProductRequest;
import com.example.backend.dto.ProductResponse;
import com.example.backend.entity.Product;
import com.example.backend.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service for handling product management operations.
 * 
 * Provides comprehensive CRUD operations for products with business logic,
 * validation, and data transformation capabilities.
 * 
 * @author Christian Otieno
 * @version 1.0.0
 */
@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;

    /**
     * Create a new product.
     * 
     * @param productRequest the product creation request
     * @return the created product response
     * @throws RuntimeException if product creation fails
     */
    public ProductResponse createProduct(ProductRequest productRequest) {
        logger.debug("Creating new product: {}", productRequest.getName());
        
        try {
            Product product = new Product();
            product.setName(productRequest.getName());
            product.setDescription(productRequest.getDescription());
            product.setPrice(productRequest.getPrice());
            product.setCategory(productRequest.getCategory());
            product.setStockQuantity(productRequest.getStockQuantity());

            Product savedProduct = productRepository.save(product);
            logger.info("Product created successfully: {} (ID: {})", product.getName(), savedProduct.getId());
            
            return new ProductResponse(savedProduct);
        } catch (Exception e) {
            logger.error("Error creating product: {}", e.getMessage());
            throw new RuntimeException("Failed to create product", e);
        }
    }

    /**
     * Get all products with pagination.
     * 
     * @param pageable pagination information
     * @return page of product responses
     */
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        logger.debug("Retrieving all products with pagination");
        return productRepository.findAll(pageable)
                .map(ProductResponse::new);
    }

    /**
     * Get all products without pagination.
     * 
     * @return list of all product responses
     */
    public List<ProductResponse> getAllProducts() {
        logger.debug("Retrieving all products");
        return productRepository.findAll()
                .stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * Get product by ID.
     * 
     * @param id the product ID
     * @return optional containing the product response if found
     */
    public Optional<ProductResponse> getProductById(Long id) {
        logger.debug("Retrieving product by ID: {}", id);
        return productRepository.findById(id)
                .map(ProductResponse::new);
    }

    /**
     * Update an existing product.
     * 
     * @param id the product ID
     * @param productRequest the product update request
     * @return the updated product response
     * @throws RuntimeException if product not found or update fails
     */
    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
        logger.debug("Updating product: {}", id);
        
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        try {
            product.setName(productRequest.getName());
            product.setDescription(productRequest.getDescription());
            product.setPrice(productRequest.getPrice());
            product.setCategory(productRequest.getCategory());
            product.setStockQuantity(productRequest.getStockQuantity());

            Product updatedProduct = productRepository.save(product);
            logger.info("Product updated successfully: {} (ID: {})", product.getName(), id);
            
            return new ProductResponse(updatedProduct);
        } catch (Exception e) {
            logger.error("Error updating product: {}", e.getMessage());
            throw new RuntimeException("Failed to update product", e);
        }
    }

    /**
     * Delete a product.
     * 
     * @param id the product ID
     * @throws RuntimeException if product not found
     */
    public void deleteProduct(Long id) {
        logger.debug("Deleting product: {}", id);
        
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found");
        }

        productRepository.deleteById(id);
        logger.info("Product deleted: {}", id);
    }

    /**
     * Get products by category.
     * 
     * @param category the category to filter by
     * @return list of products in the specified category
     */
    public List<ProductResponse> getProductsByCategory(String category) {
        logger.debug("Retrieving products by category: {}", category);
        return productRepository.findByCategory(category)
                .stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * Get products by category with pagination.
     * 
     * @param category the category to filter by
     * @param pageable pagination information
     * @return page of products in the specified category
     */
    public Page<ProductResponse> getProductsByCategory(String category, Pageable pageable) {
        logger.debug("Retrieving products by category with pagination: {}", category);
        return productRepository.findByCategory(category, pageable)
                .map(ProductResponse::new);
    }

    /**
     * Search products by name.
     * 
     * @param name the name to search for
     * @return list of products matching the search criteria
     */
    public List<ProductResponse> searchProductsByName(String name) {
        logger.debug("Searching products by name: {}", name);
        return productRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * Search products by description.
     * 
     * @param description the description to search for
     * @return list of products matching the search criteria
     */
    public List<ProductResponse> searchProductsByDescription(String description) {
        logger.debug("Searching products by description: {}", description);
        return productRepository.findByDescriptionContainingIgnoreCase(description)
                .stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * Get products by price range.
     * 
     * @param minPrice minimum price (inclusive)
     * @param maxPrice maximum price (inclusive)
     * @return list of products within the price range
     */
    public List<ProductResponse> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        logger.debug("Retrieving products by price range: {} - {}", minPrice, maxPrice);
        return productRepository.findByPriceBetween(minPrice, maxPrice)
                .stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * Get products with low stock (less than 10 items).
     * 
     * @return list of products with low stock
     */
    public List<ProductResponse> getLowStockProducts() {
        logger.debug("Retrieving products with low stock");
        return productRepository.findLowStockProducts()
                .stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * Get products in stock (stock quantity > 0).
     * 
     * @return list of products in stock
     */
    public List<ProductResponse> getProductsInStock() {
        logger.debug("Retrieving products in stock");
        return productRepository.findByStockQuantityGreaterThan(0)
                .stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }

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
    public List<ProductResponse> searchProducts(String name, String category, 
                                              BigDecimal minPrice, BigDecimal maxPrice, 
                                              Boolean inStock) {
        logger.debug("Searching products with criteria - name: {}, category: {}, price: {}-{}, inStock: {}", 
                    name, category, minPrice, maxPrice, inStock);
        
        return productRepository.searchProducts(name, category, minPrice, maxPrice, inStock)
                .stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * Get all unique categories.
     * 
     * @return list of unique category names
     */
    public List<String> getAllCategories() {
        logger.debug("Retrieving all categories");
        return productRepository.findAllCategories();
    }

    /**
     * Get products ordered by price (ascending).
     * 
     * @return list of products ordered by price (lowest first)
     */
    public List<ProductResponse> getProductsOrderByPriceAsc() {
        logger.debug("Retrieving products ordered by price (ascending)");
        return productRepository.findAllOrderByPriceAsc()
                .stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * Get products ordered by price (descending).
     * 
     * @return list of products ordered by price (highest first)
     */
    public List<ProductResponse> getProductsOrderByPriceDesc() {
        logger.debug("Retrieving products ordered by price (descending)");
        return productRepository.findAllOrderByPriceDesc()
                .stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * Get products ordered by creation date (newest first).
     * 
     * @return list of products ordered by creation date
     */
    public List<ProductResponse> getProductsOrderByCreatedAtDesc() {
        logger.debug("Retrieving products ordered by creation date");
        return productRepository.findAllOrderByCreatedAtDesc()
                .stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * Update product stock quantity.
     * 
     * @param id the product ID
     * @param quantity the quantity to add/subtract
     * @return the updated product response
     * @throws RuntimeException if product not found or insufficient stock
     */
    public ProductResponse updateStock(Long id, Integer quantity) {
        logger.debug("Updating stock for product: {}, quantity: {}", id, quantity);
        
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        try {
            if (quantity > 0) {
                product.increaseStock(quantity);
            } else if (quantity < 0) {
                product.decreaseStock(Math.abs(quantity));
            }

            Product updatedProduct = productRepository.save(product);
            logger.info("Stock updated for product: {} (ID: {}), new quantity: {}", 
                       product.getName(), id, updatedProduct.getStockQuantity());
            
            return new ProductResponse(updatedProduct);
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid stock update for product {}: {}", id, e.getMessage());
            throw new RuntimeException(e.getMessage());
        } catch (Exception e) {
            logger.error("Error updating stock for product {}: {}", id, e.getMessage());
            throw new RuntimeException("Failed to update stock", e);
        }
    }

    /**
     * Count products by category.
     * 
     * @param category the category to count
     * @return the number of products in the specified category
     */
    public long countProductsByCategory(String category) {
        logger.debug("Counting products by category: {}", category);
        return productRepository.countByCategory(category);
    }
} 