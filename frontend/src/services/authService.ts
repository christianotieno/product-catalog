import api from './api';
import { AuthRequest, AuthResponse, User, UserUpdateRequest } from '../types';

class AuthService {
  // Login user
  async login(credentials: AuthRequest): Promise<AuthResponse> {
    const response = await api.post<AuthResponse>('/auth/login', credentials);
    return response.data;
  }

  // Register user
  async register(credentials: AuthRequest): Promise<AuthResponse> {
    const response = await api.post<AuthResponse>('/auth/register', credentials);
    return response.data;
  }

  // Get all users (admin only)
  async getUsers(): Promise<User[]> {
    const response = await api.get<User[]>('/auth/users');
    return response.data;
  }

  // Get user by ID (admin only)
  async getUserById(id: number): Promise<User> {
    const response = await api.get<User>(`/auth/users/${id}`);
    return response.data;
  }

  // Get user by email (admin only)
  async getUserByEmail(email: string): Promise<User> {
    const response = await api.get<User>(`/auth/users/email/${email}`);
    return response.data;
  }

  // Update user role (admin only)
  async updateUserRole(id: number, role: 'USER' | 'ADMIN'): Promise<User> {
    const response = await api.put<User>(`/auth/users/${id}/role?role=${role}`);
    return response.data;
  }

  // Delete user (admin only)
  async deleteUser(id: number): Promise<void> {
    await api.delete(`/auth/users/${id}`);
  }

  // Get users by role (admin only)
  async getUsersByRole(role: 'USER' | 'ADMIN'): Promise<User[]> {
    const response = await api.get<User[]>(`/auth/users/role/${role}`);
    return response.data;
  }

  // Count users by role (admin only)
  async countUsersByRole(role: 'USER' | 'ADMIN'): Promise<number> {
    const response = await api.get<number>(`/auth/users/count/${role}`);
    return response.data;
  }
}

export const authService = new AuthService(); 