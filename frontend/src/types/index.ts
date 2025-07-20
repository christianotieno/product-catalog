// User types
export interface User {
  id: number;
  email: string;
  role: 'USER' | 'ADMIN';
  createdAt: string;
}

// Product types
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

// Authentication types
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

// Product request types
export interface ProductRequest {
  name: string;
  description?: string;
  price: number;
  category?: string;
  stockQuantity: number;
}

// API Response types
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

// Search and filter types
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

// Form validation types
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

// Error types
export interface ApiError {
  status: number;
  error: string;
  message: string;
  path: string;
  timestamp: string;
}

// Dashboard types
export interface DashboardStats {
  totalProducts: number;
  totalUsers: number;
  lowStockProducts: number;
  categories: string[];
}

// User management types
export interface UserUpdateRequest {
  role: 'USER' | 'ADMIN';
} 