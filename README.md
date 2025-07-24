# 🛍️ Product Catalog Management System

A complete web application for managing products with user authentication and admin features.

## 🚀 Quick Start

```bash
# Clone and start everything
git clone git@github.com:christianotieno/product-catalog.git
cd product-catalog
make start

# Open your browser
# Frontend: http://localhost:3000
# Backend: http://localhost:8080/api
```

## 🔑 Login

| Role | Email | Password |
|------|-------|----------|
| **Admin** | `admin@example.com` | `password` |
| **User** | `user@example.com` | `password` |

## 🎯 Features

### 👤 Regular User

- Browse, search, and filter products
- View product details and stock

### 👨‍💼 Admin User

- Everything above + add/edit/delete products
- Manage user accounts and stock

## 🛠️ Commands

```bash
make start      # Start everything
make stop       # Stop everything  
make restart    # Restart everything
make logs       # View logs
make status     # Check status
make test       # Run tests
make clean      # Remove everything
make db-reset   # Reset database
```

## 🏗️ Tech Stack

- **Backend**: Java 17 + Spring Boot + PostgreSQL
- **Frontend**: React 18 + TypeScript + Material-UI
- **Infrastructure**: Docker + Docker Compose + Nginx
- **Security**: JWT authentication + role-based access

## 📊 API Endpoints

### Auth

- `POST /api/auth/login` - Login
- `POST /api/auth/register` - Register  
- `GET /api/auth/validate` - Validate token

### Products

- `GET /api/products` - Get all products
- `POST /api/products` - Create (Admin)
- `PUT /api/products/{id}` - Update (Admin)
- `DELETE /api/products/{id}` - Delete (Admin)
- `GET /api/products/search` - Search

### Health

- `GET /api/health` - Check if API is running

## 🧪 Testing

```bash
make test           # All tests
make backend-test   # Backend only
make frontend-test  # Frontend only
make api-test       # API tests
```

**Status**: 18 backend tests passing ✅

## 🔍 Troubleshooting

**App won't start?**

```bash
make down
make start
```

**Database issues?**

```bash
make db-reset
```

**Build problems?**

```bash
make clean
make build
```

**View logs?**

```bash
make logs
```

## 🚀 Development

### Local Setup

```bash
# Backend
cd backend && mvn spring-boot:run

# Frontend  
cd frontend && npm install && npm start
```

### Project Structure

```
code-challenge/
├── backend/          # Spring Boot API
├── frontend/         # React App
├── docker-compose.yml
├── Makefile
└── README.md
```

## 🔒 Security

- JWT authentication with secure tokens
- Role-based access control (Admin/User)
- Input validation on frontend and backend
- Password encryption with BCrypt
- CORS protection

## 📈 Performance

- Optimized React components with memoization
- Efficient API calls with proper caching
- Fast database queries with indexing
- Responsive design for all devices

## 🚀 Deployment

### Production

1. Update environment variables in `docker-compose.yml`
2. Set secure JWT secret
3. Configure database credentials
4. Enable HTTPS
5. Set up monitoring

### Environment Variables

```bash
JWT_SECRET=your-secure-secret
SPRING_DATASOURCE_PASSWORD=your-db-password
REACT_APP_API_BASE_URL=/api
```

## 🤝 Contributing

This project demonstrates:

- ✅ Clean, readable code
- ✅ Comprehensive testing (18 tests passing)
- ✅ Modern development practices
- ✅ Scalable architecture
- ✅ Security best practices

## 📞 Support

**Need help?**

1. Check logs: `make logs`
2. Restart: `make restart`
3. Reset DB: `make db-reset`
4. Clean rebuild: `make clean && make build`

---

**Built with ❤️ using Spring Boot, React, and Docker**
