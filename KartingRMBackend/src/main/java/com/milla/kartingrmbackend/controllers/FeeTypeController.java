package com.milla.kartingrmbackend.controllers;

import com.milla.kartingrmbackend.entities.FeeTypeEntity;
import com.milla.kartingrmbackend.services.FeeTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feeType")
public class FeeTypeController {
    private final FeeTypeService feeTypeService;

    public FeeTypeController(FeeTypeService feeTypeService) {
        this.feeTypeService = feeTypeService;
    }

    //Getters
    @GetMapping("/all")
    public ResponseEntity<List<FeeTypeEntity>> getAll() {
        List<FeeTypeEntity> feeTypes = feeTypeService.getAll();
        if (feeTypes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        else {
            return ResponseEntity.ok(feeTypes);
        }
    }
    @GetMapping("/findById/{id}")
    public ResponseEntity<FeeTypeEntity> getById(@PathVariable int id) {
        FeeTypeEntity feeType = feeTypeService.getFeeTypeById(id);
        if (feeType == null) {
            return ResponseEntity.notFound().build();
        }
        else {
            return ResponseEntity.ok(feeType);
        }
    }
    //Add
    @PostMapping("/add")
    public ResponseEntity<FeeTypeEntity> save(@RequestBody FeeTypeEntity feeType) {
        FeeTypeEntity feeTypeEntity = feeTypeService.save(feeType);
        return ResponseEntity.ok(feeTypeEntity);
    }
    //Update
    @PutMapping("/update")
    public ResponseEntity<FeeTypeEntity> update(@RequestBody FeeTypeEntity feeType) {
        FeeTypeEntity feeTypeEntity = feeTypeService.update(feeType);
        return ResponseEntity.ok(feeTypeEntity);
    }
}
