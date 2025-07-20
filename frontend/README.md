# Frontend - React Application

A modern, responsive React application built with TypeScript and Material-UI for the fullstack developer assessment.

## 🚀 Features

### Core Functionality

- **Single Page Application (SPA)** with React Router
- **User Authentication** with JWT tokens
- **Role-based Access Control** (ADMIN, USER roles)
- **Product Catalog Management** with CRUD operations
- **Advanced Search & Filtering** capabilities
- **Responsive Design** for all device sizes
- **Real-time Data Fetching** with loading states

### Technical Features

- **React 18** with functional components and hooks
- **TypeScript** for type safety and better development experience
- **Material-UI (MUI)** for consistent, accessible UI components
- **React Router** for client-side routing
- **Context API** for state management
- **Axios** for HTTP client with interceptors
- **Formik & Yup** for form handling and validation
- **Jest & React Testing Library** for testing

## 🏗️ Architecture

### Project Structure

```
src/
├── components/           # Reusable UI components
│   ├── Auth/            # Authentication components
│   ├── Layout/          # Layout components
│   ├── Products/        # Product-related components
│   └── UI/              # Generic UI components
├── contexts/            # React contexts for state management
│   ├── AuthContext.tsx  # Authentication state
│   └── ProductContext.tsx # Product state
├── pages/               # Page components
│   ├── Dashboard.tsx    # Dashboard page
│   ├── Login.tsx        # Login page
│   ├── Products.tsx     # Products listing
│   ├── ProductDetail.tsx # Product details
│   ├── ProductForm.tsx  # Product form
│   ├── Register.tsx     # Registration page
│   └── Users.tsx        # User management
├── services/            # API services
│   ├── api.ts           # Axios configuration
│   ├── authService.ts   # Authentication API
│   └── productService.ts # Product API
├── types/               # TypeScript type definitions
│   └── index.ts         # All type definitions
├── utils/               # Utility functions
├── App.tsx              # Main application component
└── index.tsx            # Application entry point
```

## 🔧 Configuration

### Environment Variables

Create a `.env` file in the frontend directory:

```bash
# API Configuration
REACT_APP_API_BASE_URL=http://localhost:8080/api
REACT_APP_API_TIMEOUT=10000

# Application Configuration
REACT_APP_NAME=Product Catalog
REACT_APP_VERSION=1.0.0
```

### Package.json Scripts

```bash
npm start          # Start development server
npm run build      # Build for production
npm test           # Run tests
npm run lint       # Run ESLint
npm run lint:fix   # Fix ESLint issues
npm run format     # Format code with Prettier
npm run type-check # Run TypeScript type checking
```

## 🚀 Quick Start

### Prerequisites

- Node.js 18 or higher
- npm or yarn

### Installation & Running

```bash
# Navigate to frontend directory
cd frontend

# Install dependencies
npm install

# Start development server
npm start
```

The application will be available at `http://localhost:3000`

## 📱 User Interface

### Authentication Pages

- **Login Page**: User authentication with email/password
- **Register Page**: New user registration
- **Protected Routes**: Automatic redirection for unauthenticated users

### Dashboard

- **Overview Statistics**: Total products, users, low stock items
- **Quick Actions**: Navigation to key features
- **Recent Activity**: Latest product updates

### Product Management

- **Product Listing**: Paginated table with search and filters
- **Product Details**: Comprehensive product information
- **Product Form**: Create/edit products with validation
- **Advanced Search**: Multi-criteria search functionality

### User Management (Admin Only)

- **User Listing**: All users with role information
- **Role Management**: Update user roles
- **User Statistics**: User count by role

## 🔐 Authentication & Authorization

### User Roles

- **USER**: Can view products, search, and filter
- **ADMIN**: Full access to all features including user management

### Default Users

- **Admin**: `admin@example.com` / `admin123`
- **User**: `user@example.com` / `user123`

### JWT Token Management

- Automatic token storage in localStorage
- Token inclusion in API requests
- Automatic logout on token expiration
- Redirect to login on authentication errors

## 🎨 UI Components

### Material-UI Integration

- **Theme Provider**: Custom theme with brand colors
- **Responsive Design**: Mobile-first approach
- **Accessibility**: WCAG compliant components
- **Dark Mode Ready**: Theme configuration for future dark mode

### Key Components

