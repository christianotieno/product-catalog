import React, { createContext, useContext, useReducer, ReactNode } from 'react';
import { Product, ProductRequest, ProductSearchParams, PaginatedResponse } from '../types';
import { productService } from '../services/productService';

// Product state interface
interface ProductState {
  products: Product[];
  paginatedProducts: PaginatedResponse<Product> | null;
  selectedProduct: Product | null;
  categories: string[];
  isLoading: boolean;
  error: string | null;
  searchParams: ProductSearchParams;
}

// Product action types
type ProductAction =
  | { type: 'PRODUCTS_LOADING' }
  | { type: 'PRODUCTS_SUCCESS'; payload: Product[] }
  | { type: 'PRODUCTS_PAGINATED_SUCCESS'; payload: PaginatedResponse<Product> }
  | { type: 'PRODUCT_SUCCESS'; payload: Product }
  | { type: 'PRODUCT_CREATED'; payload: Product }
  | { type: 'PRODUCT_UPDATED'; payload: Product }
  | { type: 'PRODUCT_DELETED'; payload: number }
  | { type: 'CATEGORIES_SUCCESS'; payload: string[] }
  | { type: 'PRODUCTS_FAILURE'; payload: string }
  | { type: 'SET_SEARCH_PARAMS'; payload: ProductSearchParams }
  | { type: 'CLEAR_ERROR' };

// Initial state
const initialState: ProductState = {
  products: [],
  paginatedProducts: null,
  selectedProduct: null,
  categories: [],
  isLoading: false,
  error: null,
  searchParams: {
    page: 0,
    size: 10,
    sortBy: 'id',
    sortDir: 'asc',
  },
};

