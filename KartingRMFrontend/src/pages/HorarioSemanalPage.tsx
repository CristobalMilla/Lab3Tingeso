import React from 'react'
import { Typography, Box } from '@mui/material'

const HorarioSemanalPage: React.FC = () => {
  return (
    <Box>
      <Typography variant="h3" component="h1" gutterBottom>
        Horario Semanal
      </Typography>
      <Typography variant="body1">
        Esta página mostrará el horario semanal de la pista.
      </Typography>
    </Box>
  )
}

export default HorarioSemanalPage