- **Layout**: Responsive navigation with sidebar
- **DataGrid**: Advanced table with sorting and filtering
- **Forms**: Validated forms with error handling
- **Cards**: Product display and information cards
- **Dialogs**: Confirmation and form dialogs
- **Snackbars**: Success and error notifications

## 🔄 State Management

### Context API

- **AuthContext**: User authentication state
- **ProductContext**: Product data and operations

### State Structure

```typescript
// Auth State
interface AuthState {
  user: User | null;
  token: string | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  error: string | null;
}

// Product State
interface ProductState {
  products: Product[];
  paginatedProducts: PaginatedResponse<Product> | null;
  selectedProduct: Product | null;
  categories: string[];
  isLoading: boolean;
  error: string | null;
  searchParams: ProductSearchParams;
}
```

## 🌐 API Integration

### Service Layer

- **Axios Configuration**: Base URL, timeouts, interceptors
- **Authentication**: Automatic token inclusion
- **Error Handling**: Centralized error processing
- **Type Safety**: Full TypeScript integration

### API Endpoints

```typescript
// Authentication
POST /api/auth/login
POST /api/auth/register
GET  /api/auth/users (admin)

// Products
GET    /api/products
POST   /api/products (admin)
PUT    /api/products/:id (admin)
DELETE /api/products/:id (admin)
GET    /api/products/search
GET    /api/products/categories
```

## 🧪 Testing

### Testing Strategy

- **Unit Tests**: Component and utility function testing
- **Integration Tests**: API service testing
- **E2E Tests**: User workflow testing (future)

### Running Tests

```bash
# Run all tests
npm test

# Run tests with coverage
npm test -- --coverage

# Run specific test file
npm test -- ProductList.test.tsx
```

### Test Structure

```
src/
├── __tests__/           # Test files
│   ├── components/      # Component tests
│   ├── services/        # Service tests
│   └── utils/           # Utility tests
```

## 📦 Build & Deployment

### Development Build

```bash
npm run build
```

### Production Optimization

- **Code Splitting**: Automatic route-based splitting
- **Tree Shaking**: Unused code elimination
- **Minification**: Optimized bundle size
- **Source Maps**: Debugging support

### Deployment Options

- **Static Hosting**: Netlify, Vercel, GitHub Pages
- **Docker**: Containerized deployment
- **CDN**: Content delivery network integration

## 🔧 Development

### Code Quality

- **ESLint**: Code linting and style enforcement
- **Prettier**: Code formatting
- **TypeScript**: Static type checking
- **Husky**: Git hooks for quality checks

### Development Tools

- **React Developer Tools**: Component inspection
- **Redux DevTools**: State management debugging
- **Network Tab**: API request monitoring
- **Console Logging**: Debug information

### IDE Setup

Recommended VS Code extensions:

- **ES7+ React/Redux/React-Native snippets**
- **TypeScript Importer**
- **Prettier - Code formatter**
- **ESLint**
- **Material Icon Theme**

## 🚀 Performance

### Optimization Techniques

- **React.memo**: Component memoization
- **useMemo/useCallback**: Hook optimization
- **Lazy Loading**: Route-based code splitting
- **Image Optimization**: Responsive images
- **Bundle Analysis**: Webpack bundle analyzer

### Performance Monitoring

- **Lighthouse**: Performance auditing
- **Core Web Vitals**: User experience metrics
- **Bundle Size**: Regular size monitoring

## 🔒 Security

### Security Measures

- **Input Validation**: Client-side form validation
- **XSS Prevention**: React's built-in protection
- **CSRF Protection**: Token-based requests
- **Secure Headers**: Content Security Policy
- **HTTPS Only**: Secure communication

### Best Practices

- **Environment Variables**: Sensitive data protection
- **Token Management**: Secure token storage
- **Error Handling**: No sensitive data exposure
- **Input Sanitization**: XSS prevention

## 🤝 Contributing

This is a demonstration project for a senior engineering assessment. The code follows industry best practices:

- **Clean Code**: Readable and maintainable code
- **SOLID Principles**: Object-oriented design
- **DRY Principle**: Don't repeat yourself
- **KISS Principle**: Keep it simple, stupid
- **Comprehensive Testing**: High test coverage
- **Type Safety**: Full TypeScript integration
- **Performance Optimization**: Efficient rendering
- **Scalable Architecture**: Component-based design

## 📝 License

This project is created for demonstration purposes as part of a senior engineering assessment.
