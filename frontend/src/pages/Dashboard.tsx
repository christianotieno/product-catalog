import React, { useEffect, useState } from 'react';
import {
  Container,
  Typography,
  Grid,
  Card,
  CardContent,
  Box,
  Button,
} from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { useProduct } from '../contexts/ProductContext';
import { Product } from '../types';

const Dashboard: React.FC = () => {
  const navigate = useNavigate();
  const { user } = useAuth();
  const { products, isLoading } = useProduct();
  const [stats, setStats] = useState({
    totalProducts: 0,
    totalValue: 0,
  });

  useEffect(() => {
    if (products) {
      const totalValue = products.reduce((sum: number, product: Product) => sum + (product.price || 0), 0);
      setStats({
        totalProducts: products.length,
        totalValue,
      });
    }
  }, [products]);

  return (
    <Container maxWidth="lg">
      <Box sx={{ mb: 4 }}>
        <Typography variant="h4" component="h1" gutterBottom>
          Dashboard
        </Typography>
        <Typography variant="body1" color="text.secondary">
          Welcome back, {user?.email}!
        </Typography>
      </Box>

      <Grid container spacing={3} sx={{ mb: 4 }}>
        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Typography color="text.secondary" gutterBottom>
                Total Products
              </Typography>
              <Typography variant="h4" component="div">
                {isLoading ? '...' : stats.totalProducts}
              </Typography>
            </CardContent>
          </Card>
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Typography color="text.secondary" gutterBottom>
                Total Value
              </Typography>
              <Typography variant="h4" component="div">
                ${isLoading ? '...' : stats.totalValue.toFixed(2)}
              </Typography>
            </CardContent>
          </Card>
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Typography color="text.secondary" gutterBottom>
                User Role
              </Typography>
              <Typography variant="h6" component="div">
                {user?.role}
              </Typography>
            </CardContent>
          </Card>
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Typography color="text.secondary" gutterBottom>
                Actions
              </Typography>
              <Button
                variant="contained"
                size="small"
                onClick={() => navigate('/products')}
                sx={{ mr: 1 }}
              >
                View Products
              </Button>
              {user?.role === 'ADMIN' && (
                <Button
                  variant="outlined"
                  size="small"
                  onClick={() => navigate('/products/new')}
                >
                  Add Product
                </Button>
              )}
            </CardContent>
          </Card>
        </Grid>
      </Grid>

      <Card>
        <CardContent>
          <Typography variant="h6" gutterBottom>
            Recent Products
          </Typography>
          {isLoading ? (
            <Typography>Loading products...</Typography>
          ) : products && products.length > 0 ? (
            <Grid container spacing={2}>
              {products.slice(0, 6).map((product) => (
                <Grid item xs={12} sm={6} md={4} key={product.id}>
                  <Card variant="outlined">
                    <CardContent>
                      <Typography variant="h6" noWrap>
                        {product.name}
                      </Typography>
                      <Typography color="text.secondary">
                        ${product.price}
                      </Typography>
                      <Typography variant="body2" noWrap>
                        {product.description}
                      </Typography>
                    </CardContent>
                  </Card>
                </Grid>
              ))}
            </Grid>
          ) : (
            <Typography color="text.secondary">
              No products found. {user?.role === 'ADMIN' && (
                <Button
                  variant="text"
                  onClick={() => navigate('/products/new')}
                >
                  Add your first product
                </Button>
              )}
            </Typography>
          )}
        </CardContent>
      </Card>
    </Container>
  );
};

export default Dashboard; 