// Product reducer
const productReducer = (state: ProductState, action: ProductAction): ProductState => {
  switch (action.type) {
    case 'PRODUCTS_LOADING':
      return {
        ...state,
        isLoading: true,
        error: null,
      };
    case 'PRODUCTS_SUCCESS':
      return {
        ...state,
        products: action.payload,
        isLoading: false,
        error: null,
      };
    case 'PRODUCTS_PAGINATED_SUCCESS':
      return {
        ...state,
        paginatedProducts: action.payload,
        isLoading: false,
        error: null,
      };
    case 'PRODUCT_SUCCESS':
      return {
        ...state,
        selectedProduct: action.payload,
        isLoading: false,
        error: null,
      };
    case 'PRODUCT_CREATED':
      return {
        ...state,
        products: [...state.products, action.payload],
        isLoading: false,
        error: null,
      };
    case 'PRODUCT_UPDATED':
      return {
        ...state,
        products: state.products.map(product =>
          product.id === action.payload.id ? action.payload : product
        ),
        selectedProduct: action.payload,
        isLoading: false,
        error: null,
      };
    case 'PRODUCT_DELETED':
      return {
        ...state,
        products: state.products.filter(product => product.id !== action.payload),
        selectedProduct: null,
        isLoading: false,
        error: null,
      };
    case 'CATEGORIES_SUCCESS':
      return {
        ...state,
        categories: action.payload,
        isLoading: false,
        error: null,
      };
    case 'PRODUCTS_FAILURE':
      return {
        ...state,
        isLoading: false,
        error: action.payload,
      };
    case 'SET_SEARCH_PARAMS':
      return {
        ...state,
        searchParams: { ...state.searchParams, ...action.payload },
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

// Product context interface
interface ProductContextType extends ProductState {
  // Product operations
  fetchProducts: (params?: ProductSearchParams) => Promise<void>;
  fetchProduct: (id: number) => Promise<void>;
  createProduct: (product: ProductRequest) => Promise<void>;
  updateProduct: (id: number, product: ProductRequest) => Promise<void>;
  deleteProduct: (id: number) => Promise<void>;
  fetchCategories: () => Promise<void>;
  
  // Search and filter operations
  searchProducts: (params: ProductSearchParams) => Promise<void>;
  setSearchParams: (params: Partial<ProductSearchParams>) => void;
  
  // Utility functions
  clearError: () => void;
  clearSelectedProduct: () => void;
}

// Create context
const ProductContext = createContext<ProductContextType | undefined>(undefined);

// Product provider props
interface ProductProviderProps {
  children: ReactNode;
}

// Product provider component
export const ProductProvider: React.FC<ProductProviderProps> = ({ children }) => {
  const [state, dispatch] = useReducer(productReducer, initialState);

  // Fetch all products
  const fetchProducts = async (params?: ProductSearchParams): Promise<void> => {
    try {
      dispatch({ type: 'PRODUCTS_LOADING' });
      
      if (params?.page !== undefined) {
        // Fetch paginated products
        const response = await productService.getProductsPaginated(params);
        dispatch({ type: 'PRODUCTS_PAGINATED_SUCCESS', payload: response });
      } else {
        // Fetch all products
        const products = await productService.getProducts();
        dispatch({ type: 'PRODUCTS_SUCCESS', payload: products });
      }
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : 'Failed to fetch products';
      dispatch({ type: 'PRODUCTS_FAILURE', payload: errorMessage });
      throw error;
    }
  };

  // Fetch single product
  const fetchProduct = async (id: number): Promise<void> => {
    try {
      dispatch({ type: 'PRODUCTS_LOADING' });
      const product = await productService.getProduct(id);
      dispatch({ type: 'PRODUCT_SUCCESS', payload: product });
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : 'Failed to fetch product';
      dispatch({ type: 'PRODUCTS_FAILURE', payload: errorMessage });
      throw error;
    }
  };

  // Create product
  const createProduct = async (product: ProductRequest): Promise<void> => {
    try {
      dispatch({ type: 'PRODUCTS_LOADING' });
      const createdProduct = await productService.createProduct(product);
      dispatch({ type: 'PRODUCT_CREATED', payload: createdProduct });
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : 'Failed to create product';
      dispatch({ type: 'PRODUCTS_FAILURE', payload: errorMessage });
      throw error;
    }
  };

  // Update product
  const updateProduct = async (id: number, product: ProductRequest): Promise<void> => {
    try {
      dispatch({ type: 'PRODUCTS_LOADING' });
      const updatedProduct = await productService.updateProduct(id, product);
      dispatch({ type: 'PRODUCT_UPDATED', payload: updatedProduct });
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : 'Failed to update product';
      dispatch({ type: 'PRODUCTS_FAILURE', payload: errorMessage });
      throw error;
    }
  };

  // Delete product
  const deleteProduct = async (id: number): Promise<void> => {
    try {
      dispatch({ type: 'PRODUCTS_LOADING' });
      await productService.deleteProduct(id);
      dispatch({ type: 'PRODUCT_DELETED', payload: id });
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : 'Failed to delete product';
      dispatch({ type: 'PRODUCTS_FAILURE', payload: errorMessage });
      throw error;
    }
  };

  // Fetch categories
  const fetchCategories = async (): Promise<void> => {
    try {
      dispatch({ type: 'PRODUCTS_LOADING' });
      const categories = await productService.getCategories();
      dispatch({ type: 'CATEGORIES_SUCCESS', payload: categories });
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : 'Failed to fetch categories';
      dispatch({ type: 'PRODUCTS_FAILURE', payload: errorMessage });
      throw error;
    }
  };

  // Search products
  const searchProducts = async (params: ProductSearchParams): Promise<void> => {
    try {
      dispatch({ type: 'PRODUCTS_LOADING' });
      const products = await productService.searchProducts(params);
      dispatch({ type: 'PRODUCTS_SUCCESS', payload: products });
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : 'Failed to search products';
      dispatch({ type: 'PRODUCTS_FAILURE', payload: errorMessage });
      throw error;
    }
  };

  // Set search parameters
  const setSearchParams = (params: Partial<ProductSearchParams>): void => {
    dispatch({ type: 'SET_SEARCH_PARAMS', payload: params });
  };

  // Clear error
  const clearError = (): void => {
    dispatch({ type: 'CLEAR_ERROR' });
  };

  // Clear selected product
  const clearSelectedProduct = (): void => {
    dispatch({ type: 'PRODUCT_SUCCESS', payload: null as any });
  };

  const value: ProductContextType = {
    ...state,
    fetchProducts,
    fetchProduct,
    createProduct,
    updateProduct,
    deleteProduct,
    fetchCategories,
    searchProducts,
    setSearchParams,
    clearError,
    clearSelectedProduct,
  };

  return (
    <ProductContext.Provider value={value}>
      {children}
    </ProductContext.Provider>
  );
};

// Custom hook to use product context
export const useProduct = (): ProductContextType => {
  const context = useContext(ProductContext);
  if (context === undefined) {
    throw new Error('useProduct must be used within a ProductProvider');
  }
  return context;
}; 