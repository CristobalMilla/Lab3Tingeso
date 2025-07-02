// src/services/calendarApi.ts
import type { WeeklyEventsResponse } from '../types/calendar'

import { api } from './api'

export const calendarApi = {
  getEventsFromWeek: async (startDate: string, numberOfWeeks: number): Promise<WeeklyEventsResponse> => {
    const response = await api.get<WeeklyEventsResponse>('/calendar/getEventsFromWeek', {
      params: {
        startDate,
        numberOfWeeks
      }
    })
    return response.data
  }
}