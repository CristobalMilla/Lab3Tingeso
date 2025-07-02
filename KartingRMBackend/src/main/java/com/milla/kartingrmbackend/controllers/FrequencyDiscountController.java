package com.milla.kartingrmbackend.controllers;

import com.milla.kartingrmbackend.entities.FrequencyDiscountEntity;
import com.milla.kartingrmbackend.services.FrequencyDiscountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/frequencyDiscount")
public class FrequencyDiscountController {
    private final FrequencyDiscountService frequencyDiscountService;

    public FrequencyDiscountController(FrequencyDiscountService frequencyDiscountService) {
        this.frequencyDiscountService = frequencyDiscountService;
    }

    //Getters
    @GetMapping("/all")
    public ResponseEntity<List<FrequencyDiscountEntity>> getAll() {
        List<FrequencyDiscountEntity> frequencyDiscountEntities = frequencyDiscountService.getAll();
        if (frequencyDiscountEntities == null) {
            return ResponseEntity.noContent().build();
        }
        else {
            return ResponseEntity.ok(frequencyDiscountEntities);
        }
    }
    @GetMapping("/findById/{id}")
    public ResponseEntity<FrequencyDiscountEntity> getById(@PathVariable int id) {
        FrequencyDiscountEntity frequencyDiscountEntity = frequencyDiscountService.getById(id);
        if (frequencyDiscountEntity == null) {
            return ResponseEntity.notFound().build();
        }
        else {
            return ResponseEntity.ok(frequencyDiscountEntity);
        }
    }
    //Save
    @PostMapping("/save")
    public ResponseEntity<FrequencyDiscountEntity> save(@RequestBody FrequencyDiscountEntity frequencyDiscountEntity) {
        FrequencyDiscountEntity frequencyDiscountEntitySaved = frequencyDiscountService.save(frequencyDiscountEntity);
        return ResponseEntity.ok(frequencyDiscountEntitySaved);
    }
    //Update
    @PutMapping("/update")
    public ResponseEntity<FrequencyDiscountEntity> update(@RequestBody FrequencyDiscountEntity frequencyDiscountEntity) {
        FrequencyDiscountEntity frequencyDiscountEntityUpdated = frequencyDiscountService.update(frequencyDiscountEntity);
        return ResponseEntity.ok(frequencyDiscountEntityUpdated);
    }

    //Controller de Funcion para obtener una entidad descuento-frecuencia segun numero de visitas ingresado
    @GetMapping("/getFrequencyByNumber/{number}")
    public ResponseEntity<FrequencyDiscountEntity> getFrequencyByNumber(@PathVariable int number) {
        FrequencyDiscountEntity frequencyDiscountEntity = frequencyDiscountService.getFrequencyByClientFrequency(number);
        if (frequencyDiscountEntity == null) {
            return ResponseEntity.notFound().build();
        }
        else {
            return ResponseEntity.ok(frequencyDiscountEntity);
        }
    }
    //Controller de misma funcion, pero devolviendo el descuento
    @GetMapping("/getDiscountByNumber/{number}")
    public ResponseEntity<BigDecimal> getDiscountByNumber(@PathVariable int number) {
        FrequencyDiscountEntity frequencyDiscountEntity = frequencyDiscountService.getFrequencyByClientFrequency(number);
        if (frequencyDiscountEntity == null) {
            return ResponseEntity.notFound().build();
        }
        else {
            return ResponseEntity.ok(frequencyDiscountEntity.getDiscount());
        }
    }
}
