import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import Products from './Products';
import { ProductProvider } from '../contexts/ProductContext';
import { AuthProvider } from '../contexts/AuthContext';

jest.mock('../services/productService', () => ({
  productService: {
    getProducts: jest.fn(),
    getProduct: jest.fn(),
    createProduct: jest.fn(),
    updateProduct: jest.fn(),
    deleteProduct: jest.fn(),
    getCategories: jest.fn(),
    searchProducts: jest.fn(),
  },
}));

jest.mock('axios', () => ({
  create: jest.fn(() => ({
    get: jest.fn(),
    post: jest.fn(),
    put: jest.fn(),
    delete: jest.fn(),
    interceptors: {
      request: { use: jest.fn() },
      response: { use: jest.fn() },
    },
  })),
  default: {
    get: jest.fn(),
    post: jest.fn(),
    put: jest.fn(),
    delete: jest.fn(),
  },
}));

const theme = createTheme();

const renderWithProviders = (component: React.ReactElement) => {
  return render(
    <BrowserRouter>
      <ThemeProvider theme={theme}>
        <AuthProvider>
          <ProductProvider>
            {component}
          </ProductProvider>
        </AuthProvider>
      </ThemeProvider>
    </BrowserRouter>
  );
};

describe('Products Component', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  test('renders products page title', () => {
    renderWithProviders(<Products />);
    expect(screen.getByText('Products')).toBeInTheDocument();
  });

  test('shows loading state initially', () => {
    renderWithProviders(<Products />);
    expect(screen.getByText('Loading products...')).toBeInTheDocument();
  });

  test('renders search input', () => {
    renderWithProviders(<Products />);
    expect(screen.getByLabelText('Search products')).toBeInTheDocument();
  });

  test('renders category filter', () => {
    renderWithProviders(<Products />);
    expect(screen.getByLabelText('Category')).toBeInTheDocument();
  });

  test('shows "No products found" when no products are available', async () => {
    renderWithProviders(<Products />);
    
    await waitFor(() => {
      expect(screen.getByText('No products found.')).toBeInTheDocument();
    });
  });
}); 