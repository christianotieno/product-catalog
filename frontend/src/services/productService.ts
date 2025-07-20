import api from './api';
import { Product, ProductRequest, ProductSearchParams, PaginatedResponse } from '../types';

class ProductService {
  // Get all products (paginated)
  async getProductsPaginated(params: ProductSearchParams): Promise<PaginatedResponse<Product>> {
    const queryParams = new URLSearchParams();
    
    if (params.page !== undefined) queryParams.append('page', params.page.toString());
    if (params.size !== undefined) queryParams.append('size', params.size.toString());
    if (params.sortBy) queryParams.append('sortBy', params.sortBy);
    if (params.sortDir) queryParams.append('sortDir', params.sortDir);
    
    const response = await api.get<PaginatedResponse<Product>>(`/products?${queryParams}`);
    return response.data;
  }

  // Get all products (no pagination)
  async getProducts(): Promise<Product[]> {
    const response = await api.get<Product[]>('/products/all');
    return response.data;
  }

  // Get product by ID
  async getProduct(id: number): Promise<Product> {
    const response = await api.get<Product>(`/products/${id}`);
    return response.data;
  }

  // Create product
  async createProduct(product: ProductRequest): Promise<Product> {
    const response = await api.post<Product>('/products', product);
    return response.data;
  }

  // Update product
  async updateProduct(id: number, product: ProductRequest): Promise<Product> {
    const response = await api.put<Product>(`/products/${id}`, product);
    return response.data;
  }

  // Delete product
  async deleteProduct(id: number): Promise<void> {
    await api.delete(`/products/${id}`);
  }

  // Get products by category
  async getProductsByCategory(category: string): Promise<Product[]> {
    const response = await api.get<Product[]>(`/products/category/${category}`);
    return response.data;
  }

  // Search products by name
  async searchProductsByName(name: string): Promise<Product[]> {
    const response = await api.get<Product[]>(`/products/search/name?name=${name}`);
    return response.data;
  }

  // Search products by description
  async searchProductsByDescription(description: string): Promise<Product[]> {
    const response = await api.get<Product[]>(`/products/search/description?description=${description}`);
    return response.data;
  }

  // Get products by price range
  async getProductsByPriceRange(minPrice: number, maxPrice: number): Promise<Product[]> {
    const response = await api.get<Product[]>(`/products/search/price?minPrice=${minPrice}&maxPrice=${maxPrice}`);
    return response.data;
  }

  // Get low stock products
  async getLowStockProducts(): Promise<Product[]> {
    const response = await api.get<Product[]>('/products/low-stock');
    return response.data;
  }

  // Get products in stock
  async getProductsInStock(): Promise<Product[]> {
    const response = await api.get<Product[]>('/products/in-stock');
    return response.data;
  }

  // Advanced search
  async searchProducts(params: ProductSearchParams): Promise<Product[]> {
    const queryParams = new URLSearchParams();
    
    if (params.name) queryParams.append('name', params.name);
    if (params.category) queryParams.append('category', params.category);
    if (params.minPrice !== undefined) queryParams.append('minPrice', params.minPrice.toString());
    if (params.maxPrice !== undefined) queryParams.append('maxPrice', params.maxPrice.toString());
    if (params.inStock !== undefined) queryParams.append('inStock', params.inStock.toString());
    
    const response = await api.get<Product[]>(`/products/search?${queryParams}`);
    return response.data;
  }

  // Get all categories
  async getCategories(): Promise<string[]> {
    const response = await api.get<string[]>('/products/categories');
    return response.data;
  }

  // Get products ordered by price (ascending)
  async getProductsOrderByPriceAsc(): Promise<Product[]> {
    const response = await api.get<Product[]>('/products/sort/price-asc');
    return response.data;
  }

  // Get products ordered by price (descending)
  async getProductsOrderByPriceDesc(): Promise<Product[]> {
    const response = await api.get<Product[]>('/products/sort/price-desc');
    return response.data;
  }

  // Get newest products
  async getNewestProducts(): Promise<Product[]> {
    const response = await api.get<Product[]>('/products/sort/newest');
    return response.data;
  }

  // Update product stock
  async updateStock(id: number, quantity: number): Promise<Product> {
    const response = await api.put<Product>(`/products/${id}/stock?quantity=${quantity}`);
    return response.data;
  }

  // Count products by category
  async countProductsByCategory(category: string): Promise<number> {
    const response = await api.get<number>(`/products/count/${category}`);
    return response.data;
  }
}

export const productService = new ProductService(); 