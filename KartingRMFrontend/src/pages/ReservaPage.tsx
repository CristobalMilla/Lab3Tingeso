import React, { useState } from 'react'
import {
  Typography,
  Box,
  Card,
  CardContent,
  Button,
  Stepper,
  Step,
  StepLabel,
  TextField,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Alert,
  Chip,
  Stack,
  Paper,
  Divider,
  CircularProgress,
  Skeleton
} from '@mui/material'
import {
  Schedule as ScheduleIcon,
  Person as PersonIcon,
  Group as GroupIcon,
  Receipt as ReceiptIcon,
  CheckCircle as CheckIcon,
  ArrowBack as BackIcon,
  ArrowForward as ForwardIcon,
  AttachMoney as MoneyIcon
} from '@mui/icons-material'
import { DatePicker } from '@mui/x-date-pickers/DatePicker'
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider'
import { AdapterDateFns } from '@mui/x-date-pickers/AdapterDateFns'
import { format, isBefore, startOfDay } from 'date-fns'
import { es } from 'date-fns/locale'
import { useQuery, useMutation } from '@tanstack/react-query'
import toast from 'react-hot-toast'

import { reservaApi } from '../services/reservaApi'
import type { FeeType, RentEntity, RentRequest, RentPreviewDTO } from '../types/reserva'

// Steps configuration
const steps = [
  'Seleccionar Tarifa',
  'Fecha y Hora', 
  'Información del Cliente',
  'Participantes',
  'Resumen y Confirmación',
  'Reserva Completada'
]

