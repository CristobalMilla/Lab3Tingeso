package com.milla.kartingrmbackend.services;

import com.milla.kartingrmbackend.entities.FeeTypeEntity;
import com.milla.kartingrmbackend.repositories.FeeTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeeTypeService {
    private final FeeTypeRepository feeTypeRepository;

    public FeeTypeService(FeeTypeRepository feeTypeRepository) {
        this.feeTypeRepository = feeTypeRepository;
    }

    //Getters
    public List<FeeTypeEntity> getAll(){
        return feeTypeRepository.findAll();
    }
    public FeeTypeEntity getFeeTypeById(int id){
        return feeTypeRepository.findById(id).orElse(null);
    }
    //Save
    public FeeTypeEntity save(FeeTypeEntity feeType){
        return feeTypeRepository.save(feeType);
    }
    //Update
    public FeeTypeEntity update(FeeTypeEntity feeType){
        return feeTypeRepository.save(feeType);
    }
}

