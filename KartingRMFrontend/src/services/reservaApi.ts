import type { RentEntity, RentRequest, RentPreviewDTO, FeeType } from '../types/reserva'

import { api } from './api'

export const reservaApi = {
  // Get all fee types
  getFeeTypes: async (): Promise<FeeType[]> => {
    const response = await api.get('/feeType/all')
    return response.data
  },

  // Get available time slots for a specific date and duration
  getAvailableTimeSlots: async (rentDate: string, rentDuration: number): Promise<string[]> => {
    const response = await api.get('/rent/getAvailableTimeSlots', {
      params: {
        rentDate,
        rentDuration
      }
    })
    
    // Convert Java LocalTime array [[14,0],[14,15],...] to string array ["14:00","14:15",...]
    return response.data.map((timeArray: number[]) => {
      const [hour, minute] = timeArray
      return `${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}`
    })
  },

  // Calculate reservation preview (prices without saving)
  calculatePreview: async (request: RentRequest): Promise<RentPreviewDTO> => {
    const response = await api.post('/receipt/calculatePreview', request)
    return response.data
  },

  // Save reservation from preview data
  saveFromPreview: async (preview: RentPreviewDTO): Promise<RentEntity> => {
    const response = await api.post('/receipt/saveFromPreview', preview)
    return response.data
  },

  // Get rent by ID (for confirmation page)
  getRentById: async (id: number): Promise<RentEntity> => {
    const response = await api.get(`/rent/${id}`)
    return response.data
  }
}