const ReservaPage: React.FC = () => {
  const [activeStep, setActiveStep] = useState(0)
  
  // Form state
  const [selectedFeeType, setSelectedFeeType] = useState<FeeType | null>(null)
  const [selectedDate, setSelectedDate] = useState<Date | null>(null)
  const [selectedTime, setSelectedTime] = useState<string>('')
  const [peopleNumber, setPeopleNumber] = useState<number>(1)
  const [mainClient, setMainClient] = useState<string>('')
  const [subClients, setSubClients] = useState<string[]>([''])
  const [previewData, setPreviewData] = useState<RentPreviewDTO | null>(null)

  // Fetch fee types
  const { data: feeTypes, isLoading: loadingFeeTypes } = useQuery<FeeType[]>({
    queryKey: ['feeTypes'],
    queryFn: reservaApi.getFeeTypes,
    staleTime: 10 * 60 * 1000 // 10 minutes
  })

  // Fetch available time slots
  const { data: availableSlots, isLoading: loadingSlots } = useQuery<string[]>({
    queryKey: ['availableSlots', selectedDate, selectedFeeType?.duration],
    queryFn: () => {
      if (!selectedDate || !selectedFeeType) return Promise.resolve([])
      return reservaApi.getAvailableTimeSlots(
        format(selectedDate, 'yyyy-MM-dd'),
        selectedFeeType.duration
      )
    },
    enabled: !!selectedDate && !!selectedFeeType
  })

  // Calculate preview mutation
  const calculatePreviewMutation = useMutation({
    mutationFn: reservaApi.calculatePreview,
    onSuccess: (data) => {
      setPreviewData(data)
      setActiveStep(4) // Go to confirmation step
      toast.success('Precios calculados exitosamente')
    },
    onError: () => {
      toast.error('Error al calcular los precios')
    }
  })

  // Save reservation mutation
  const saveReservationMutation = useMutation({
    mutationFn: reservaApi.saveFromPreview,
    onSuccess: () => {
      toast.success('¡Reserva creada exitosamente!')
      setActiveStep(5) // Go to success step
    },
    onError: () => {
      toast.error('Error al crear la reserva')
    }
  })

  // Handler functions
  const handlePeopleNumberChange = (newPeopleNumber: number) => {
    setPeopleNumber(newPeopleNumber)
    
    const newSubClients = Array(newPeopleNumber).fill('').map((_, index) => {
      if (index === 0) {
        return mainClient
      }
      return subClients[index] || ''
    })
    setSubClients(newSubClients)
  }

  const handleMainClientChange = (newMainClient: string) => {
    setMainClient(newMainClient)
    
    if (subClients.length > 0) {
      const newSubClients = [...subClients]
      newSubClients[0] = newMainClient
      setSubClients(newSubClients)
    }
  }

  // Helper functions
  const formatDateForDisplay = (dateInput: string | number[]): string => {
    try {
      let year, month, day;
      if (typeof dateInput === 'string') {
        const parts = dateInput.split('-').map(num => parseInt(num, 10));
        if (parts.length !== 3) return 'No disponible';
        [year, month, day] = parts;
      } else if (Array.isArray(dateInput) && dateInput.length === 3) {
        [year, month, day] = dateInput;
      } else {
        return 'No disponible';
      }
      
      const date = new Date(year, month - 1, day); // month is 0-based in JS
      
      if (isNaN(date.getTime())) return 'Fecha inválida';
      
      return format(date, 'dd/MM/yyyy', { locale: es });
    } catch {
      return 'Error al formatear fecha';
    }
  }

  const formatTimeForDisplay = (timeInput: string | number[]): string => {
      try {
        let hour: number, minute: number;
        if (typeof timeInput === 'string') {
          const parts = timeInput.split(':').map(num => parseInt(num, 10));
          if (parts.length < 2) return 'No disponible';
          [hour, minute] = parts;
        } else if (Array.isArray(timeInput) && timeInput.length >= 2) {
          [hour, minute] = timeInput;
        } else {
          return 'No disponible';
        }
  
        return `${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}`;
      } catch {
        return 'Error al formatear hora';
      }
    }

  // Generate rent code
  const generateRentCode = (): string => {
    if (!mainClient || !selectedDate) return ''
    const dateStr = format(selectedDate, 'yyyyMMdd')
    const clientName = mainClient.replace(/\s+/g, '').toLowerCase()
    return `${clientName}${dateStr}`
  }

  // Navigation functions
  const handleNext = () => {
    if (validateCurrentStep()) {
      if (activeStep === 3) {
        // After subclient step, calculate preview
        handleCalculatePreview()
      } else {
        setActiveStep(prev => prev + 1)
      }
    }
  }

  const handleBack = () => {
    setActiveStep(prev => prev - 1)
  }

  const validateCurrentStep = (): boolean => {
    switch (activeStep) {
      case 0: // Fee type selection
        return !!selectedFeeType
      case 1: // Date and time
        return !!selectedDate && !!selectedTime
      case 2: // Client info
        return peopleNumber >= 1 && peopleNumber <= 15 && mainClient.trim().length >= 2
      case 3: // Subclients
        return subClients.every(client => client.trim().length >= 2)
      case 4: // Confirmation
        return !!previewData
      default:
        return true
    }
  }

  const handleCalculatePreview = async () => {
    if (!selectedFeeType || !selectedDate) return

    const rentData: RentEntity = {
      rentDate: format(selectedDate, 'yyyy-MM-dd'),
      rentTime: selectedTime,
      feeTypeId: selectedFeeType.feeTypeId,
      peopleNumber,
      mainClient: mainClient.trim(),
      rentCode: generateRentCode()
    }

    const request: RentRequest = {
      rent: rentData,
      subClients: subClients.map(client => client.trim())
    }

    calculatePreviewMutation.mutate(request)
  }

  const handleConfirmReservation = async () => {
    if (previewData) {
      saveReservationMutation.mutate(previewData)
    }
  }

  // Format currency
  const formatCurrency = (amount: number): string => {
    return new Intl.NumberFormat('es-CL', {
      style: 'currency',
      currency: 'CLP',
      minimumFractionDigits: 0
    }).format(amount)
  }

  const renderStepContent = () => {
    switch (activeStep) {
      case 0: // Fee Type Selection
        return (
          <Box>
            <Typography variant="h6" gutterBottom sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 3 }}>
              <MoneyIcon /> Selecciona el tipo de tarifa
            </Typography>
            
            {loadingFeeTypes ? (
              <Stack spacing={2}>
                {[1, 2, 3].map((i) => (
                  <Skeleton key={i} variant="rectangular" height={120} />
                ))}
              </Stack>
            ) : (
              <Stack spacing={2}>
                {feeTypes?.map((feeType) => (
                  <Card 
                    key={feeType.feeTypeId}
                    sx={{ 
                      cursor: 'pointer',
                      border: selectedFeeType?.feeTypeId === feeType.feeTypeId ? 2 : 1,
                      borderColor: selectedFeeType?.feeTypeId === feeType.feeTypeId ? 'primary.main' : 'grey.300',
                      '&:hover': {
                        boxShadow: 3
                      }
                    }}
                    onClick={() => setSelectedFeeType(feeType)}
                  >
                    <CardContent>
                      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                        <Box>
                          <Typography variant="h5" color="primary" gutterBottom>
                            {formatCurrency(feeType.price)}
                          </Typography>
                          <Stack direction="row" spacing={3}>
                            <Typography variant="body2" color="text.secondary">
                              🏁 {feeType.lapNumber} vueltas
                            </Typography>
                            <Typography variant="body2" color="text.secondary">
                              ⏱️ Máximo {feeType.maxTime} minutos
                            </Typography>
                            <Typography variant="body2" color="text.secondary">
                              🕐 Duración: {feeType.duration} min
                            </Typography>
                          </Stack>
                        </Box>
                        
                        {selectedFeeType?.feeTypeId === feeType.feeTypeId && (
                          <Chip 
                            label="Seleccionado" 
                            color="primary" 
                            size="small"
                          />
                        )}
                      </Box>
                    </CardContent>
                  </Card>
                ))}
              </Stack>
            )}
          </Box>
        )

      case 1: // Date and Time Selection
        return (
          <Box>
            <Typography variant="h6" gutterBottom sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 3 }}>
              <ScheduleIcon /> Selecciona fecha y hora
            </Typography>
            
            <Stack spacing={3}>
              <Box>
                <LocalizationProvider dateAdapter={AdapterDateFns} adapterLocale={es}>
                  <DatePicker
                    label="Fecha de la reserva"
                    value={selectedDate}
                    onChange={(date) => {
                      setSelectedDate(date)
                      setSelectedTime('') // Reset time when date changes
                    }}
                    shouldDisableDate={(date) => isBefore(date, startOfDay(new Date()))}
                    slotProps={{ 
                      textField: { 
                        fullWidth: true,
                        helperText: 'Solo fechas futuras',
                        // Make the entire input clickable
                        onClick: (event) => {
                          // Prevent default and trigger the DatePicker to open
                          event.preventDefault()
                          const input = event.currentTarget
                          const datePicker = input.closest('.MuiFormControl-root')
                          const calendarButton = datePicker?.querySelector('[aria-label="Choose date"]')
                          if (calendarButton) {
                            (calendarButton as HTMLElement).click()
                          }
                        },
                        sx: {
                          '& .MuiInputBase-input': {
                            cursor: 'pointer', // Show pointer cursor on hover
                          },
                          '& .MuiOutlinedInput-root': {
                            cursor: 'pointer', // Show pointer cursor on the entire input
                          }
                        }
                      } 
                    }}
                  />
                </LocalizationProvider>
              </Box>
              
              {selectedDate && (
                <Box>
                  <FormControl fullWidth>
                    <InputLabel>Hora disponible</InputLabel>
                    <Select
                      value={selectedTime}
                      onChange={(e) => setSelectedTime(e.target.value)}
                      disabled={loadingSlots}
                    >
                      {loadingSlots ? (
                        <MenuItem disabled>Cargando horarios...</MenuItem>
                      ) : availableSlots?.length === 0 ? (
                        <MenuItem disabled>No hay horarios disponibles</MenuItem>
                      ) : (
                        availableSlots?.map((time) => (
                          <MenuItem key={time} value={time}>
                            {time} - {(() => {
                              const [hour, minute] = time.split(':').map(Number)
                              const endTime = new Date()
                              endTime.setHours(hour, minute + (selectedFeeType?.duration || 0))
                              return format(endTime, 'HH:mm')
                            })()} ({selectedFeeType?.duration} min)
                          </MenuItem>
                        ))
                      )}
                    </Select>
                  </FormControl>
                </Box>
              )}
            </Stack>

            {selectedDate && selectedFeeType && (
              <Alert severity="info" sx={{ mt: 3 }}>
                <strong>Tarifa seleccionada:</strong> {selectedFeeType.lapNumber} vueltas, 
                duración de {selectedFeeType.duration} minutos - {formatCurrency(selectedFeeType.price)}
              </Alert>
            )}
          </Box>
        )

      case 2: // Client Information  
        return (
          <Box>
            <Typography variant="h6" gutterBottom sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 3 }}>
              <PersonIcon /> Información del cliente principal
            </Typography>
            
            <Stack spacing={3}>
              <TextField
                fullWidth
                label="Nombre del cliente principal"
                value={mainClient}
                onChange={(e) => handleMainClientChange(e.target.value)}
                placeholder="Nombre y apellido"
                helperText="Mínimo 2 caracteres. Este será el responsable de la reserva."
              />
              
              <TextField
                fullWidth
                type="number"
                label="Número de personas"
                value={peopleNumber}
                onChange={(e) => handlePeopleNumberChange(Math.max(1, Math.min(15, parseInt(e.target.value) || 1)))}
                inputProps={{ min: 1, max: 15 }}
                helperText="Entre 1 y 15 personas"
                sx={{ maxWidth: 300 }}
              />
            </Stack>

            <Alert severity="info" sx={{ mt: 3 }}>
              El cliente principal será responsable del pago y recibirá la confirmación de la reserva.
            </Alert>
          </Box>
        )

      case 3: // Subclient Names
        return (
          <Box>
            <Typography variant="h6" gutterBottom sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 1 }}>
              <GroupIcon /> Nombres de los participantes ({peopleNumber})
            </Typography>
            <Typography variant="body2" color="text.secondary" sx={{ mb: 3 }}>
              Ingresa el nombre completo de cada participante. Todos los campos son obligatorios.
            </Typography>
            
            <Stack spacing={2}>
              {subClients.map((client, index) => (
                <TextField
                  key={index}
                  fullWidth
                  label={`Participante ${index + 1}${index === 0 ? ' (Principal)' : ''}`}
                  value={client}
                  onChange={(e) => {
                    const newSubClients = [...subClients]
                    newSubClients[index] = e.target.value
                    setSubClients(newSubClients)
                  }}
                  placeholder="Nombre y apellido"
                  helperText={index === 0 ? 'Cliente responsable de la reserva' : 'Mínimo 2 caracteres'}
                  color={index === 0 ? 'primary' : 'secondary'}
                />
              ))}
            </Stack>

            <Alert severity="warning" sx={{ mt: 3 }}>
              Verifica que todos los nombres estén correctos. Esta información se usará para los recibos individuales.
            </Alert>
          </Box>
        )

      case 4: // Confirmation and Preview
        return (
          <Box>
            <Typography variant="h6" gutterBottom sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 3 }}>
              <ReceiptIcon /> Resumen de la reserva
            </Typography>

            {calculatePreviewMutation.isPending && (
              <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, mb: 3 }}>
                <CircularProgress size={24} />
                <Typography>Calculando precios...</Typography>
              </Box>
            )}

            {previewData && (
              <Stack spacing={3}>
                {/* Reservation Details */}
                <Paper sx={{ p: 3 }}>
                  <Typography variant="h6" gutterBottom>
                    Detalles de la Reserva
                  </Typography>
                  <Stack spacing={2}>
                    <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                      <Typography variant="subtitle2" color="text.secondary">
                        Tipo de tarifa:
                      </Typography>
                      <Typography variant="body1">
                        {selectedFeeType?.lapNumber} vueltas - {selectedFeeType?.duration} min
                      </Typography>
                    </Box>
                    
                    <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                      <Typography variant="subtitle2" color="text.secondary">
                        Fecha y hora:
                      </Typography>
                      <Typography variant="body1">
                        {selectedDate && format(selectedDate, 'dd/MM/yyyy', { locale: es })} a las {selectedTime}
                      </Typography>
                    </Box>
                    
                    <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                      <Typography variant="subtitle2" color="text.secondary">
                        Cliente principal:
                      </Typography>
                      <Typography variant="body1">{mainClient}</Typography>
                    </Box>
                    
                    <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                      <Typography variant="subtitle2" color="text.secondary">
                        Código de reserva:
                      </Typography>
                      <Typography variant="body1" sx={{ fontFamily: 'monospace' }}>
                        {previewData.rent.rentCode}
                      </Typography>
                    </Box>
                  </Stack>
                </Paper>

                {/* Pricing Breakdown */}
                <Paper sx={{ p: 3 }}>
                  <Typography variant="h6" gutterBottom>
                    Desglose de Precios
                  </Typography>
                  <Stack spacing={2}>
                    <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                      <Typography variant="subtitle2" color="text.secondary">
                        Precio total de la tarifa:
                      </Typography>
                      <Typography variant="body1">
                        {formatCurrency(selectedFeeType?.price || 0)}
                      </Typography>
                    </Box>
                    
                    <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                      <Typography variant="subtitle2" color="text.secondary">
                        Precio base por persona ({peopleNumber} personas):
                      </Typography>
                      <Typography variant="body1">
                        {formatCurrency(previewData.receipts[0]?.baseTariff || 0)}
                      </Typography>
                    </Box>
                    
                    <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                      <Typography variant="subtitle2" color="text.secondary">
                        Descuento por grupo:
                      </Typography>
                      <Typography variant="body1">
                        {((1 - (previewData.receipts[0]?.sizeDiscount || 1)) * 100).toFixed(0)}%
                      </Typography>
                    </Box>
                    
                    <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                      <Typography variant="subtitle2" color="text.secondary">
                        Descuento especial:
                      </Typography>
                      <Typography variant="body1">
                        {((1 - (previewData.receipts[0]?.specialDiscount || 1)) * 100).toFixed(0)}%
                      </Typography>
                    </Box>
                    
                    <Divider />
                    
                    <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                      <Typography variant="subtitle2" color="text.secondary">
                        Subtotal por persona:
                      </Typography>
                      <Typography variant="body1">
                        {formatCurrency(previewData.receipts[0]?.aggregatedPrice || 0)}
                      </Typography>
                    </Box>
                    
                    <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                      <Typography variant="subtitle2" color="text.secondary">
                        IVA por persona (19%):
                      </Typography>
                      <Typography variant="body1">
                        {formatCurrency(previewData.receipts[0]?.ivaPrice || 0)}
                      </Typography>
                    </Box>
                    
                    <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                      <Typography variant="subtitle2" color="text.secondary">
                        Total por persona:
                      </Typography>
                      <Typography variant="body1" sx={{ fontWeight: 'bold' }}>
                        {formatCurrency(previewData.receipts[0]?.finalPrice || 0)}
                      </Typography>
                    </Box>
                  </Stack>
                </Paper>

                {/* Participants and Individual Prices */}
                <Paper sx={{ p: 3 }}>
                  <Typography variant="h6" gutterBottom>
                    Participantes y Precios Individuales
                  </Typography>
                  <Stack spacing={1}>
                    {previewData.receipts.map((receipt, index) => (
                      <Box key={index}>
                        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', py: 1 }}>
                          <Typography variant="body1">
                            {receipt.subClientName}
                          </Typography>
                          <Typography variant="body1" sx={{ fontWeight: 'bold' }}>
                            {formatCurrency(receipt.finalPrice || 0)}
                          </Typography>
                        </Box>
                        {index < previewData.receipts.length - 1 && <Divider />}
                      </Box>
                    ))}
                  </Stack>
                </Paper>

                {/* Total */}
                <Paper sx={{ p: 3, bgcolor: 'primary.main', color: 'primary.contrastText' }}>
                  <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                    <Typography variant="h6">
                      Total de la Reserva
                    </Typography>
                    <Typography variant="h4" sx={{ fontWeight: 'bold' }}>
                      {formatCurrency(previewData.rent.totalPrice || 0)}
                    </Typography>
                  </Box>
                </Paper>

                <Alert severity="info">
                  Al confirmar, se procesará la reserva y se generarán los recibos individuales.
                </Alert>
              </Stack>
            )}
          </Box>
        )

      case 5: // Success
        return (
          <Box textAlign="center">
            <CheckIcon sx={{ fontSize: 80, color: 'success.main', mb: 2 }} />
            <Typography variant="h4" gutterBottom>
              ¡Reserva Completada!
            </Typography>
            <Typography variant="body1" color="text.secondary" sx={{ mb: 3 }}>
              Tu reserva ha sido procesada exitosamente.
            </Typography>
            
            {saveReservationMutation.data && (
              <Paper sx={{ p: 3, maxWidth: 500, mx: 'auto', mb: 3 }}>
                <Typography variant="h6" gutterBottom>
                  Detalles de la reserva confirmada
                </Typography>
                <Stack spacing={1}>
                  <Typography variant="body2">
                    <strong>Código:</strong> {saveReservationMutation.data.rentCode}
                  </Typography>
                  <Typography variant="body2">
                    <strong>Cliente:</strong> {saveReservationMutation.data.mainClient}
                  </Typography>
                  <Typography variant="body2">
                    <strong>Fecha:</strong> {formatDateForDisplay(saveReservationMutation.data.rentDate)}
                  </Typography>
                  <Typography variant="body2">
                    <strong>Hora:</strong> {formatTimeForDisplay(saveReservationMutation.data.rentTime)}
                  </Typography>
                  <Typography variant="h6" color="primary" sx={{ mt: 2 }}>
                    Total: {formatCurrency(saveReservationMutation.data.totalPrice || 0)}
                  </Typography>
                </Stack>
              </Paper>
            )}

            <Button 
              variant="contained" 
              onClick={() => {
                // Reset form
                setActiveStep(0)
                setSelectedFeeType(null)
                setSelectedDate(null)
                setSelectedTime('')
                setPeopleNumber(1)
                setMainClient('')
                setSubClients([''])
                setPreviewData(null)
              }}
            >
              Hacer Nueva Reserva
            </Button>
          </Box>
        )

      default:
        return null
    }
  }

  return (
    <LocalizationProvider dateAdapter={AdapterDateFns} adapterLocale={es}>
      <Box sx={{ p: 3 }}>
        {/* Header */}
        <Typography variant="h3" component="h1" gutterBottom sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
          <ScheduleIcon sx={{ fontSize: 40 }} />
          Nueva Reserva
        </Typography>

        {/* Stepper */}
        <Card sx={{ mb: 4 }}>
          <CardContent>
            <Stepper activeStep={activeStep} alternativeLabel>
              {steps.map((label) => (
                <Step key={label}>
                  <StepLabel>{label}</StepLabel>
                </Step>
              ))}
            </Stepper>
          </CardContent>
        </Card>

        {/* Step Content */}
        <Card>
          <CardContent sx={{ minHeight: 500 }}>
            {renderStepContent()}
          </CardContent>
        </Card>

        {/* Navigation */}
        {activeStep < 5 && (
          <Box sx={{ display: 'flex', justifyContent: 'space-between', mt: 3 }}>
            <Button
              onClick={handleBack}
              disabled={activeStep === 0}
              startIcon={<BackIcon />}
            >
              Anterior
            </Button>

            {activeStep === 4 ? (
              <Button
                variant="contained"
                onClick={handleConfirmReservation}
                disabled={!previewData || saveReservationMutation.isPending}
                startIcon={saveReservationMutation.isPending ? <CircularProgress size={20} /> : <CheckIcon />}
              >
                {saveReservationMutation.isPending ? 'Procesando...' : 'Confirmar Reserva'}
              </Button>
            ) : (
              <Button
                variant="contained"
                onClick={handleNext}
                disabled={!validateCurrentStep() || (activeStep === 3 && calculatePreviewMutation.isPending)}
                endIcon={activeStep === 3 && calculatePreviewMutation.isPending ? <CircularProgress size={20} /> : <ForwardIcon />}
              >
                {activeStep === 3 ? 'Calcular Precios' : 'Siguiente'}
              </Button>
            )}
          </Box>
        )}
      </Box>
    </LocalizationProvider>
  )
}

export default ReservaPage