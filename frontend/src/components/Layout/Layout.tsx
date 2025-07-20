import React from 'react';
import { Outlet } from 'react-router-dom';
import { AppBar, Toolbar, Typography, Button, Box, Container } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';

const Layout: React.FC = () => {
  const navigate = useNavigate();
  const { user, logout } = useAuth();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const handleNavigation = (path: string) => {
    navigate(path);
  };

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', minHeight: '100vh' }}>
      <AppBar position="static">
        <Toolbar>
          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            Product Catalog
          </Typography>
          <Button color="inherit" onClick={() => handleNavigation('/dashboard')}>
            Dashboard
          </Button>
          <Button color="inherit" onClick={() => handleNavigation('/products')}>
            Products
          </Button>
          {user?.role === 'ADMIN' && (
            <>
              <Button color="inherit" onClick={() => handleNavigation('/products/new')}>
                Add Product
              </Button>
              <Button color="inherit" onClick={() => handleNavigation('/users')}>
                Users
              </Button>
            </>
          )}
          <Button color="inherit" onClick={handleLogout}>
            Logout
          </Button>
        </Toolbar>
      </AppBar>
      
      <Container component="main" sx={{ flexGrow: 1, py: 3 }}>
        <Outlet />
      </Container>
    </Box>
  );
};

export default Layout; 