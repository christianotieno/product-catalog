# Java & React

Fullstack app with Spring Boot backend and React frontend.

## 🏗️ Architecture Overview

```
code-challenge/
├── backend/                 # Spring Boot API
│   ├── src/
│   ├── pom.xml
│   └── README.md
├── frontend/               # React SPA
│   ├── src/
│   ├── package.json
│   └── README.md
└── README.md              # This file
```

## 🚀 Quick Start

### Prerequisites

- Java 17+
- Node.js 18+
- Maven 3.6+
- npm or yarn

### Backend Setup

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`

### Frontend Setup

```bash
cd frontend
npm install
npm start
```

The React app will be available at `http://localhost:3000`

## 📋 Features Implemented

### Backend (Spring Boot)

- ✅ RESTful API with CRUD operations
- ✅ Product catalog data model
- ✅ Data validation with Bean Validation
- ✅ Comprehensive error handling
- ✅ JWT-based authentication
- ✅ Role-based authorization
- ✅ Database integration (H2 for development)
- ✅ API documentation with Swagger
- ✅ Unit and integration tests
- ✅ Logging and monitoring

### Frontend (React)

- ✅ Single-page application (SPA)
- ✅ User authentication with JWT
- ✅ Protected routes
- ✅ Product catalog management
- ✅ Dynamic data fetching
- ✅ Form validation
- ✅ Responsive design
- ✅ Error handling and loading states
- ✅ Modern UI with Material-UI
- ✅ State management with React Context

## 🔐 Authentication

### Default Users

- **Admin**: `admin@example.com` / `password`
- **User**: `user@example.com` / `password`

## 🧪 Testing

### Backend Tests

```bash
cd backend
mvn test
```

### Frontend Tests

```bash
cd frontend
npm test
```

## 🛠️ Technical Decisions

### Backend Architecture

- **Spring Boot 3.x** with Java 17 for modern development
- **Spring Security** with JWT for authentication
- **Spring Data JPA** for data persistence
- **H2 Database** for development (easily switchable to PostgreSQL/MySQL)
- **Bean Validation** for input validation
- **Swagger/OpenAPI** for API documentation
- **JUnit 5** and **Testcontainers** for testing

### Frontend Architecture

- **React 18** with functional components and hooks
- **React Router** for client-side routing
- **Material-UI** for consistent, accessible UI components
- **Axios** for HTTP client
- **React Context** for state management
- **Formik** and **Yup** for form handling and validation
- **Jest** and **React Testing Library** for testing

## 🔧 Environment Configuration

### Backend Environment Variables

```bash
# Database
SPRING_DATASOURCE_URL=jdbc:h2:mem:testdb
SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.h2.Driver

# JWT
JWT_SECRET=your-secret-key-here
JWT_EXPIRATION=86400000

# Server
SERVER_PORT=8080
```

### Frontend Environment Variables

```bash
# API Configuration
REACT_APP_API_BASE_URL=http://localhost:8080/api
REACT_APP_API_TIMEOUT=10000
```

## 📊 Database Schema

### Products Table

```sql
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    category VARCHAR(100),
    stock_quantity INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### Users Table

```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## 🚀 Deployment

### Backend Deployment

The Spring Boot application can be deployed as a JAR file:

```bash
cd backend
mvn clean package
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

### Frontend Deployment

The React application can be built for production:

```bash
cd frontend
npm run build
```

## 🔍 Code Quality

- **Backend**: SonarQube integration, consistent code formatting
- **Frontend**: ESLint, Prettier, TypeScript for type safety
- **Git**: Conventional commits, meaningful commit messages
- **Documentation**: Comprehensive API documentation and inline comments

## 📈 Performance Considerations

- **Backend**: Connection pooling, caching with Redis (configurable)
- **Frontend**: Code splitting, lazy loading, optimized bundle size
- **Database**: Indexed queries, pagination for large datasets
- **Security**: Input sanitization, CORS configuration, rate limiting
