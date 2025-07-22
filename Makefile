# Product Catalog Application Makefile
# Author: Christian Otieno
# Version: 1.0.0

.PHONY: help build up down restart logs clean reset test api-test frontend-test backend-test

# Default target
help:
	@echo "Product Catalog Application - Available Commands:"
	@echo ""
	@echo "Build and Run:"
	@echo "  build     - Build all Docker images"
	@echo "  up        - Start all services"
	@echo "  down      - Stop all services"
	@echo "  restart   - Restart all services"
	@echo "  dev       - Start in development mode with logs"
	@echo ""
	@echo "Monitoring:"
	@echo "  logs      - Show logs from all services"
	@echo "  logs-backend  - Show backend logs only"
	@echo "  logs-frontend - Show frontend logs only"
	@echo "  logs-db    - Show database logs only"
	@echo ""
	@echo "Database:"
	@echo "  db-shell  - Connect to PostgreSQL shell"
	@echo "  db-reset  - Reset database (remove volumes)"
	@echo ""
	@echo "Testing:"
	@echo "  test      - Run all tests"
	@echo "  api-test  - Test API endpoints"
	@echo "  frontend-test - Run frontend tests"
	@echo "  backend-test  - Run backend tests"
	@echo ""
	@echo "Maintenance:"
	@echo "  clean     - Remove all containers, images, and volumes"
	@echo "  reset     - Complete reset (clean + rebuild)"
	@echo ""
	@echo "URLs:"
	@echo "  Frontend: http://localhost:3000"
	@echo "  Backend API: http://localhost:8080/api"
	@echo "  Health Check: http://localhost:8080/api/health"
	@echo ""
	@echo "Default Users:"
	@echo "  Admin: admin@example.com / password"
	@echo "  User: user@example.com / password"

# Build all Docker images
build:
	@echo "Building Docker images..."
	docker-compose build --no-cache

# Start all services
up:
	@echo "Starting services..."
	docker-compose up -d

# Start in development mode with logs
dev:
	@echo "Starting services in development mode..."
	docker-compose up --build

# Stop all services
down:
	@echo "Stopping services..."
	docker-compose down

# Restart all services
restart: down up
	@echo "Services restarted"

# Show logs from all services
logs:
	docker-compose logs -f

# Show backend logs only
logs-backend:
	docker-compose logs -f backend

# Show frontend logs only
logs-frontend:
	docker-compose logs -f frontend

# Show database logs only
logs-db:
	docker-compose logs -f postgres

# Connect to PostgreSQL shell
db-shell:
	docker exec -it product_catalog_db psql -U postgres -d product_catalog

# Reset database (remove volumes)
db-reset:
	@echo "Resetting database..."
	docker-compose down -v
	docker-compose up -d postgres
	@echo "Waiting for database to be ready..."
	@sleep 10
	docker-compose up -d backend
	@echo "Database reset complete"

# Run all tests
test: backend-test frontend-test api-test

# Test API endpoints
api-test:
	@echo "Testing API endpoints..."
	@echo "Testing health endpoint..."
	@curl -f http://localhost:8080/api/health || echo "Health check failed"
	@echo ""
	@echo "Testing login endpoint..."
	@curl -X POST http://localhost:8080/api/auth/login \
		-H "Content-Type: application/json" \
		-d '{"email":"admin@example.com","password":"password"}' || echo "Login test failed"
	@echo ""

# Run frontend tests
frontend-test:
	@echo "Running frontend tests..."
	cd frontend && npm test -- --watchAll=false --passWithNoTests || echo "No frontend tests found or tests failed"

# Run backend tests
backend-test:
	@echo "Running backend tests..."
	cd backend && mvn test

# Remove all containers, images, and volumes
clean:
	@echo "Cleaning up Docker resources..."
	docker-compose down -v --rmi all
	docker system prune -f

# Complete reset (clean + rebuild)
reset: clean build up
	@echo "Complete reset completed"

# Check service status
status:
	@echo "Service Status:"
	docker-compose ps

# Show service URLs
urls:
	@echo "Application URLs:"
	@echo "Frontend: http://localhost:3000"
	@echo "Backend API: http://localhost:8080/api"
	@echo "Health Check: http://localhost:8080/api/health"
	@echo ""
	@echo "Database:"
	@echo "Host: localhost"
	@echo "Port: 5432"
	@echo "Database: product_catalog"
	@echo "Username: postgres"
	@echo "Password: password"

# Wait for services to be ready
wait:
	@echo "Waiting for services to be ready..."
	@until curl -f http://localhost:8080/api/health > /dev/null 2>&1; do \
		echo "Waiting for backend..."; \
		sleep 5; \
	done
	@echo "All services are ready!"

# Quick start (build + up + wait)
start: build up wait urls
	@echo "Application started successfully!" 