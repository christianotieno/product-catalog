# Product Catalog Management System - Docker Setup

This document provides instructions for running the fullstack application using Docker and Docker Compose.

## Prerequisites

- Docker (version 20.10 or higher)
- Docker Compose (version 2.0 or higher)

## Quick Start

1. **Clone the repository** (if not already done):

   ```bash
   git clone git@github.com:christianotieno/code-challenge-natixis.git
   cd code-challenge-natixis
   ```

2. **Build and start all services**:

   ```bash
   docker-compose up --build
   ```

3. **Access the application**:
   - Frontend: <http://localhost:3000>
   - Backend API: <http://localhost:8080/api>
   - Database: localhost:5432

## Services

The application consists of three main services:

### 1. PostgreSQL Database (`postgres`)

- **Port**: 5432
- **Database**: product_catalog
- **Username**: postgres
- **Password**: password
- **Data Persistence**: PostgreSQL data is persisted in a Docker volume

### 2. Spring Boot Backend (`backend`)

- **Port**: 8080
- **API Base URL**: <http://localhost:8080/api>
- **Database**: Connects to PostgreSQL
- **Features**: JWT authentication, RESTful APIs, data persistence

### 3. React Frontend (`frontend`)

- **Port**: 3000
- **UI**: Material-UI based interface
- **Features**: User authentication, product management, admin panel

## Environment Variables

### Backend Environment Variables

- `SPRING_PROFILES_ACTIVE`: Set to `docker` for containerized environment
- `SPRING_DATASOURCE_URL`: PostgreSQL connection URL
- `SPRING_DATASOURCE_USERNAME`: Database username
- `SPRING_DATASOURCE_PASSWORD`: Database password
- `JWT_SECRET`: Secret key for JWT token generation

### Frontend Environment Variables

- `REACT_APP_API_BASE_URL`: Backend API base URL

## Docker Commands

### Start all services

```bash
docker-compose up
```

### Start services in background

```bash
docker-compose up -d
```

### Build and start services

```bash
docker-compose up --build
```

### Stop all services

```bash
docker-compose down
```

### Stop services and remove volumes

```bash
docker-compose down -v
```

### View logs

```bash
# All services
docker-compose logs

# Specific service
docker-compose logs backend
docker-compose logs frontend
docker-compose logs postgres
```

### Access service containers

```bash
# Backend container
docker-compose exec backend sh

# Frontend container
docker-compose exec frontend sh

# Database container
docker-compose exec postgres psql -U postgres -d product_catalog
```

## Development Workflow

### Making Changes

1. **Backend Changes**:
   - Modify Java code in `backend/src/`
   - Rebuild: `docker-compose build backend`
   - Restart: `docker-compose up backend`

2. **Frontend Changes**:
   - Modify React code in `frontend/src/`
   - Rebuild: `docker-compose build frontend`
   - Restart: `docker-compose up frontend`

3. **Database Changes**:
   - Modify `backend/src/main/resources/data.sql`
   - Restart backend: `docker-compose restart backend`

### Hot Reload (Development)

For development with hot reload:

1. **Backend Development**:

   ```bash
   cd backend
   ./mvnw spring-boot:run
   ```

2. **Frontend Development**:

   ```bash
   cd frontend
   npm start
   ```

3. **Database Only**:

   ```bash
   docker-compose up postgres
   ```

## Troubleshooting

### Common Issues

1. **Port Already in Use**:

   ```bash
   # Check what's using the port
   lsof -i :3000
   lsof -i :8080
   lsof -i :5432
   
   # Stop conflicting services
   docker-compose down
   ```

2. **Database Connection Issues**:

   ```bash
   # Check database logs
   docker-compose logs postgres
   
   # Restart database
   docker-compose restart postgres
   ```

3. **Build Failures**:

   ```bash
   # Clean and rebuild
   docker-compose down
   docker system prune -f
   docker-compose up --build
   ```

4. **Permission Issues**:

   ```bash
   # Fix file permissions
   sudo chown -R $USER:$USER .
   ```

### Health Checks

The services include health checks to ensure proper startup order:

- **Database**: Checks if PostgreSQL is ready to accept connections
- **Backend**: Checks if the Spring Boot application is responding
- **Frontend**: Depends on backend being healthy

### Logs and Debugging

```bash
# Follow logs in real-time
docker-compose logs -f

# View specific service logs
docker-compose logs -f backend

# Check service status
docker-compose ps
```

## Production Deployment

For production deployment:

1. **Update environment variables** in `docker-compose.yml`
2. **Change JWT secret** to a secure value
3. **Use proper database credentials**
4. **Configure SSL/TLS** for HTTPS
5. **Set up proper logging** and monitoring
6. **Use Docker secrets** for sensitive data

## Cleanup

To completely remove the application:

```bash
# Stop and remove containers, networks, and volumes
docker-compose down -v

# Remove images
docker rmi code-challenge-backend code-challenge-frontend

# Clean up unused Docker resources
docker system prune -f
```
