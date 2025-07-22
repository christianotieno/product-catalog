package com.example.backend.service;

import com.example.backend.dto.ProductRequest;
import com.example.backend.dto.ProductResponse;
import com.example.backend.entity.Product;
import com.example.backend.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ProductService.
 * 
 * Tests all major functionality including CRUD operations,
 * search, filtering, and business logic validation.
 * 
 * @author Christian Otieno
 * @version 1.0.0
 */
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;
    private ProductRequest testProductRequest;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setDescription("Test Description");
        testProduct.setPrice(new BigDecimal("99.99"));
        testProduct.setCategory("Electronics");
        testProduct.setStockQuantity(10);

        testProductRequest = new ProductRequest();
        testProductRequest.setName("Test Product");
        testProductRequest.setDescription("Test Description");
        testProductRequest.setPrice(new BigDecimal("99.99"));
        testProductRequest.setCategory("Electronics");
        testProductRequest.setStockQuantity(10);
    }

    @Test
    void createProduct_Success() {
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        ProductResponse result = productService.createProduct(testProductRequest);

        assertNotNull(result);
        assertEquals(testProduct.getName(), result.getName());
        assertEquals(testProduct.getPrice(), result.getPrice());
        assertEquals(testProduct.getCategory(), result.getCategory());
        assertEquals(testProduct.getStockQuantity(), result.getStockQuantity());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void createProduct_ThrowsException() {
        when(productRepository.save(any(Product.class))).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> productService.createProduct(testProductRequest));
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void getAllProducts_Success() {
        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Test Product 2");
        product2.setPrice(new BigDecimal("149.99"));
        product2.setCategory("Sports");
        product2.setStockQuantity(5);

        List<Product> products = Arrays.asList(testProduct, product2);
        when(productRepository.findAll()).thenReturn(products);

        List<ProductResponse> result = productService.getAllProducts();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testProduct.getName(), result.get(0).getName());
        assertEquals(product2.getName(), result.get(1).getName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void getProductById_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        Optional<ProductResponse> result = productService.getProductById(1L);

        assertTrue(result.isPresent());
        assertEquals(testProduct.getName(), result.get().getName());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void getProductById_NotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<ProductResponse> result = productService.getProductById(999L);

        assertFalse(result.isPresent());
        verify(productRepository, times(1)).findById(999L);
    }

    @Test
    void updateProduct_Success() {
        ProductRequest updateRequest = new ProductRequest();
        updateRequest.setName("Updated Product");
        updateRequest.setDescription("Updated Description");
        updateRequest.setPrice(new BigDecimal("199.99"));
        updateRequest.setCategory("Updated Category");
        updateRequest.setStockQuantity(20);

        Product updatedProduct = new Product();
        updatedProduct.setId(1L);
        updatedProduct.setName("Updated Product");
        updatedProduct.setDescription("Updated Description");
        updatedProduct.setPrice(new BigDecimal("199.99"));
        updatedProduct.setCategory("Updated Category");
        updatedProduct.setStockQuantity(20);

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        ProductResponse result = productService.updateProduct(1L, updateRequest);

        assertNotNull(result);
        assertEquals("Updated Product", result.getName());
        assertEquals(new BigDecimal("199.99"), result.getPrice());
        assertEquals("Updated Category", result.getCategory());
        assertEquals(20, result.getStockQuantity());
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void updateProduct_NotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productService.updateProduct(999L, testProductRequest));
        verify(productRepository, times(1)).findById(999L);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void deleteProduct_Success() {
        when(productRepository.existsById(1L)).thenReturn(true);

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).existsById(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteProduct_NotFound() {
        when(productRepository.existsById(999L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> productService.deleteProduct(999L));
        verify(productRepository, times(1)).existsById(999L);
        verify(productRepository, never()).deleteById(any());
    }

    @Test
    void getProductsByCategory_Success() {
        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Electronics Product 2");
        product2.setCategory("Electronics");
        product2.setPrice(new BigDecimal("299.99"));
        product2.setStockQuantity(15);

        List<Product> electronicsProducts = Arrays.asList(testProduct, product2);
        when(productRepository.findByCategory("Electronics")).thenReturn(electronicsProducts);

        List<ProductResponse> result = productService.getProductsByCategory("Electronics");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Electronics", result.get(0).getCategory());
        assertEquals("Electronics", result.get(1).getCategory());
        verify(productRepository, times(1)).findByCategory("Electronics");
    }

    @Test
    void searchProductsByName_Success() {
        List<Product> searchResults = Arrays.asList(testProduct);
        when(productRepository.findByNameContainingIgnoreCase("Test")).thenReturn(searchResults);

        List<ProductResponse> result = productService.searchProductsByName("Test");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Product", result.get(0).getName());
        verify(productRepository, times(1)).findByNameContainingIgnoreCase("Test");
    }

    @Test
    void getLowStockProducts_Success() {
        Product lowStockProduct = new Product();
        lowStockProduct.setId(3L);
        lowStockProduct.setName("Low Stock Product");
        lowStockProduct.setStockQuantity(5);

        List<Product> lowStockProducts = Arrays.asList(lowStockProduct);
        when(productRepository.findLowStockProducts()).thenReturn(lowStockProducts);

        List<ProductResponse> result = productService.getLowStockProducts();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).isLowStock());
        verify(productRepository, times(1)).findLowStockProducts();
    }

    @Test
    void getProductsInStock_Success() {
        Product inStockProduct = new Product();
        inStockProduct.setId(4L);
        inStockProduct.setName("In Stock Product");
        inStockProduct.setStockQuantity(25);

        List<Product> inStockProducts = Arrays.asList(inStockProduct);
        when(productRepository.findByStockQuantityGreaterThan(0)).thenReturn(inStockProducts);

        List<ProductResponse> result = productService.getProductsInStock();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).isInStock());
        verify(productRepository, times(1)).findByStockQuantityGreaterThan(0);
    }

    @Test
    void updateStock_IncreaseStock_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        ProductResponse result = productService.updateStock(1L, 5);

        assertNotNull(result);
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void updateStock_DecreaseStock_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        ProductResponse result = productService.updateStock(1L, -3);

        assertNotNull(result);
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void updateStock_ProductNotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productService.updateStock(999L, 5));
        verify(productRepository, times(1)).findById(999L);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void getAllCategories_Success() {
        List<String> categories = Arrays.asList("Electronics", "Sports", "Books");
        when(productRepository.findAllCategories()).thenReturn(categories);

        List<String> result = productService.getAllCategories();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains("Electronics"));
        assertTrue(result.contains("Sports"));
        assertTrue(result.contains("Books"));
        verify(productRepository, times(1)).findAllCategories();
    }

    @Test
    void countProductsByCategory_Success() {
        when(productRepository.countByCategory("Electronics")).thenReturn(5L);

        long result = productService.countProductsByCategory("Electronics");

        assertEquals(5L, result);
        verify(productRepository, times(1)).countByCategory("Electronics");
    }
} 