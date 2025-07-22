export interface User {
  id: number;
  email: string;
  role: 'USER' | 'ADMIN';
  createdAt: string;
}

export interface Product {
  id: number;
  name: string;
  description?: string;
  price: number;
  category?: string;
  stockQuantity: number;
  createdAt: string;
  updatedAt: string;
  inStock: boolean;
  lowStock: boolean;
  formattedPrice: string;
}

export interface AuthRequest {
  email: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  tokenType: string;
  userId: number;
  email: string;
  role: 'USER' | 'ADMIN';
  message: string;
}

export interface ProductRequest {
  name: string;
  description?: string;
  price: number;
  category?: string;
  stockQuantity: number;
}

export interface ApiResponse<T> {
  data: T;
  message?: string;
  success: boolean;
}

export interface PaginatedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}

export interface ProductSearchParams {
  name?: string;
  category?: string;
  minPrice?: number;
  maxPrice?: number;
  inStock?: boolean;
  page?: number;
  size?: number;
  sortBy?: string;
  sortDir?: 'asc' | 'desc';
}

export interface LoginFormData {
  email: string;
  password: string;
}

export interface RegisterFormData {
  email: string;
  password: string;
  confirmPassword: string;
}

export interface ProductFormData {
  name: string;
  description: string;
  price: number;
  category: string;
  stockQuantity: number;
}

export interface ApiError {
  status: number;
  error: string;
  message: string;
  path: string;
  timestamp: string;
}

export interface DashboardStats {
  totalProducts: number;
  totalUsers: number;
  lowStockProducts: number;
  categories: string[];
}

export interface UserUpdateRequest {
  role: 'USER' | 'ADMIN';
} 