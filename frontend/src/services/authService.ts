import api from './api';
import { AuthRequest, AuthResponse, User, UserUpdateRequest } from '../types';

class AuthService {
  async login(credentials: AuthRequest): Promise<AuthResponse> {
    const response = await api.post<AuthResponse>('/auth/login', credentials);
    return response.data;
  }

  async register(credentials: AuthRequest): Promise<AuthResponse> {
    const response = await api.post<AuthResponse>('/auth/register', credentials);
    return response.data;
  }

  async validateToken(): Promise<AuthResponse> {
    const response = await api.post<AuthResponse>('/auth/validate');
    return response.data;
  }

  async getUsers(): Promise<User[]> {
    const response = await api.get<User[]>('/auth/users');
    return response.data;
  }

  async getUserById(id: number): Promise<User> {
    const response = await api.get<User>(`/auth/users/${id}`);
    return response.data;
  }

  async getUserByEmail(email: string): Promise<User> {
    const response = await api.get<User>(`/auth/users/email/${email}`);
    return response.data;
  }

  async updateUserRole(id: number, role: 'USER' | 'ADMIN'): Promise<User> {
    const response = await api.put<User>(`/auth/users/${id}/role?role=${role}`);
    return response.data;
  }

  async deleteUser(id: number): Promise<void> {
    await api.delete(`/auth/users/${id}`);
  }

  async getUsersByRole(role: 'USER' | 'ADMIN'): Promise<User[]> {
    const response = await api.get<User[]>(`/auth/users/role/${role}`);
    return response.data;
  }

  async countUsersByRole(role: 'USER' | 'ADMIN'): Promise<number> {
    const response = await api.get<number>(`/auth/users/count/${role}`);
    return response.data;
  }
}

export const authService = new AuthService(); 