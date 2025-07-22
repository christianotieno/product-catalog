import React, { createContext, useContext, useReducer, useEffect, ReactNode } from 'react';
import { User, AuthRequest, AuthResponse } from '../types';
import { authService } from '../services/authService';

interface AuthState {
  user: User | null;
  token: string | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  error: string | null;
}

type AuthAction =
  | { type: 'AUTH_START' }
  | { type: 'AUTH_SUCCESS'; payload: { user: User; token: string } }
  | { type: 'AUTH_FAILURE'; payload: string }
  | { type: 'AUTH_LOGOUT' }
  | { type: 'CLEAR_ERROR' };

const initialState: AuthState = {
  user: null,
  token: localStorage.getItem('token'),
  isAuthenticated: false,
  isLoading: false,
  error: null,
};

const authReducer = (state: AuthState, action: AuthAction): AuthState => {
  switch (action.type) {
    case 'AUTH_START':
      return {
        ...state,
        isLoading: true,
        error: null,
      };
    case 'AUTH_SUCCESS':
      return {
        ...state,
        user: action.payload.user,
        token: action.payload.token,
        isAuthenticated: true,
        isLoading: false,
        error: null,
      };
    case 'AUTH_FAILURE':
      return {
        ...state,
        user: null,
        token: null,
        isAuthenticated: false,
        isLoading: false,
        error: action.payload,
      };
    case 'AUTH_LOGOUT':
      return {
        ...state,
        user: null,
        token: null,
        isAuthenticated: false,
        isLoading: false,
        error: null,
      };
    case 'CLEAR_ERROR':
      return {
        ...state,
        error: null,
      };
    default:
      return state;
  }
};

interface AuthContextType extends AuthState {
  login: (credentials: AuthRequest) => Promise<void>;
  register: (credentials: AuthRequest) => Promise<void>;
  logout: () => void;
  clearError: () => void;
  isAdmin: () => boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

interface AuthProviderProps {
  children: ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [state, dispatch] = useReducer(authReducer, initialState);

  const isAdmin = (): boolean => {
    return state.user?.role === 'ADMIN';
  };

  const login = async (credentials: AuthRequest): Promise<void> => {
    try {
      dispatch({ type: 'AUTH_START' });
      
      const response: AuthResponse = await authService.login(credentials);
      
      localStorage.setItem('token', response.token);
      
      const user: User = {
        id: response.userId,
        email: response.email,
        role: response.role,
        createdAt: new Date().toISOString(),
      };
      
      dispatch({ 
        type: 'AUTH_SUCCESS', 
        payload: { user, token: response.token } 
      });
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : 'Login failed';
      dispatch({ type: 'AUTH_FAILURE', payload: errorMessage });
      throw error;
    }
  };

  const register = async (credentials: AuthRequest): Promise<void> => {
    try {
      dispatch({ type: 'AUTH_START' });
      
      const response: AuthResponse = await authService.register(credentials);
      
      localStorage.setItem('token', response.token);
      
      const user: User = {
        id: response.userId,
        email: response.email,
        role: response.role,
        createdAt: new Date().toISOString(),
      };
      
      dispatch({ 
        type: 'AUTH_SUCCESS', 
        payload: { user, token: response.token } 
      });
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : 'Registration failed';
      dispatch({ type: 'AUTH_FAILURE', payload: errorMessage });
      throw error;
    }
  };

  const logout = (): void => {
    localStorage.removeItem('token');
    dispatch({ type: 'AUTH_LOGOUT' });
  };

  const clearError = (): void => {
    dispatch({ type: 'CLEAR_ERROR' });
  };

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token && !state.isAuthenticated) {
      const validateStoredToken = async () => {
        try {
          dispatch({ type: 'AUTH_START' });
          const response: AuthResponse = await authService.validateToken();
          
          const user: User = {
            id: response.userId!,
            email: response.email!,
            role: response.role!,
            createdAt: new Date().toISOString(),
          };
          
          dispatch({ 
            type: 'AUTH_SUCCESS', 
            payload: { user, token: response.token! } 
          });
        } catch (error) {
          localStorage.removeItem('token');
          dispatch({ type: 'AUTH_LOGOUT' });
        }
      };
      
      validateStoredToken();
    }
  }, [state.isAuthenticated]);

  const value: AuthContextType = {
    ...state,
    login,
    register,
    logout,
    clearError,
    isAdmin,
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = (): AuthContextType => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
}; 