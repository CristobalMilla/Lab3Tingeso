import React, { useState, useEffect } from 'react'
import {
  Typography,
  Box,
  Card,
  CardContent,
  Button,
  Chip,
  CircularProgress,
  Alert,
  IconButton,
  Tooltip,
  Stack,
} from '@mui/material'
import {
  ChevronLeft as ChevronLeftIcon,
  ChevronRight as ChevronRightIcon,
  CalendarToday as CalendarIcon,
  AccessTime as TimeIcon,
  Person as PersonIcon,
  Refresh as RefreshIcon
} from '@mui/icons-material'
import { format, addWeeks, subWeeks, startOfWeek, addDays, parseISO } from 'date-fns'
import { es } from 'date-fns/locale'
import { useQuery } from '@tanstack/react-query'
import toast from 'react-hot-toast'

import { calendarApi } from '../services/calendarApi'
import type { CalendarEvent, DaySchedule, TimeSlot } from '../types/calendar'

const HorarioSemanalPage: React.FC = () => {
  const [currentWeek, setCurrentWeek] = useState(() => startOfWeek(new Date(), { weekStartsOn: 1 }))

  // React Query for fetching calendar events
  const {
    data: weeklyEvents,
    isLoading,
    error,
    refetch
  } = useQuery<Record<string, CalendarEvent[]>, Error>({
    queryKey: ['calendarEvents', format(currentWeek, 'yyyy-MM-dd')],
    queryFn: () => calendarApi.getEventsFromWeek(format(currentWeek, 'yyyy-MM-dd'), 1)
  })

  useEffect(() => {
    if (error) {
      toast.error('Error al cargar el horario semanal')
    }
  }, [error])

  // Generate time slots (10:00-22:00 on weekends, 14:00-22:00 on weekdays)
  const generateTimeSlots = (dayOfWeek: number): string[] => {
    const isWeekend = dayOfWeek === 0 || dayOfWeek === 6 // Sunday or Saturday
    const startHour = isWeekend ? 10 : 14
    const endHour = 21 // Changed from 22 to 21 (last slot at 21:30)
    
    const slots: string[] = []
    for (let hour = startHour; hour <= endHour; hour++) {
      slots.push(`${hour.toString().padStart(2, '0')}:00`)
      if (hour < endHour) {
        slots.push(`${hour.toString().padStart(2, '0')}:30`)
      }
    }
    // Add the final 21:30 slot
    slots.push('21:30')
    return slots
  }

  // Helper function to convert Java LocalDateTime array to Date
  const parseJavaLocalDateTime = (javaDateTime: number[]): Date => {
    if (Array.isArray(javaDateTime) && javaDateTime.length >= 3) {
      // Java LocalDateTime array format: [year, month, day, hour, minute, second?, nanosecond?]
      const [year, month, day, hour = 0, minute = 0, second = 0] = javaDateTime
      // Note: Java month is 1-based, JavaScript month is 0-based
      return new Date(year, month - 1, day, hour, minute, second)
    }
    throw new Error('Invalid Java LocalDateTime format')
  }

  // Helper function to handle different date formats
  const parseEventDate = (dateValue: unknown): Date => {
    if (typeof dateValue === 'string') {
      return parseISO(dateValue)
    } else if (dateValue instanceof Date) {
      return dateValue
    } else if (Array.isArray(dateValue)) {
      return parseJavaLocalDateTime(dateValue)
    } else if (dateValue && typeof dateValue === 'object') {
      return parseISO(String(dateValue))
    } else {
      throw new Error('Unknown date format')
    }
  }

  // Process events into daily schedule format
  const processDailySchedule = (): DaySchedule[] => {
    const schedule: DaySchedule[] = []
    
    for (let i = 0; i < 7; i++) {
      const currentDay = addDays(currentWeek, i)
      const dayOfWeek = currentDay.getDay()
      const dateStr = format(currentDay, 'yyyy-MM-dd')
      
      // Get events for this day
      const weekLabel = Object.keys(weeklyEvents || {})[0]
      const events = weeklyEvents?.[weekLabel] || []
      
      // Filter events for this day with better date handling
      const dayEvents = events.filter(event => {
        try {
          const eventDate = parseEventDate(event.start)
          return format(eventDate, 'yyyy-MM-dd') === dateStr
        } catch (error) {
          console.error('Error parsing event date:', event, error)
          return false
        }
      })

      // Generate time slots for this day
      const timeSlots = generateTimeSlots(dayOfWeek)
      const slotsWithEvents: TimeSlot[] = timeSlots.map(time => ({
        time,
        events: dayEvents.filter(event => {
          try {
            const eventStartDate = parseEventDate(event.start)
            const eventEndDate = parseEventDate(event.end)
            
            // Parse current time slot boundaries
            const [slotHour, slotMinute] = time.split(':').map(Number)
            const slotStart = new Date(eventStartDate.getFullYear(), eventStartDate.getMonth(), eventStartDate.getDate(), slotHour, slotMinute)
            const slotEnd = new Date(slotStart.getTime() + 30 * 60 * 1000) // Add 30 minutes
            
            // Check if event overlaps with this 30-minute time slot
            return (
              (eventStartDate >= slotStart && eventStartDate < slotEnd) || // Event starts in this slot
              (eventEndDate > slotStart && eventEndDate <= slotEnd) || // Event ends in this slot
              (eventStartDate <= slotStart && eventEndDate >= slotEnd) // Event spans this entire slot
            )
          } catch (error) {
            console.error('Error parsing event time for slot matching:', event, error)
            return false
          }
        })
      }))

      schedule.push({
        date: dateStr,
        dayName: format(currentDay, 'EEEE', { locale: es }),
        timeSlots: slotsWithEvents
      })
    }
    
    return schedule
  }

  const dailySchedule = processDailySchedule()

  // Navigation handlers
  const goToPreviousWeek = () => {
    setCurrentWeek(prev => subWeeks(prev, 1))
  }

  const goToNextWeek = () => {
    setCurrentWeek(prev => addWeeks(prev, 1))
  }

  const goToCurrentWeek = () => {
    setCurrentWeek(startOfWeek(new Date(), { weekStartsOn: 1 }))
  }

  // Event card component

  return (
    <Box sx={{ p: 3 }}> {/* Removed the viewport width overrides */}
      {/* Header */}
      <Box sx={{ mb: 4 }}>
        <Typography variant="h3" component="h1" gutterBottom sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
          <CalendarIcon sx={{ fontSize: 40 }} />
          Horario Semanal
        </Typography>
        <Typography variant="h6" color="text.secondary">
          Gestión de reservas y disponibilidad de la pista
        </Typography>
      </Box>

      {/* Week Navigation */}
      <Card sx={{ mb: 3 }}>
        <CardContent>
          <Stack direction="row" justifyContent="space-between" alignItems="center">
            <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
              <IconButton onClick={goToPreviousWeek} color="primary">
                <ChevronLeftIcon />
              </IconButton>
              
              <Box sx={{ textAlign: 'center', minWidth: 200 }}>
                <Typography variant="h5" component="h2">
                  {format(currentWeek, 'MMMM yyyy', { locale: es }).replace(/^\w/, c => c.toUpperCase())}
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  Semana del {format(currentWeek, 'd')} al {format(addDays(currentWeek, 6), 'd')}
                </Typography>
              </Box>

              <IconButton onClick={goToNextWeek} color="primary">
                <ChevronRightIcon />
              </IconButton>
            </Box>

            <Stack direction="row" spacing={1}>
              <Button
                variant="outlined"
                size="small"
                onClick={goToCurrentWeek}
                startIcon={<CalendarIcon />}
              >
                Semana Actual
              </Button>
              <Tooltip title="Actualizar">
                <IconButton onClick={() => refetch()} color="primary">
                  <RefreshIcon />
                </IconButton>
              </Tooltip>
            </Stack>
          </Stack>
        </CardContent>
      </Card>

      {/* Loading State */}
      {isLoading && (
        <Box sx={{ display: 'flex', justifyContent: 'center', py: 4 }}>
          <CircularProgress />
          <Typography variant="body1" sx={{ ml: 2 }}>
            Cargando horario semanal...
          </Typography>
        </Box>
      )}

      {/* Error State */}
      {error && (
        <Alert severity="error" sx={{ mb: 3 }}>
          Error al cargar el horario semanal. Por favor, inténtalo de nuevo.
        </Alert>
      )}

      {/* Weekly Schedule Grid - Contained Width */}
      {!isLoading && !error && (
        <Card sx={{ width: '100%', overflowX: 'auto' }}>
          <CardContent sx={{ p: 0 }}>
            {/* Days Header Row */}
            <Box sx={{ 
              display: 'flex', 
              borderBottom: 1, 
              borderColor: 'divider',
              backgroundColor: 'grey.50',
              minWidth: 800 // Ensure minimum table width
            }}>
              {/* Time column header */}
              <Box sx={{ 
                width: 100,
                p: 2, 
                borderRight: 1, 
                borderColor: 'divider',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                flexShrink: 0
              }}>
                <Typography variant="subtitle2" sx={{ fontWeight: 'bold' }}>
                  Hora
                </Typography>
              </Box>
              
              {/* Day headers */}
              {dailySchedule.map((day) => (
                <Box key={day.date} sx={{ 
                  width: 100, // Fixed width instead of flex
                  p: 2, 
                  textAlign: 'center',
                  borderRight: 1,
                  borderColor: 'divider',
                  '&:last-child': { borderRight: 0 },
                  flexShrink: 0
                }}>
                  <Typography variant="subtitle2" sx={{ fontWeight: 'bold', textTransform: 'capitalize' }}>
                    {day.dayName}
                  </Typography>
                  <Typography variant="caption" color="text.secondary">
                    {format(parseISO(day.date + 'T00:00:00'), 'd MMM', { locale: es })}
                  </Typography>
                </Box>
              ))}
            </Box>

            {/* Schedule Content with Single Vertical Scroll */}
            <Box sx={{ height: 600, overflowY: 'auto' }}>
              {/* Generate ALL possible time slots (10:00-21:30) */}
              {(() => {
                // Generate all possible time slots from 10:00 to 21:30
                const allTimeSlots: string[] = []
                for (let hour = 10; hour <= 21; hour++) {
                  allTimeSlots.push(`${hour.toString().padStart(2, '0')}:00`)
                  if (hour < 21) {
                    allTimeSlots.push(`${hour.toString().padStart(2, '0')}:30`)
                  }
                }
                allTimeSlots.push('21:30')
                
                return allTimeSlots.map((timeSlot) => (
                  <Box key={timeSlot} sx={{ 
                    display: 'flex',
                    borderBottom: 1,
                    borderColor: 'divider',
                    minHeight: 60,
                    minWidth: 800, // Match header width
                    '&:hover': {
                      backgroundColor: 'grey.25'
                    }
                  }}>
                    {/* Time column */}
                    <Box sx={{ 
                      width: 100,
                      p: 1,
                      borderRight: 1, 
                      borderColor: 'divider',
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center',
                      backgroundColor: 'grey.50',
                      flexShrink: 0
                    }}>
                      <Typography variant="body2" sx={{ fontWeight: 'medium' }}>
                        {timeSlot}
                      </Typography>
                    </Box>

                    {/* Day columns */}
                    {dailySchedule.map((day) => {
                      const currentDayDate = parseISO(day.date + 'T00:00:00')
                      const dayOfWeek = currentDayDate.getDay()
                      const isWeekend = dayOfWeek === 0 || dayOfWeek === 6 // Sunday or Saturday
                      
                      // Check if this time slot should be available for this day
                      const timeHour = parseInt(timeSlot.split(':')[0])
                      const shouldShowSlot = isWeekend ? timeHour >= 10 : timeHour >= 14
                      
                      // Find events for this time slot
                      const dayTimeSlot = day.timeSlots.find(slot => slot.time === timeSlot)
                      const events = dayTimeSlot?.events || []
                      
                      return (
                        <Box key={day.date} sx={{ 
                          width: 100, // Fixed width instead of flex
                          p: 1,
                          borderRight: 1,
                          borderColor: 'divider',
                          minHeight: 58,
                          flexShrink: 0,
                          '&:last-child': { borderRight: 0 }
                        }}>
                          {!shouldShowSlot ? (
                            // Show closed/unavailable for weekdays before 14:00
                            <Box sx={{ 
                              height: '100%',
                              display: 'flex',
                              alignItems: 'center',
                              justifyContent: 'center',
                              minHeight: 50,
                              backgroundColor: 'grey.100'
                            }}>
                              <Typography variant="caption" color="text.disabled" sx={{ fontSize: '0.75rem' }}> {/* Changed from 0.65rem */}
                                Cerrado
                              </Typography>
                            </Box>
                          ) : events.length > 0 ? (
                            // Show events (keep existing styling)
                            events.map((event, eventIndex) => (
                              <Box
                                key={eventIndex}
                                sx={{
                                  backgroundColor: 'primary.light',
                                  color: 'primary.contrastText',
                                  borderRadius: 1,
                                  p: 0.5,
                                  mb: events.length > 1 ? 0.5 : 0,
                                  minHeight: 50
                                }}
                              >
                                <Typography variant="caption" display="block" sx={{ fontWeight: 'bold', mb: 0.25, fontSize: '0.65rem' }}> {/* Keep existing size */}
                                  {event.title}
                                </Typography>
                                <Box sx={{ display: 'flex', alignItems: 'center', gap: 0.25, mb: 0.25 }}>
                                  <PersonIcon sx={{ fontSize: 10 }} />
                                  <Typography variant="caption" sx={{ fontSize: '0.6rem' }}> {/* Slightly increased from 0.55rem */}
                                    {event.clientName}
                                  </Typography>
                                </Box>
                                <Box sx={{ display: 'flex', alignItems: 'center', gap: 0.25 }}>
                                  <TimeIcon sx={{ fontSize: 10 }} />
                                  <Typography variant="caption" sx={{ fontSize: '0.6rem' }}>
                                    {(() => {
                                      try {
                                        const startDate = parseEventDate(event.start)
                                        const endDate = parseEventDate(event.end)
                                        
                                        return `${format(startDate, 'HH:mm')} - ${format(endDate, 'HH:mm')}`
                                      } catch (error) {
                                        console.error('Error formatting event time:', event, error)
                                        return 'Hora no disponible'
                                      }
                                    })()}
                                  </Typography>
                                </Box>
                              </Box>
                            ))
                          ) : (
                            // Show available
                            <Box sx={{ 
                              height: '100%',
                              display: 'flex',
                              alignItems: 'center',
                              justifyContent: 'center',
                              minHeight: 50
                            }}>
                              <Chip 
                                label="Disponible" 
                                size="small" 
                                color="success" 
                                variant="outlined"
                                sx={{ fontSize: '0.7rem' }}
                              />
                            </Box>
                          )}
                        </Box>
                      );
                    })}
                  </Box>
                ));
              })()}
            </Box>
          </CardContent>
        </Card>
      )}

      {/* Summary */}
      {!isLoading && !error && weeklyEvents && (
        <Card sx={{ mt: 3 }}>
          <CardContent>
            <Typography variant="h6" gutterBottom>
              Resumen de la Semana
            </Typography>
            <Stack direction="row" spacing={2}>
              <Chip 
                icon={<CalendarIcon />}
                label={`${Object.values(weeklyEvents)[0]?.length || 0} reservas`}
                color="primary"
              />
              <Chip 
                icon={<TimeIcon />}
                label="Horarios: L-V 14:00-21:30, S-D 10:00-21:30"
                variant="outlined"
              />
            </Stack>
          </CardContent>
        </Card>
      )}
    </Box>
  )
}

export default HorarioSemanalPage