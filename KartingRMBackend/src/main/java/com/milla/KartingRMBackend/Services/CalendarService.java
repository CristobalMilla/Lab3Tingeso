package com.milla.KartingRMBackend.Services;

import com.milla.KartingRMBackend.DTO.CalendarEvent;
import com.milla.KartingRMBackend.Entities.RentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class CalendarService {
    @Autowired
    private RentService rentService;


    //Funcion que recupera las rentas de la semana especificada
    public List<RentEntity> getRentsForWeek(LocalDate weekStartDate) {
        LocalDate weekEndDate = weekStartDate.plusDays(6); // Sunday-Saturday week

        List<RentEntity> rents = rentService.getRentsBetweenDates(weekStartDate, weekEndDate);
        return rents != null ? rents : new ArrayList<>();
    }
    //Funcion que convierte listas de rentas a listas de eventos, definidos en el DTO
    public List<CalendarEvent> convertToCalendarEvents(List<RentEntity> rents) {
        List<CalendarEvent> events = new ArrayList<>();

        for (RentEntity rent : rents) {
            CalendarEvent event = new CalendarEvent();

            // Fetch duration in minutes for the current rent
            int duration = rentService.getDurationByRentId(rent.getRentId());

            // Calculate end time
            LocalTime startTime = rent.getRentTime();
            LocalTime endTime = startTime.plusMinutes(duration);

            // Set event properties
            event.setTitle("Rent: " + rent.getRentCode());
            LocalDateTime start = LocalDateTime.of(rent.getRentDate(), startTime);
            event.setStart(start);
            LocalDateTime end = LocalDateTime.of(rent.getRentDate(), endTime);
            event.setEnd(end);
            event.setClientName(rent.getMainClient());

            // Add to events list
            events.add(event);
        }
        return events;
    }
    //Funcion que, utilizando las anteriores, obtiene una lista de eventos, segun una fecha inicial y un numero de semanas a partir de esa fecha
    //Retorna en formato mapeado para las semanas, lo que se envia a frontend
    public Map<String, List<CalendarEvent>> getCalendarEventsForWeeks(LocalDate startDate, int numberOfWeeks) {
        Map<String, List<CalendarEvent>> weeklyEventsMap = new LinkedHashMap<>();

        LocalDate currentWeekStart = startDate;
        for (int i = 0; i < numberOfWeeks; i++) {
            // Fetch rents for the current week
            List<RentEntity> rentsForWeek = getRentsForWeek(currentWeekStart);

            // Convert rents to calendar events
            List<CalendarEvent> eventsForWeek = convertToCalendarEvents(rentsForWeek);

            // Add to the map with a label
            String weekLabel = "Week of " + currentWeekStart.toString();
            weeklyEventsMap.put(weekLabel, eventsForWeek);

            // Move to the next week
            currentWeekStart = currentWeekStart.plusWeeks(1);
        }

        return weeklyEventsMap;
    }



}