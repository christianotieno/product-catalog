import React, { useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  Container,
  Typography,
  Card,
  CardContent,
  Button,
  Box,
  Chip,
  Grid,
} from '@mui/material';
import { ArrowBack as ArrowBackIcon, Edit as EditIcon } from '@mui/icons-material';
import { useAuth } from '../contexts/AuthContext';
import { useProduct } from '../contexts/ProductContext';

const ProductDetail: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { user } = useAuth();
  const { selectedProduct, isLoading, fetchProduct } = useProduct();

  useEffect(() => {
    if (id) {
      fetchProduct(parseInt(id));
    }
  }, [id, fetchProduct]);

  if (isLoading) {
    return (
      <Container maxWidth="md">
        <Typography>Loading product...</Typography>
      </Container>
    );
  }

  if (!selectedProduct) {
    return (
      <Container maxWidth="md">
        <Typography>Product not found.</Typography>
      </Container>
    );
  }

  return (
    <Container maxWidth="md">
      <Box sx={{ mb: 3 }}>
        <Button
          startIcon={<ArrowBackIcon />}
          onClick={() => navigate('/products')}
          sx={{ mb: 2 }}
        >
          Back to Products
        </Button>
        
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <Typography variant="h4" component="h1">
            {selectedProduct.name}
          </Typography>
          {user?.role === 'ADMIN' && (
            <Button
              variant="contained"
              startIcon={<EditIcon />}
              onClick={() => navigate(`/products/${selectedProduct.id}/edit`)}
            >
              Edit Product
            </Button>
          )}
        </Box>
      </Box>

      <Card>
        <CardContent>
          <Grid container spacing={3}>
            <Grid item xs={12} md={8}>
              <Typography variant="h5" gutterBottom>
                {selectedProduct.name}
              </Typography>
              
              <Typography variant="h4" color="primary" gutterBottom>
                ${selectedProduct.price}
              </Typography>
              
              <Typography variant="body1" paragraph>
                {selectedProduct.description}
              </Typography>
              
              <Box sx={{ display: 'flex', gap: 1, mb: 2 }}>
                {selectedProduct.category && (
                  <Chip label={selectedProduct.category} />
                )}
                <Chip
                  label={selectedProduct.inStock ? 'In Stock' : 'Out of Stock'}
                  color={selectedProduct.inStock ? 'success' : 'error'}
                />
                {selectedProduct.lowStock && (
                  <Chip label="Low Stock" color="warning" />
                )}
              </Box>
            </Grid>
            
            <Grid item xs={12} md={4}>
              <Card variant="outlined">
                <CardContent>
                  <Typography variant="h6" gutterBottom>
                    Product Information
                  </Typography>
                  
                  <Box sx={{ mb: 2 }}>
                    <Typography variant="body2" color="text.secondary">
                      Stock Quantity
                    </Typography>
                    <Typography variant="h6">
                      {selectedProduct.stockQuantity}
                    </Typography>
                  </Box>
                  
                  <Box sx={{ mb: 2 }}>
                    <Typography variant="body2" color="text.secondary">
                      Created
                    </Typography>
                    <Typography variant="body2">
                      {new Date(selectedProduct.createdAt).toLocaleDateString()}
                    </Typography>
                  </Box>
                  
                  <Box sx={{ mb: 2 }}>
                    <Typography variant="body2" color="text.secondary">
                      Last Updated
                    </Typography>
                    <Typography variant="body2">
                      {new Date(selectedProduct.updatedAt).toLocaleDateString()}
                    </Typography>
                  </Box>
                </CardContent>
              </Card>
            </Grid>
          </Grid>
        </CardContent>
      </Card>
    </Container>
  );
};

export default ProductDetail; 