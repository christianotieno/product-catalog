import React, { useEffect, useState } from 'react';
import {
  Container,
  Typography,
  Card,
  CardContent,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Chip,
  IconButton,
  Tooltip,
} from '@mui/material';
import { Edit as EditIcon } from '@mui/icons-material';
import { useAuth } from '../contexts/AuthContext';

interface User {
  id: number;
  email: string;
  role: 'USER' | 'ADMIN';
  createdAt: string;
}

const Users: React.FC = () => {
  const { user: currentUser } = useAuth();
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const sampleUsers: User[] = [
      {
        id: 1,
        email: 'admin@example.com',
        role: 'ADMIN',
        createdAt: '2024-01-01T00:00:00Z',
      },
      {
        id: 2,
        email: 'user1@example.com',
        role: 'USER',
        createdAt: '2024-01-15T00:00:00Z',
      },
      {
        id: 3,
        email: 'user2@example.com',
        role: 'USER',
        createdAt: '2024-02-01T00:00:00Z',
      },
    ];
    
    setUsers(sampleUsers);
    setLoading(false);
  }, []);

  const handleEditUser = (userId: number) => {  
    console.log('Edit user:', userId);
  };

  if (loading) {
    return (
      <Container maxWidth="lg">
        <Typography>Loading users...</Typography>
      </Container>
    );
  }

  return (
    <Container maxWidth="lg">
      <Typography variant="h4" component="h1" gutterBottom>
        User Management
      </Typography>
      
      <Card>
        <CardContent>
          <TableContainer component={Paper}>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>ID</TableCell>
                  <TableCell>Email</TableCell>
                  <TableCell>Role</TableCell>
                  <TableCell>Created At</TableCell>
                  <TableCell>Actions</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {users.map((user) => (
                  <TableRow key={user.id}>
                    <TableCell>{user.id}</TableCell>
                    <TableCell>{user.email}</TableCell>
                    <TableCell>
                      <Chip
                        label={user.role}
                        color={user.role === 'ADMIN' ? 'primary' : 'default'}
                        size="small"
                      />
                    </TableCell>
                    <TableCell>
                      {new Date(user.createdAt).toLocaleDateString()}
                    </TableCell>
                    <TableCell>
                      <Tooltip title="Edit User">
                        <IconButton
                          size="small"
                          onClick={() => handleEditUser(user.id)}
                          disabled={user.id === currentUser?.id}
                        >
                          <EditIcon />
                        </IconButton>
                      </Tooltip>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        </CardContent>
      </Card>
    </Container>
  );
};

export default Users; 