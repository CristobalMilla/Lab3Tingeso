import React from 'react'
import { Typography, Box } from '@mui/material'

const ReservaPage: React.FC = () => {
  return (
    <Box>
      <Typography variant="h3" component="h1" gutterBottom>
        Hacer Reserva
      </Typography>
      <Typography variant="body1">
        Esta página permitirá hacer nuevas reservas.
      </Typography>
    </Box>
  )
}

export default ReservaPage