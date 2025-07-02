package com.milla.kartingrmbackend.services;

import com.milla.kartingrmbackend.entities.HolidayEntity;
import com.milla.kartingrmbackend.repositories.HolidayRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class HolidayService {
    private final HolidayRepository holidayRepository;

    public HolidayService(HolidayRepository holidayRepository) {
        this.holidayRepository = holidayRepository;
    }

    //Getters
    public List<HolidayEntity> findAllHolidays(){
        return holidayRepository.findAll();
    }
    public HolidayEntity findHolidayById(int id){
        return holidayRepository.findById(id).orElse(null);
    }
    public HolidayEntity findHolidayByDate(LocalDate date){
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();
        return holidayRepository.findByMonthAndDay(month, day);
    }
    //Save
    public HolidayEntity saveHoliday(HolidayEntity holidayEntity){
        return holidayRepository.save(holidayEntity);
    }
    //Update
    public HolidayEntity updateHoliday(HolidayEntity holidayEntity){
        return holidayRepository.save(holidayEntity);
    }
    //Is it a holiday
    public boolean isItAHoliday(LocalDate date) {
        return holidayRepository.existsByMonthAndDay(date.getMonthValue(), date.getDayOfMonth());
    }
    //Get discount
    public BigDecimal findHolidayDiscountByDate(LocalDate date){
        HolidayEntity holiday = findHolidayByDate(date);
        if(holiday == null){
            return BigDecimal.ONE;
        }
        return holiday.getDiscount();
    }
    //Is today a holiday
    public boolean isTodayAHoliday(){
        LocalDate today = LocalDate.now();
        return isItAHoliday(today);
    }
}
