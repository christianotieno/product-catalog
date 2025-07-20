package com.example.backend.controller;

import com.example.backend.dto.ProductRequest;
import com.example.backend.dto.ProductResponse;
import com.example.backend.service.ProductService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * REST controller for product management operations.
 * 
 * Provides comprehensive CRUD operations for products with search,
 * filtering, pagination, and role-based access control.
 * 
 * @author Christian Otieno
 * @version 1.0.0
 */
@RestController
@RequestMapping("/products")
@Tag(name = "Products", description = "Product management endpoints")
@CrossOrigin(origins = "*")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    /**
     * Create a new product (admin only).
     * 
     * @param productRequest the product creation request
     * @return the created product
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create product", description = "Create a new product (admin only)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Product created successfully",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<ProductResponse> createProduct(
            @Parameter(description = "Product data", required = true)
            @Valid @RequestBody ProductRequest productRequest) {
        
        logger.info("Creating new product: {}", productRequest.getName());
        
        try {
            ProductResponse response = productService.createProduct(productRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("Failed to create product: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get all products with pagination.
     * 
     * @param page page number (0-based)
     * @param size page size
     * @param sortBy sort field
     * @param sortDir sort direction
     * @return page of products
     */
    @GetMapping
    @Operation(summary = "Get all products", description = "Retrieve all products with pagination and sorting")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field")
            @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction (asc/desc)")
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        logger.debug("Retrieving products - page: {}, size: {}, sort: {} {}", page, size, sortBy, sortDir);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<ProductResponse> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(products);
    }

    /**
     * Get all products without pagination.
     * 
     * @return list of all products
     */
    @GetMapping("/all")
    @Operation(summary = "Get all products (no pagination)", description = "Retrieve all products without pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        logger.debug("Retrieving all products without pagination");
        List<ProductResponse> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * Get product by ID.
     * 
     * @param id the product ID
     * @return the product if found
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Retrieve a product by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<ProductResponse> getProductById(
            @Parameter(description = "Product ID", required = true)
            @PathVariable Long id) {
        
        logger.debug("Retrieving product by ID: {}", id);
        
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Update an existing product (admin only).
     * 
     * @param id the product ID
     * @param productRequest the product update request
     * @return the updated product
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update product", description = "Update an existing product (admin only)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product updated successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<ProductResponse> updateProduct(
            @Parameter(description = "Product ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated product data", required = true)
            @Valid @RequestBody ProductRequest productRequest) {
        
        logger.info("Updating product: {}", id);
        
        try {
            ProductResponse response = productService.updateProduct(id, productRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to update product {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete a product (admin only).
     * 
     * @param id the product ID
     * @return no content response
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete product", description = "Delete a product (admin only)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "Product ID", required = true)
            @PathVariable Long id) {
        
        logger.info("Deleting product: {}", id);
        
        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Failed to delete product {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get products by category.
     * 
     * @param category the category to filter by
     * @return list of products in the specified category
     */
    @GetMapping("/category/{category}")
    @Operation(summary = "Get products by category", description = "Retrieve products by category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(
            @Parameter(description = "Product category", required = true)
            @PathVariable String category) {
        
        logger.debug("Retrieving products by category: {}", category);
        List<ProductResponse> products = productService.getProductsByCategory(category);
        return ResponseEntity.ok(products);
    }

    /**
     * Search products by name.
     * 
     * @param name the name to search for
     * @return list of products matching the search criteria
     */
    @GetMapping("/search/name")
    @Operation(summary = "Search products by name", description = "Search products by name (case-insensitive)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    public ResponseEntity<List<ProductResponse>> searchProductsByName(
            @Parameter(description = "Product name to search for", required = true)
            @RequestParam String name) {
        
        logger.debug("Searching products by name: {}", name);
        List<ProductResponse> products = productService.searchProductsByName(name);
        return ResponseEntity.ok(products);
    }

    /**
     * Search products by description.
     * 
     * @param description the description to search for
     * @return list of products matching the search criteria
     */
    @GetMapping("/search/description")
    @Operation(summary = "Search products by description", description = "Search products by description (case-insensitive)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    public ResponseEntity<List<ProductResponse>> searchProductsByDescription(
            @Parameter(description = "Product description to search for", required = true)
            @RequestParam String description) {
        
        logger.debug("Searching products by description: {}", description);
        List<ProductResponse> products = productService.searchProductsByDescription(description);
        return ResponseEntity.ok(products);
    }

    /**
     * Get products by price range.
     * 
     * @param minPrice minimum price (inclusive)
     * @param maxPrice maximum price (inclusive)
     * @return list of products within the price range
     */
    @GetMapping("/search/price")
    @Operation(summary = "Get products by price range", description = "Retrieve products within a price range")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    public ResponseEntity<List<ProductResponse>> getProductsByPriceRange(
            @Parameter(description = "Minimum price", required = true)
            @RequestParam BigDecimal minPrice,
            @Parameter(description = "Maximum price", required = true)
            @RequestParam BigDecimal maxPrice) {
        
        logger.debug("Retrieving products by price range: {} - {}", minPrice, maxPrice);
        List<ProductResponse> products = productService.getProductsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }

    /**
     * Get products with low stock.
     * 
     * @return list of products with low stock (less than 10 items)
     */
    @GetMapping("/low-stock")
    @Operation(summary = "Get low stock products", description = "Retrieve products with low stock (less than 10 items)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    public ResponseEntity<List<ProductResponse>> getLowStockProducts() {
        logger.debug("Retrieving products with low stock");
        List<ProductResponse> products = productService.getLowStockProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * Get products in stock.
     * 
     * @return list of products in stock (stock quantity > 0)
     */
    @GetMapping("/in-stock")
    @Operation(summary = "Get products in stock", description = "Retrieve products that are in stock")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    public ResponseEntity<List<ProductResponse>> getProductsInStock() {
        logger.debug("Retrieving products in stock");
        List<ProductResponse> products = productService.getProductsInStock();
        return ResponseEntity.ok(products);
    }

    /**
     * Advanced search with multiple criteria.
     * 
     * @param name product name (optional)
     * @param category product category (optional)
     * @param minPrice minimum price (optional)
     * @param maxPrice maximum price (optional)
     * @param inStock whether to include only products in stock (optional)
     * @return list of products matching the criteria
     */
    @GetMapping("/search")
    @Operation(summary = "Advanced product search", description = "Search products with multiple criteria")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    public ResponseEntity<List<ProductResponse>> searchProducts(
            @Parameter(description = "Product name (optional)")
            @RequestParam(required = false) String name,
            @Parameter(description = "Product category (optional)")
            @RequestParam(required = false) String category,
            @Parameter(description = "Minimum price (optional)")
            @RequestParam(required = false) BigDecimal minPrice,
            @Parameter(description = "Maximum price (optional)")
            @RequestParam(required = false) BigDecimal maxPrice,
            @Parameter(description = "In stock only (optional)")
            @RequestParam(required = false) Boolean inStock) {
        
        logger.debug("Advanced search - name: {}, category: {}, price: {}-{}, inStock: {}", 
                    name, category, minPrice, maxPrice, inStock);
        
        List<ProductResponse> products = productService.searchProducts(name, category, minPrice, maxPrice, inStock);
        return ResponseEntity.ok(products);
    }

    /**
     * Get all unique categories.
     * 
     * @return list of unique category names
     */
    @GetMapping("/categories")
    @Operation(summary = "Get all categories", description = "Retrieve all unique product categories")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categories retrieved successfully")
    })
    public ResponseEntity<List<String>> getAllCategories() {
        logger.debug("Retrieving all categories");
        List<String> categories = productService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    /**
     * Get products ordered by price (ascending).
     * 
     * @return list of products ordered by price (lowest first)
     */
    @GetMapping("/sort/price-asc")
    @Operation(summary = "Get products by price (ascending)", description = "Retrieve products ordered by price (lowest first)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    public ResponseEntity<List<ProductResponse>> getProductsOrderByPriceAsc() {
        logger.debug("Retrieving products ordered by price (ascending)");
        List<ProductResponse> products = productService.getProductsOrderByPriceAsc();
        return ResponseEntity.ok(products);
    }

    /**
     * Get products ordered by price (descending).
     * 
     * @return list of products ordered by price (highest first)
     */
    @GetMapping("/sort/price-desc")
    @Operation(summary = "Get products by price (descending)", description = "Retrieve products ordered by price (highest first)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    public ResponseEntity<List<ProductResponse>> getProductsOrderByPriceDesc() {
        logger.debug("Retrieving products ordered by price (descending)");
        List<ProductResponse> products = productService.getProductsOrderByPriceDesc();
        return ResponseEntity.ok(products);
    }

    /**
     * Get products ordered by creation date (newest first).
     * 
     * @return list of products ordered by creation date
     */
    @GetMapping("/sort/newest")
    @Operation(summary = "Get newest products", description = "Retrieve products ordered by creation date (newest first)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    public ResponseEntity<List<ProductResponse>> getProductsOrderByCreatedAtDesc() {
        logger.debug("Retrieving products ordered by creation date");
        List<ProductResponse> products = productService.getProductsOrderByCreatedAtDesc();
        return ResponseEntity.ok(products);
    }

    /**
     * Update product stock quantity (admin only).
     * 
     * @param id the product ID
     * @param quantity the quantity to add/subtract
     * @return the updated product
     */
    @PutMapping("/{id}/stock")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update product stock", description = "Update product stock quantity (admin only)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Stock updated successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "400", description = "Invalid quantity"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<ProductResponse> updateStock(
            @Parameter(description = "Product ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Quantity to add/subtract", required = true)
            @RequestParam Integer quantity) {
        
        logger.info("Updating stock for product: {}, quantity: {}", id, quantity);
        
        try {
            ProductResponse response = productService.updateStock(id, quantity);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to update stock for product {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Count products by category.
     * 
     * @param category the category to count
     * @return the number of products in the specified category
     */
    @GetMapping("/count/{category}")
    @Operation(summary = "Count products by category", description = "Count products in a specific category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Count retrieved successfully")
    })
    public ResponseEntity<Long> countProductsByCategory(
            @Parameter(description = "Product category", required = true)
            @PathVariable String category) {
        
        logger.debug("Counting products by category: {}", category);
        long count = productService.countProductsByCategory(category);
        return ResponseEntity.ok(count);
    }
} 