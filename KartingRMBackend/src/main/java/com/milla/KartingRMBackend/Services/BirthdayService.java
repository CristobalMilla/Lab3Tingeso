package com.milla.KartingRMBackend.Services;

import com.milla.KartingRMBackend.Entities.BirthdayEntity;
import com.milla.KartingRMBackend.Repositories.BirthdayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class BirthdayService {
    @Autowired
    private BirthdayRepository birthdayRepository;

    //Getters
    public List<BirthdayEntity> findAllBirthday(){
        return birthdayRepository.findAll();
    }
    public BirthdayEntity findByBirthdayId(int id){
        return birthdayRepository.findById(id).orElse(null);
    }
    //Save
    public BirthdayEntity saveBirthday(BirthdayEntity birthdayEntity){
        return birthdayRepository.save(birthdayEntity);
    }
    //Update
    public BirthdayEntity updateBirthday(BirthdayEntity birthdayEntity){
        return birthdayRepository.save(birthdayEntity);
    }
    //Get date from name
    public LocalDate findBirthdayDateByName(String name){
        BirthdayEntity birthday = birthdayRepository.findFirstByName(name);
        if(birthday == null){
            return null;
        }
        return birthday.getDate();
    }
    //Boolean of its birthday
    public boolean isItsBirthday(String name, LocalDate date){
        LocalDate birthdayDate = findBirthdayDateByName(name);
        if (birthdayDate != null) {
            if (birthdayDate.getDayOfMonth() == date.getDayOfMonth()
                    && birthdayDate.getMonth() == date.getMonth()) {
                return true;
            }
        } return false;

    }
    public BigDecimal findBirthdayDiscountByName(String name){
        BirthdayEntity birthday = birthdayRepository.findFirstByName(name);
        if(birthday == null){
            return BigDecimal.ONE;
        }
        return birthday.getDiscount();
    }
}
