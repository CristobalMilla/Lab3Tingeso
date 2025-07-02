// This file defines TypeScript interfaces for calendar-related data structures.
export interface CalendarEvent {
  title: string
  start: string | Date | number[] // Allow Java LocalDateTime array format
  end: string | Date | number[] // Allow Java LocalDateTime array format
  clientName: string
}

export interface WeeklyEventsResponse {
  [weekLabel: string]: CalendarEvent[]
}

export interface TimeSlot {
  time: string
  events: CalendarEvent[]
}

export interface DaySchedule {
  date: string
  dayName: string
  timeSlots: TimeSlot[]
}