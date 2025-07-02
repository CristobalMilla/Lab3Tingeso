package com.milla.kartingrmbackend.controllers;

import com.milla.kartingrmbackend.entities.PeopleDiscountEntity;
import com.milla.kartingrmbackend.services.PeopleDiscountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/peopleDiscount")
public class PeopleDiscountController {
    private final PeopleDiscountService peopleDiscountService;

    public PeopleDiscountController(PeopleDiscountService peopleDiscountService) {
        this.peopleDiscountService = peopleDiscountService;
    }

    //Getters
    @GetMapping("/all")
    public ResponseEntity<List<PeopleDiscountEntity>> getAllPeopleDiscount() {
        List<PeopleDiscountEntity> list = peopleDiscountService.getAll();
        return ResponseEntity.ok(list);
    }
    @GetMapping("/findById/{id}")
    public ResponseEntity<PeopleDiscountEntity> getPeopleDiscountById(@PathVariable("id") int id) {
        PeopleDiscountEntity peopleDiscount = peopleDiscountService.getPeopleDiscountById(id);
        if (peopleDiscount == null) {
            return ResponseEntity.notFound().build();
        }
        else {
            return ResponseEntity.ok(peopleDiscount);
        }
    }
    //Save
    @PostMapping("/save")
    public ResponseEntity<PeopleDiscountEntity> save(PeopleDiscountEntity peopleDiscountEntity) {
        PeopleDiscountEntity newPeopleDiscount = peopleDiscountService.save(peopleDiscountEntity);
        return ResponseEntity.ok(newPeopleDiscount);
    }
    @PutMapping("/update")
    //Update
    public ResponseEntity<PeopleDiscountEntity> update(PeopleDiscountEntity peopleDiscountEntity) {
        PeopleDiscountEntity updatedPeopleDiscount = peopleDiscountService.update(peopleDiscountEntity);
        return ResponseEntity.ok(updatedPeopleDiscount);
    }
    //Funcion que retorna un PeopleDiscountEntity segun un numero de personas de entrada
    @GetMapping("/getPeopleDiscountByAmount/{peopleAmount}")
    public ResponseEntity<PeopleDiscountEntity> findByPeopleAmount(@PathVariable  int peopleAmount) {
        PeopleDiscountEntity peopleDiscount = peopleDiscountService.findByPeopleAmount(peopleAmount);
        if (peopleDiscount == null) {
            return ResponseEntity.notFound().build();
        }
        else {
            return ResponseEntity.ok(peopleDiscount);
        }
    }
    //Funcion igual a la anterior, pero que devuelve el descuento
    @GetMapping("/getDiscountByAmount/{peopleAmount}")
    public ResponseEntity<BigDecimal> findDiscountByAmount(@PathVariable int peopleAmount) {
        PeopleDiscountEntity peopleDiscount = peopleDiscountService.findByPeopleAmount(peopleAmount);
        if (peopleDiscount == null) {
            return ResponseEntity.notFound().build();
        }
        else {
            return ResponseEntity.ok(peopleDiscount.getDiscount());
        }
    }

}
