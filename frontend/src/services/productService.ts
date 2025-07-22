import api from './api';
import { Product, ProductRequest, ProductSearchParams, PaginatedResponse } from '../types';

class ProductService {
  async getProductsPaginated(params: ProductSearchParams): Promise<PaginatedResponse<Product>> {
    const queryParams = new URLSearchParams();
    
    if (params.page !== undefined) queryParams.append('page', params.page.toString());
    if (params.size !== undefined) queryParams.append('size', params.size.toString());
    if (params.sortBy) queryParams.append('sortBy', params.sortBy);
    if (params.sortDir) queryParams.append('sortDir', params.sortDir);
    
    const response = await api.get<PaginatedResponse<Product>>(`/products?${queryParams}`);
    return response.data;
  }

  async getProducts(): Promise<Product[]> {
    const response = await api.get<Product[]>('/products/all');
    return response.data;
  }

  async getProduct(id: number): Promise<Product> {
    const response = await api.get<Product>(`/products/${id}`);
    return response.data;
  }

  async createProduct(product: ProductRequest): Promise<Product> {
    const response = await api.post<Product>('/products', product);
    return response.data;
  }

  async updateProduct(id: number, product: ProductRequest): Promise<Product> {
    const response = await api.put<Product>(`/products/${id}`, product);
    return response.data;
  }

  async deleteProduct(id: number): Promise<void> {
    await api.delete(`/products/${id}`);
  }

  async getProductsByCategory(category: string): Promise<Product[]> {
    const response = await api.get<Product[]>(`/products/category/${category}`);
    return response.data;
  }

  async searchProductsByName(name: string): Promise<Product[]> {
    const response = await api.get<Product[]>(`/products/search/name?name=${name}`);
    return response.data;
  }

  async searchProductsByDescription(description: string): Promise<Product[]> {
    const response = await api.get<Product[]>(`/products/search/description?description=${description}`);
    return response.data;
  }

  async getProductsByPriceRange(minPrice: number, maxPrice: number): Promise<Product[]> {
    const response = await api.get<Product[]>(`/products/search/price?minPrice=${minPrice}&maxPrice=${maxPrice}`);
    return response.data;
  }

  async getLowStockProducts(): Promise<Product[]> {
    const response = await api.get<Product[]>('/products/low-stock');
    return response.data;
  }

  async getProductsInStock(): Promise<Product[]> {
    const response = await api.get<Product[]>('/products/in-stock');
    return response.data;
  }

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

  async getCategories(): Promise<string[]> {
    const response = await api.get<string[]>('/products/categories');
    return response.data;
  }

  async getProductsOrderByPriceAsc(): Promise<Product[]> {
    const response = await api.get<Product[]>('/products/sort/price-asc');
    return response.data;
  }

  async getProductsOrderByPriceDesc(): Promise<Product[]> {
    const response = await api.get<Product[]>('/products/sort/price-desc');
    return response.data;
  }

  async getNewestProducts(): Promise<Product[]> {
    const response = await api.get<Product[]>('/products/sort/newest');
    return response.data;
  }

  async updateStock(id: number, quantity: number): Promise<Product> {
    const response = await api.put<Product>(`/products/${id}/stock?quantity=${quantity}`);
    return response.data;
  }
  
  async countProductsByCategory(category: string): Promise<number> {
    const response = await api.get<number>(`/products/count/${category}`);
    return response.data;
  }
}

export const productService = new ProductService(); 