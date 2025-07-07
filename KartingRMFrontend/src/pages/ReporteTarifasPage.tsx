import React, { useState, useEffect } from 'react'
import {
  Typography,
  Box,
  Card,
  CardContent,
  Button,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Stack,
  Chip,
  CircularProgress,
  Alert,
  IconButton,
  Tooltip,
  Divider,
  FormControl,
  InputLabel,
  Select,
  MenuItem
} from '@mui/material'
import {
  Assessment as ReportIcon,
  Refresh as RefreshIcon,
  Download as DownloadIcon,
  DateRange as DateIcon,
  AttachMoney as MoneyIcon
} from '@mui/icons-material'
import { format } from 'date-fns'
import { es } from 'date-fns/locale'
import { useQuery } from '@tanstack/react-query'
import toast from 'react-hot-toast'

import { reportsApi } from '../services/reportsApi'

interface FeeTypeReportData {
  Descripcion: string
  [month: string]: string | number | undefined
  Total?: number
  'Gran Total'?: number
}

const ReporteTarifasPage: React.FC = () => {
  // State for date range selection
  const [startMonth, setStartMonth] = useState(() => {
    const now = new Date()
    return format(new Date(now.getFullYear(), now.getMonth() - 1, 1), 'yyyy-MM')
  })
  
  const [endMonth, setEndMonth] = useState(() => {
    return format(new Date(), 'yyyy-MM')
  })

  // Generate month options for the last 2 years and next 6 months
  // Cambiable segun requisitos
  const generateMonthOptions = () => {
    const options = []
    const currentDate = new Date()
    
    // Start from 2 years ago
    const startDate = new Date(currentDate.getFullYear() - 2, 0, 1)
    // End 6 months from now
    const endDate = new Date(currentDate.getFullYear(), currentDate.getMonth() + 6, 1)
    
    const currentMonth = new Date(startDate)
    
    while (currentMonth <= endDate) {
      const value = format(currentMonth, 'yyyy-MM')
      const label = format(currentMonth, 'MMMM yyyy', { locale: es })
        .replace(/^\w/, c => c.toUpperCase())
      
      options.push({ value, label })
      
      // Move to next month
      currentMonth.setMonth(currentMonth.getMonth() + 1)
    }
    
    return options.reverse() // Show newest first
  }

  const monthOptions = generateMonthOptions()

  // React Query for fetching report data
  const {
    data: reportData,
    isLoading,
    error,
    refetch
  } = useQuery<FeeTypeReportData[], Error>({
    queryKey: ['feeTypeReport', startMonth, endMonth],
    queryFn: () => reportsApi.getFeeTypeReport(startMonth, endMonth),
    enabled: startMonth !== '' && endMonth !== '',
    staleTime: 5 * 60 * 1000 // 5 minutes
  })

  // Handle error with useEffect
  useEffect(() => {
    if (error) {
      console.error('Error loading fee type report:', error)
      toast.error('Error al cargar el reporte de tarifas')
    }
  }, [error])

  // Handle refresh
  const handleRefresh = () => {
    refetch()
    toast.success('Reporte actualizado')
  }

  // Generate month columns dynamically
  const getMonthColumns = (): string[] => {
    if (!reportData || !Array.isArray(reportData) || reportData.length === 0) return []    
    const firstRow = reportData[0]
    return Object.keys(firstRow).filter(key => 
      key !== 'Descripcion' && 
      key !== 'Total' && 
      key !== 'Gran Total' &&
      key.match(/^\d{4}-\d{2}$/) // Match YYYY-MM format
    )
  }

  // Format currency
  const formatCurrency = (amount: number | string): string => {
    const num = typeof amount === 'string' ? parseFloat(amount) : amount
    return new Intl.NumberFormat('es-CL', {
      style: 'currency',
      currency: 'CLP',
      minimumFractionDigits: 0
    }).format(num || 0)
  }

  // Format month header
  const formatMonthHeader = (monthStr: string): string => {
    try {
      const [year, month] = monthStr.split('-')
      const date = new Date(parseInt(year), parseInt(month) - 1)
      return format(date, 'MMM yyyy', { locale: es }).replace(/^\w/, c => c.toUpperCase())
    } catch {
      return monthStr
    }
  }

  // Export to CSV
  const handleExport = () => {
    if (!reportData) return
    
    const csvContent = [
      // Header
      ['Descripción', ...getMonthColumns().map(formatMonthHeader), 'Total'].join(','),
      // Data rows
      ...((Array.isArray(reportData) ? reportData : [])).map(row => [
        `"${row.Descripcion}"`,
        ...getMonthColumns().map(month => row[month] || 0),
        row.Total || row['Gran Total'] || 0
      ].join(','))
    ].join('\n')
    
    const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' })
    const link = document.createElement('a')
    link.href = URL.createObjectURL(blob)
    link.download = `reporte_tarifas_${startMonth}_${endMonth}.csv`
    link.click()
    
    toast.success('Reporte exportado')
  }

  const monthColumns = getMonthColumns()
  const totalRow = Array.isArray(reportData) ? reportData.find(row => row.Descripcion === 'Total General') : undefined
  const dataRows = Array.isArray(reportData) ? reportData.filter(row => row.Descripcion !== 'Total General') : []

  return (
    <Box sx={{ p: 3 }}>
      {/* Header */}
      <Box sx={{ mb: 4 }}>
        <Typography variant="h3" component="h1" gutterBottom sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
          <ReportIcon sx={{ fontSize: 40 }} />
          Reporte de Tarifas
        </Typography>
        <Typography variant="h6" color="text.secondary">
          Análisis de ingresos por tipo de tarifa y período
        </Typography>
      </Box>

      {/* Controls */}
      <Card sx={{ mb: 3 }}>
        <CardContent>
          <Stack direction={{ xs: 'column', md: 'row' }} spacing={2} alignItems="end">
            <Box sx={{ flex: 1 }}>
              <Typography variant="subtitle2" gutterBottom>
                Período de reporte
              </Typography>
              <Stack direction="row" spacing={2}>
                <FormControl size="small" sx={{ minWidth: 180 }}>
                  <InputLabel>Mes inicial</InputLabel>
                  <Select
                    value={startMonth}
                    label="Mes inicial"
                    onChange={(e) => setStartMonth(e.target.value)}
                  >
                    {monthOptions.map((option) => (
                      <MenuItem key={option.value} value={option.value}>
                        {option.label}
                      </MenuItem>
                    ))}
                  </Select>
                </FormControl>
                
                <FormControl size="small" sx={{ minWidth: 180 }}>
                  <InputLabel>Mes final</InputLabel>
                  <Select
                    value={endMonth}
                    label="Mes final"
                    onChange={(e) => setEndMonth(e.target.value)}
                  >
                    {monthOptions
                      .filter(option => option.value >= startMonth) // Only show months >= start month
                      .map((option) => (
                        <MenuItem key={option.value} value={option.value}>
                          {option.label}
                        </MenuItem>
                      ))}
                  </Select>
                </FormControl>
              </Stack>
            </Box>

            <Stack direction="row" spacing={1}>
              <Tooltip title="Actualizar reporte">
                <IconButton onClick={handleRefresh} color="primary" disabled={isLoading}>
                  <RefreshIcon />
                </IconButton>
              </Tooltip>
              
              <Button
                variant="outlined"
                startIcon={<DownloadIcon />}
                onClick={handleExport}
                disabled={!reportData || isLoading}
                size="small"
              >
                Exportar CSV
              </Button>
            </Stack>
          </Stack>
        </CardContent>
      </Card>

      {/* Loading State */}
      {isLoading && (
        <Box sx={{ display: 'flex', justifyContent: 'center', py: 4 }}>
          <CircularProgress />
          <Typography variant="body1" sx={{ ml: 2 }}>
            Generando reporte...
          </Typography>
        </Box>
      )}

      {/* Error State */}
      {error && (
        <Alert severity="error" sx={{ mb: 3 }}>
          Error al cargar el reporte. Por favor, verifica las fechas e inténtalo de nuevo.
        </Alert>
      )}

      {/* Report Table */}
      {!isLoading && !error && reportData && (
        <>
          {/* Summary Cards */}
          <Box sx={{ mb: 3 }}>
            <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
              <Card sx={{ flex: 1 }}>
                <CardContent sx={{ textAlign: 'center' }}>
                  <MoneyIcon color="primary" sx={{ fontSize: 40, mb: 1 }} />
                  <Typography variant="h6" color="primary">
                    {formatCurrency(totalRow?.['Gran Total'] as number || 0)}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Total General
                  </Typography>
                </CardContent>
              </Card>
              
              <Card sx={{ flex: 1 }}>
                <CardContent sx={{ textAlign: 'center' }}>
                  <DateIcon color="primary" sx={{ fontSize: 40, mb: 1 }} />
                  <Typography variant="h6" color="primary">
                    {monthColumns.length}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Meses analizados
                  </Typography>
                </CardContent>
              </Card>
              
              <Card sx={{ flex: 1 }}>
                <CardContent sx={{ textAlign: 'center' }}>
                  <ReportIcon color="primary" sx={{ fontSize: 40, mb: 1 }} />
                  <Typography variant="h6" color="primary">
                    {dataRows.length}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Tipos de tarifa
                  </Typography>
                </CardContent>
              </Card>
            </Stack>
          </Box>

          {/* Main Report Table */}
          <Card>
            <CardContent sx={{ p: 0 }}>
              <TableContainer component={Paper} sx={{ maxHeight: 600 }}>
                <Table stickyHeader>
                  <TableHead>
                    <TableRow>
                      <TableCell sx={{ 
                        fontWeight: 'bold', 
                        backgroundColor: 'primary.main', 
                        color: 'primary.contrastText',
                        minWidth: 250
                      }}>
                        Descripción
                      </TableCell>
                      {monthColumns.map((month) => (
                        <TableCell
                          key={month}
                          align="right"
                          sx={{ 
                            fontWeight: 'bold', 
                            backgroundColor: 'primary.main', 
                            color: 'primary.contrastText',
                            minWidth: 120
                          }}
                        >
                          {formatMonthHeader(month)}
                        </TableCell>
                      ))}
                      <TableCell
                        align="right"
                        sx={{ 
                          fontWeight: 'bold', 
                          backgroundColor: 'primary.main', 
                          color: 'primary.contrastText',
                          minWidth: 120
                        }}
                      >
                        Total
                      </TableCell>
                    </TableRow>
                  </TableHead>
                  <TableBody>
                    {/* Data Rows */}
                    {dataRows.map((row, index) => (
                      <TableRow key={index} hover>
                        <TableCell sx={{ fontWeight: 'medium' }}>
                          {row.Descripcion}
                        </TableCell>
                        {monthColumns.map((month) => (
                          <TableCell key={month} align="right">
                            {formatCurrency(row[month] as number)}
                          </TableCell>
                        ))}
                        <TableCell align="right" sx={{ fontWeight: 'bold' }}>
                          {formatCurrency(row.Total as number)}
                        </TableCell>
                      </TableRow>
                    ))}
                    
                    {/* Total Row */}
                    {totalRow && (
                      <>
                        <TableRow>
                          <TableCell colSpan={monthColumns.length + 2}>
                            <Divider />
                          </TableCell>
                        </TableRow>
                        <TableRow sx={{ backgroundColor: 'grey.50' }}>
                          <TableCell sx={{ fontWeight: 'bold', fontSize: '1.1rem' }}>
                            {totalRow.Descripcion}
                          </TableCell>
                          {monthColumns.map((month) => (
                            <TableCell key={month} align="right" sx={{ fontWeight: 'bold', fontSize: '1.1rem' }}>
                              {formatCurrency(totalRow[month] as number)}
                            </TableCell>
                          ))}
                          <TableCell align="right" sx={{ fontWeight: 'bold', fontSize: '1.1rem', color: 'primary.main' }}>
                            {formatCurrency(totalRow['Gran Total'] as number)}
                          </TableCell>
                        </TableRow>
                      </>
                    )}
                  </TableBody>
                </Table>
              </TableContainer>
            </CardContent>
          </Card>

          {/* Report Info */}
          <Box sx={{ mt: 2, textAlign: 'center' }}>
            <Chip 
              icon={<DateIcon />}
              label={`Generado el ${format(new Date(), 'dd/MM/yyyy HH:mm', { locale: es })}`}
              variant="outlined"
              size="small"
            />
          </Box>
        </>
      )}
    </Box>
  )
}

export default ReporteTarifasPage