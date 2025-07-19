-- Initialize Users Table
INSERT INTO users (email, password, role, created_at) VALUES
('admin@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'ADMIN', CURRENT_TIMESTAMP),
('user@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'USER', CURRENT_TIMESTAMP);

-- Initialize Products Table
INSERT INTO products (name, description, price, category, stock_quantity, created_at, updated_at) VALUES
('MacBook Pro 16"', 'Apple MacBook Pro with M2 Pro chip, 16GB RAM, 512GB SSD', 2499.99, 'Electronics', 15, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('iPhone 15 Pro', 'Apple iPhone 15 Pro with A17 Pro chip, 128GB storage', 999.99, 'Electronics', 25, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Sony WH-1000XM5', 'Wireless noise-canceling headphones with 30-hour battery life', 349.99, 'Electronics', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Nike Air Max 270', 'Comfortable running shoes with Air Max technology', 129.99, 'Sports', 50, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Adidas Ultraboost 22', 'Premium running shoes with Boost midsole technology', 179.99, 'Sports', 35, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Samsung 65" QLED TV', '4K QLED Smart TV with Quantum Dot technology', 1299.99, 'Electronics', 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Dell XPS 13', '13-inch laptop with Intel i7 processor, 16GB RAM', 1199.99, 'Electronics', 20, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Canon EOS R6', 'Full-frame mirrorless camera with 20MP sensor', 2499.99, 'Electronics', 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Yoga Mat Premium', 'Non-slip yoga mat with alignment lines', 49.99, 'Sports', 100, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Wireless Charger', 'Fast wireless charging pad compatible with all devices', 29.99, 'Electronics', 75, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP); 