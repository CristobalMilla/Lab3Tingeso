package com.milla.kartingrmbackend.controllers;


import com.milla.kartingrmbackend.entities.HolidayEntity;
import com.milla.kartingrmbackend.services.HolidayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/holiday")
public class HolidayController {
    private final HolidayService holidayService;

    public HolidayController(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<HolidayEntity>> getAllHolidays(){
        List<HolidayEntity> holidays = holidayService.findAllHolidays();
        if(holidays.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(holidays);
    }
    @GetMapping("/findById/{id}")
    public ResponseEntity<HolidayEntity> getHolidayById(@PathVariable int id){
        HolidayEntity holiday = holidayService.findHolidayById(id);
        if(holiday == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(holiday);
    }
    @PostMapping("/save")
    public ResponseEntity<HolidayEntity> saveHoliday(@RequestBody HolidayEntity holidayEntity){
        HolidayEntity savedHoliday = holidayService.saveHoliday(holidayEntity);
        return ResponseEntity.ok(savedHoliday);
    }
    @PutMapping("/update")
    public ResponseEntity<HolidayEntity> updateHoliday(@RequestBody HolidayEntity holidayEntity){
        HolidayEntity updatedHoliday = holidayService.updateHoliday(holidayEntity);
        return ResponseEntity.ok(updatedHoliday);
    }
    @GetMapping("/isItHoliday")
    public ResponseEntity<Boolean> isItHoliday(@RequestParam LocalDate date){
        boolean isItHoliday = holidayService.isItAHoliday(date);
        return ResponseEntity.ok(isItHoliday);
    }
    @GetMapping("/discount")
    public ResponseEntity<Double> getHolidayDiscountByDate(@RequestParam LocalDate date){
        Double discount = holidayService.findHolidayDiscountByDate(date).doubleValue();
        return ResponseEntity.ok(discount);
    }
    @GetMapping("/isTodayAHoliday")
    public ResponseEntity<Boolean> isTodayAHoliday(){
        boolean isTodayAHoliday = holidayService.isTodayAHoliday();
        return ResponseEntity.ok(isTodayAHoliday);
    }
}
