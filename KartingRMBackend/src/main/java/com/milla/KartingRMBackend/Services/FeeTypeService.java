package com.milla.KartingRMBackend.Services;

import com.milla.KartingRMBackend.Entities.FeeTypeEntity;
import com.milla.KartingRMBackend.Repositories.FeeTypeRepository;
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

