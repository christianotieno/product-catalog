import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  Container,
  Typography,
  TextField,
  Button,
  Box,
  Card,
  CardContent,
  Alert,
} from '@mui/material';
import { ArrowBack as ArrowBackIcon } from '@mui/icons-material';
import { useProduct } from '../contexts/ProductContext';
import { ProductRequest } from '../types';

const ProductForm: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { selectedProduct, isLoading, fetchProduct, createProduct, updateProduct } = useProduct();
  const [formData, setFormData] = useState<ProductRequest>({
    name: '',
    description: '',
    price: 0,
    category: '',
    stockQuantity: 0,
  });
  const [error, setError] = useState('');
  const [submitting, setSubmitting] = useState(false);

  const isEditing = Boolean(id);

  useEffect(() => {
    if (isEditing && id) {
      fetchProduct(parseInt(id));
    }
  }, [isEditing, id, fetchProduct]);

  useEffect(() => {
    if (selectedProduct && isEditing) {
      setFormData({
        name: selectedProduct.name,
        description: selectedProduct.description || '',
        price: selectedProduct.price,
        category: selectedProduct.category || '',
        stockQuantity: selectedProduct.stockQuantity,
      });
    }
  }, [selectedProduct, isEditing]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setSubmitting(true);

    try {
      if (isEditing && id) {
        await updateProduct(parseInt(id), formData);
      } else {
        await createProduct(formData);
      }
      navigate('/products');
    } catch (err) {
      setError('Failed to save product. Please try again.');
    } finally {
      setSubmitting(false);
    }
  };

  const handleChange = (field: keyof ProductRequest) => (
    e: React.ChangeEvent<HTMLInputElement | { value: unknown }>
  ) => {
    const value = e.target.value;
    setFormData(prev => ({
      ...prev,
      [field]: field === 'price' || field === 'stockQuantity' ? Number(value) : value,
    }));
  };

  if (isLoading && isEditing) {
    return (
      <Container maxWidth="md">
        <Typography>Loading product...</Typography>
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
        
        <Typography variant="h4" component="h1">
          {isEditing ? 'Edit Product' : 'Add New Product'}
        </Typography>
      </Box>

      <Card>
        <CardContent>
          {error && (
            <Alert severity="error" sx={{ mb: 2 }}>
              {error}
            </Alert>
          )}

          <Box component="form" onSubmit={handleSubmit}>
            <TextField
              fullWidth
              label="Product Name"
              value={formData.name}
              onChange={handleChange('name')}
              required
              margin="normal"
            />
            
            <TextField
              fullWidth
              label="Description"
              value={formData.description}
              onChange={handleChange('description')}
              multiline
              rows={4}
              margin="normal"
            />
            
            <TextField
              fullWidth
              label="Price"
              type="number"
              value={formData.price}
              onChange={handleChange('price')}
              required
              margin="normal"
              inputProps={{ min: 0, step: 0.01 }}
            />
            
            <TextField
              fullWidth
              label="Category"
              value={formData.category}
              onChange={handleChange('category')}
              margin="normal"
            />
            
            <TextField
              fullWidth
              label="Stock Quantity"
              type="number"
              value={formData.stockQuantity}
              onChange={handleChange('stockQuantity')}
              required
              margin="normal"
              inputProps={{ min: 0 }}
            />
            
            <Box sx={{ mt: 3, display: 'flex', gap: 2 }}>
              <Button
                type="submit"
                variant="contained"
                disabled={submitting}
              >
                {submitting ? 'Saving...' : (isEditing ? 'Update Product' : 'Create Product')}
              </Button>
              <Button
                variant="outlined"
                onClick={() => navigate('/products')}
                disabled={submitting}
              >
                Cancel
              </Button>
            </Box>
          </Box>
        </CardContent>
      </Card>
    </Container>
  );
};

export default ProductForm; 