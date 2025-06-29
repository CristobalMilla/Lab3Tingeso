import { createTheme } from '@mui/material/styles'

const theme = createTheme({
  palette: {
    primary: {
      main: '#d32f2f',        // Red primary
      light: '#ff6659',       // Light red
      dark: '#9a0007',        // Dark red
    },
    secondary: {
      main: '#424242',        // Dark gray for secondary elements
      light: '#6d6d6d',       // Light gray
      dark: '#1b1b1b',        // Almost black
    },
    background: {
      default: '#ffffff',     // White background
      paper: '#ffffff',       // White paper
    },
    text: {
      primary: '#000000',     // Black text
      secondary: '#424242',   // Dark gray text
    },
    error: {
      main: '#d32f2f',        // Red for errors
    },
    success: {
      main: '#2e7d32',        // Green for success (subtle)
    },
  },
  typography: {
    fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
    h1: {
      fontSize: '2.5rem',
      fontWeight: 600,
      color: '#000000',
    },
    h2: {
      fontSize: '2rem',
      fontWeight: 600,
      color: '#000000',
    },
    h3: {
      fontSize: '1.75rem',
      fontWeight: 500,
      color: '#000000',
    },
    h4: {
      fontSize: '1.5rem',
      fontWeight: 500,
      color: '#000000',
    },
    body1: {
      color: '#000000',
    },
    body2: {
      color: '#424242',
    },
  },
  components: {
    MuiButton: {
      styleOverrides: {
        root: {
          textTransform: 'none',
          borderRadius: 8,
          fontWeight: 500,
        },
      },
    },
    MuiCard: {
      styleOverrides: {
        root: {
          borderRadius: 12,
          boxShadow: '0 2px 8px rgba(0,0,0,0.1)',
          border: '1px solid #e0e0e0',
        },
      },
    },
    MuiAppBar: {
      styleOverrides: {
        root: {
          backgroundColor: '#d32f2f',
          color: '#ffffff',
        },
      },
    },
  },
})

export default theme