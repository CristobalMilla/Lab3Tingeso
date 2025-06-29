import React from 'react'
import { Typography, Box, Button } from '@mui/material'
import { Link } from 'react-router-dom'
import { Home as HomeIcon } from '@mui/icons-material'

const NotFoundPage: React.FC = () => {
  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        minHeight: '60vh',
        textAlign: 'center',
      }}
    >
      <Typography variant="h1" component="h1" gutterBottom sx={{ fontSize: '4rem' }}>
        404
      </Typography>
      <Typography variant="h4" component="h2" gutterBottom>
        Página no encontrada
      </Typography>
      <Typography variant="body1" sx={{ mb: 3 }}>
        La página que buscas no existe o ha sido movida.
      </Typography>
      <Button
        component={Link}
        to="/"
        variant="contained"
        color="primary"
        startIcon={<HomeIcon />}
        size="large"
      >
        Volver al Inicio
      </Button>
    </Box>
  )
}

export default NotFoundPage