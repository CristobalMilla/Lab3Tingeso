import React from 'react'
import {
  Typography,
  Card,
  CardContent,
  Button,
  Box,
  CardActions,
  Stack,
} from '@mui/material'
import {
  Schedule as ScheduleIcon,
  Assessment as ReportIcon,
  BookOnline as ReservaIcon,
  DirectionsCar as KartIcon,
} from '@mui/icons-material'
import { Link } from 'react-router-dom'

const HomePage: React.FC = () => {
  const features = [
    {
      title: 'Horario Semanal',
      description: 'Consulta el horario semanal de la pista y las reservas existentes',
      icon: <ScheduleIcon sx={{ fontSize: 48 }} />,
      path: '/horario-semanal',
      color: 'primary' as const,
    },
    {
      title: 'Reporte de Tarifas',
      description: 'Genera reportes de tarifas y descuentos por períodos específicos',
      icon: <ReportIcon sx={{ fontSize: 48 }} />,
      path: '/reporte-tarifas',
      color: 'secondary' as const,
    },
    {
      title: 'Hacer Reserva',
      description: 'Realiza una nueva reserva en la pista de karting',
      icon: <ReservaIcon sx={{ fontSize: 48 }} />,
      path: '/reserva',
      color: 'success' as const,
    },
  ]

  return (
    <Box>
      {/* Hero Section */}
      <Box
        sx={{
          textAlign: 'center',
          mb: 6,
          p: 4,
          backgroundColor: 'primary.main',
          color: 'white',
          borderRadius: 2,
        }}
      >
        <KartIcon sx={{ fontSize: 80, mb: 2 }} />
        <Typography variant="h2" component="h1" gutterBottom>
          Bienvenido a KartingRM
        </Typography>
        <Typography variant="h5" component="p" sx={{ opacity: 0.9 }}>
          Sistema de gestión de reservas para pista de karting
        </Typography>
      </Box>

      {/* Features Grid */}
      <Stack
        direction={{ xs: 'column', md: 'row' }}
        spacing={4}
        sx={{ mb: 6 }}
      >
        {features.map((feature, index) => (
          <Box key={index} sx={{ flex: 1 }}>
            <Card
              sx={{
                height: '100%',
                display: 'flex',
                flexDirection: 'column',
                transition: 'transform 0.2s ease-in-out',
                '&:hover': {
                  transform: 'translateY(-4px)',
                },
              }}
            >
              <CardContent sx={{ flexGrow: 1, textAlign: 'center' }}>
                <Box
                  sx={{
                    color: `${feature.color}.main`,
                    mb: 2,
                  }}
                >
                  {feature.icon}
                </Box>
                <Typography gutterBottom variant="h5" component="h2">
                  {feature.title}
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  {feature.description}
                </Typography>
              </CardContent>
              <CardActions sx={{ justifyContent: 'center', pb: 2 }}>
                <Button
                  component={Link}
                  to={feature.path}
                  variant="contained"
                  color={feature.color}
                  size="large"
                >
                  Acceder
                </Button>
              </CardActions>
            </Card>
          </Box>
        ))}
      </Stack>

      {/* Information Section */}
      <Box sx={{ mt: 6, textAlign: 'center' }}>
        <Typography variant="h4" gutterBottom>
          Información de la Pista
        </Typography>
        <Stack
          direction={{ xs: 'column', md: 'row' }}
          spacing={3}
          sx={{ mt: 2 }}
        >
          <Box sx={{ flex: 1 }}>
            <Card>
              <CardContent>
                <Typography variant="h6" gutterBottom>
                  Horarios de Operación
                </Typography>
                <Typography variant="body2">
                  Lunes a Viernes: 14:00 - 22:00
                  <br />
                  Sábados, Domingos y Feriados: 10:00 - 22:00
                </Typography>
              </CardContent>
            </Card>
          </Box>
          <Box sx={{ flex: 1 }}>
            <Card>
              <CardContent>
                <Typography variant="h6" gutterBottom>
                  Capacidad
                </Typography>
                <Typography variant="body2">
                  Hasta 15 personas simultáneas
                  <br />
                  Bloques de 30, 35 y 40 minutos
                </Typography>
              </CardContent>
            </Card>
          </Box>
          <Box sx={{ flex: 1 }}>
            <Card>
              <CardContent>
                <Typography variant="h6" gutterBottom>
                  Servicios
                </Typography>
                <Typography variant="body2">
                  Alquiler de karts
                  <br />
                  Tarifas por tiempo y vueltas
                </Typography>
              </CardContent>
            </Card>
          </Box>
        </Stack>
      </Box>
    </Box>
  )
}

export default HomePage