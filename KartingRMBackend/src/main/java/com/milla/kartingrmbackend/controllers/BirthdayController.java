package com.milla.kartingrmbackend.controllers;


import com.milla.kartingrmbackend.entities.BirthdayEntity;
import com.milla.kartingrmbackend.services.BirthdayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/birthday")
public class BirthdayController {
    private final BirthdayService birthdayService;

    public BirthdayController(BirthdayService birthdayService) {
        this.birthdayService = birthdayService;
    }

    //Getters
    @GetMapping("/all")
    public ResponseEntity<List<BirthdayEntity>> getAllBirthdays(){
        List<BirthdayEntity> birthdays = birthdayService.findAllBirthday();
        if(birthdays.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(birthdays);
    }
    @GetMapping("/findById/{id}")
    public ResponseEntity<BirthdayEntity> getBirthdayById(@PathVariable int id) {
        BirthdayEntity birthday = birthdayService.findByBirthdayId(id);
        if (birthday == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(birthday);
        }
    }
    //Save
    @PostMapping("/save")
    public ResponseEntity<BirthdayEntity> saveBirthday(@RequestBody BirthdayEntity birthdayEntity){
        BirthdayEntity savedBirthday = birthdayService.saveBirthday(birthdayEntity);
        return ResponseEntity.ok(savedBirthday);
    }
    @PutMapping("/update")
    public ResponseEntity<BirthdayEntity> updateBirthday(@RequestBody BirthdayEntity birthdayEntity){
        BirthdayEntity updatedBirthday = birthdayService.updateBirthday(birthdayEntity);
        return ResponseEntity.ok(updatedBirthday);
    }
    //Get date from name
    @GetMapping("/findDateByName/{name}")
    public ResponseEntity<LocalDate> getBirthdayDateByName(@PathVariable String name){
        LocalDate date = birthdayService.findBirthdayDateByName(name);
        if(date == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(date);
    }

    //Boolean of its birthday
    @GetMapping("/isItBirthday")
    public ResponseEntity<Boolean> isItsBirthday(@RequestParam String name, @RequestParam LocalDate date){
        boolean isItsBirthday = birthdayService.isItsBirthday(name, date);
        return ResponseEntity.ok(isItsBirthday);
    }
    //Get discount
    @GetMapping("/discount")
    public ResponseEntity<BigDecimal> getBirthdayDiscountByName(@RequestParam String name){
        BigDecimal discount = birthdayService.findBirthdayDiscountByName(name);
        return ResponseEntity.ok(discount);
    }
}
