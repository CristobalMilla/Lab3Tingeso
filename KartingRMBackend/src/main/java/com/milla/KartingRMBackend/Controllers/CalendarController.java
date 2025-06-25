package com.milla.KartingRMBackend.Controllers;

import com.milla.KartingRMBackend.DTO.CalendarEvent;
import com.milla.KartingRMBackend.Entities.RentEntity;
import com.milla.KartingRMBackend.Services.CalendarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/calendar")
public class CalendarController {

    private final CalendarService calendarService;

    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }


    //Funcion que recupera las rentas de la semana especificada
    @GetMapping("/singleWeek")
    public ResponseEntity<List<RentEntity>> getRentsByWeek(@RequestParam LocalDate weekStartDate) {
        List<RentEntity> rents = calendarService.getRentsForWeek(weekStartDate);
        return ResponseEntity.ok(rents);
    }
    //Funcion que convierte listas de rentas a listas de eventos, definidos en el DTO
    @GetMapping("/eventListByRentList")
    public ResponseEntity<List<CalendarEvent>> convertToCalendarEvents(@RequestParam List<RentEntity> rents){
        List<CalendarEvent> events = calendarService.convertToCalendarEvents(rents);
        return ResponseEntity.ok(events);
    }
    //Funcion principal que, utilizando las anteriores, obtiene una lista de eventos, segun una fecha inicial y un numero de semanas a partir de esa fecha
    //Retorna en formato mapeado para las semanas, lo que se envia a frontend
    @GetMapping("/getEventsFromWeek")
    public ResponseEntity<Map<String, List<CalendarEvent>>> getCalendarEventsForWeeks(@RequestParam LocalDate startDate, @RequestParam int numberOfWeeks){
        Map<String, List<CalendarEvent>> weeklyEvents = calendarService.getCalendarEventsForWeeks(startDate, numberOfWeeks);
        return ResponseEntity.ok(weeklyEvents);
    }
}