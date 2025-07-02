package com.milla.kartingrmbackend.services;

import com.milla.kartingrmbackend.entities.FrequencyDiscountEntity;
import com.milla.kartingrmbackend.repositories.FrequencyDiscountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FrequencyDiscountService {
    private final FrequencyDiscountRepository frequencyDiscountRepository;

    public FrequencyDiscountService(FrequencyDiscountRepository frequencyDiscountRepository) {
        this.frequencyDiscountRepository = frequencyDiscountRepository;
    }

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
