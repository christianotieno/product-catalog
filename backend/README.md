# Backend API - Spring Boot

A RESTful API built with Spring Boot 3.x and Java 17 for the product catalog.

## 🚀 Features

### Core Functionality

- **RESTful API** with CRUD operations
- **Product Catalog Management** with search and filtering
- **User Authentication & Authorization** with JWT tokens
- **Role-based Access Control** (ADMIN, USER roles)
- **Data Validation** with Bean Validation
- **Comprehensive Error Handling** with consistent error responses

### Technical Features

- **Spring Boot 3.2.0** with Java 17
- **Spring Security** with JWT authentication
- **Spring Data JPA** for data persistence
- **H2 Database** for development (easily switchable)
- **Swagger/OpenAPI** documentation
- **CORS** configuration for frontend integration
- **Comprehensive Logging** with SLF4J
- **Unit & Integration Tests** with JUnit 5 and Mockito

## 🏗️ Architecture

### Package Structure

```
src/main/java/com/example/backend/
├── BackendApplication.java          # Main application class
├── config/
│   └── SecurityConfig.java         # Security configuration
├── controller/
│   ├── AuthController.java         # Authentication endpoints
│   └── ProductController.java      # Product management endpoints
├── dto/
│   ├── AuthRequest.java            # Authentication request DTO
│   ├── AuthResponse.java           # Authentication response DTO
│   ├── ProductRequest.java         # Product request DTO
│   └── ProductResponse.java        # Product response DTO
├── entity/
│   ├── Product.java                # Product entity
│   └── User.java                   # User entity
├── exception/
│   ├── BusinessException.java      # Business logic exceptions
│   ├── GlobalExceptionHandler.java # Global exception handler
│   └── ResourceNotFoundException.java # Resource not found exceptions
├── repository/
│   ├── ProductRepository.java      # Product data access
│   └── UserRepository.java         # User data access
├── security/
│   ├── JwtAuthenticationFilter.java # JWT authentication filter
│   └── JwtUtil.java               # JWT utility class
└── service/
    ├── AuthService.java            # Authentication business logic
    ├── CustomUserDetailsService.java # User details service
    └── ProductService.java         # Product business logic
```

## 🔧 Configuration

### Application Properties

The application is configured via `application.yml` with the following key settings:

- **Server**: Port 8080, context path `/api`
- **Database**: H2 in-memory database with console enabled
- **JWT**: Configurable secret and expiration time
- **CORS**: Configured for frontend integration
- **Logging**: Debug level for development
- **Swagger**: API documentation at `/swagger-ui.html`

### Environment Variables

```bash
# Database
SPRING_DATASOURCE_URL=jdbc:h2:mem:testdb
SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.h2.Driver

# JWT
JWT_SECRET=secret-key-here
JWT_EXPIRATION=86400000

# Server
SERVER_PORT=8080
```

## 🚀 Quick Start

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Running the Application

```bash
# Clone and navigate to backend directory
cd backend

# Build the application
mvn clean install

# Run the application
mvn spring-boot:run
```

The API will be available at `http://localhost:8080/api`

### Database Console

H2 console is available at `http://localhost:8080/api/h2-console`

- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `password`

## 📚 API Documentation

### Swagger UI

Once the application is running, visit:

- **Swagger UI**: `http://localhost:8080/api/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/api/v3/api-docs`

### Authentication Endpoints

```
POST /api/auth/login          # User login
POST /api/auth/register       # User registration
GET  /api/auth/users          # Get all users (ADMIN)
GET  /api/auth/users/{id}     # Get user by ID (ADMIN)
PUT  /api/auth/users/{id}/role # Update user role (ADMIN)
DELETE /api/auth/users/{id}   # Delete user (ADMIN)
```

### Product Endpoints

```
GET    /api/products                    # Get all products (paginated)
GET    /api/products/all               # Get all products (no pagination)
GET    /api/products/{id}              # Get product by ID
POST   /api/products                   # Create product (ADMIN)
PUT    /api/products/{id}              # Update product (ADMIN)
DELETE /api/products/{id}              # Delete product (ADMIN)
GET    /api/products/category/{cat}    # Get products by category
GET    /api/products/search/name       # Search by name
GET    /api/products/search/price      # Search by price range
GET    /api/products/low-stock         # Get low stock products
GET    /api/products/in-stock          # Get products in stock
GET    /api/products/search            # Advanced search
GET    /api/products/categories        # Get all categories
GET    /api/products/sort/price-asc    # Sort by price (ascending)
GET    /api/products/sort/price-desc   # Sort by price (descending)
GET    /api/products/sort/newest       # Sort by creation date
PUT    /api/products/{id}/stock        # Update stock (ADMIN)
GET    /api/products/count/{category}  # Count by category
```

## 🔐 Authentication

### Default Users

The application comes with pre-configured users:

- **Admin**: `admin@example.com` / `password`
- **User**: `user@example.com` / `password`

### JWT Token Usage

Include the JWT token in the Authorization header:

```
Authorization: Bearer <your-jwt-token>
```

### Role-based Access

- **USER**: Can view products, search, and filter
- **ADMIN**: Full access to all endpoints including user management

## 🧪 Testing

### Running Tests

```bash
# Run all tests
mvn test

# Run tests with coverage
mvn test jacoco:report

# Run specific test class
mvn test -Dtest=ProductServiceTest
```

### Test Coverage

The application includes comprehensive unit tests for:

- Service layer business logic
- Repository layer data access
- Controller layer API endpoints
- Security configuration
- Exception handling

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

## 🔍 Monitoring & Health

### Actuator Endpoints

- **Health Check**: `GET /api/actuator/health`
- **Application Info**: `GET /api/actuator/info`
- **Metrics**: `GET /api/actuator/metrics`

### Logging

The application uses SLF4J with the following log levels:

- **DEBUG**: Application and Spring Security
- **INFO**: Business operations
- **WARN**: Validation and authentication failures
- **ERROR**: System errors and exceptions

## 🚀 Deployment

### JAR Deployment

```bash
# Build executable JAR
mvn clean package

# Run JAR file
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

### Docker Deployment

```dockerfile
FROM openjdk:17-jdk-slim
COPY target/backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 🔧 Development

### IDE Setup

Recommended IDE settings:

- **IntelliJ IDEA**: Import as Maven project
- **Eclipse**: Import as existing Maven project
- **VS Code**: Install Java extension pack
