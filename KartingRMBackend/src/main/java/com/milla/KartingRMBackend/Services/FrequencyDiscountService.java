package com.milla.KartingRMBackend.Services;

import com.milla.KartingRMBackend.Entities.FrequencyDiscountEntity;
import com.milla.KartingRMBackend.Repositories.FrequencyDiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FrequencyDiscountService {
    @Autowired
    private FrequencyDiscountRepository frequencyDiscountRepository;

    //Getters
    public List<FrequencyDiscountEntity> getAll(){
        return frequencyDiscountRepository.findAll();
    }
    public FrequencyDiscountEntity getById(int id){
        return frequencyDiscountRepository.findById(id).orElse(null);
    }
    //Save
    public FrequencyDiscountEntity save(FrequencyDiscountEntity fec){
        return frequencyDiscountRepository.save(fec);
    }
    //Update
    public FrequencyDiscountEntity update(FrequencyDiscountEntity fec){
        return frequencyDiscountRepository.save(fec);
    }

    //Funcion para obtener una entidad descuento-frecuencia segun numero de visitas ingresado
    public FrequencyDiscountEntity getFrequencyByClientFrequency(int clientFrequency){
        return frequencyDiscountRepository.findByClientFrequency(clientFrequency);
    }
}
