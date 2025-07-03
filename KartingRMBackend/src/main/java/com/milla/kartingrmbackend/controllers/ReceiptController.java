package com.milla.kartingrmbackend.controllers;

import com.milla.kartingrmbackend.dto.RentPreviewDTO;
import com.milla.kartingrmbackend.dto.RentRequestDTO;
import com.milla.kartingrmbackend.entities.ReceiptEntity;
import com.milla.kartingrmbackend.entities.RentEntity;
import com.milla.kartingrmbackend.services.ReceiptService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/receipt")
public class ReceiptController {
    private final ReceiptService receiptService;

    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }


    //Getters
    @GetMapping("/all")
    public ResponseEntity<List<ReceiptEntity>> getAllReceipts() {
        List<ReceiptEntity> receiptList = receiptService.getAll();
        if (receiptList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        else {
            return ResponseEntity.ok(receiptList);
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<ReceiptEntity> getReceiptById(@PathVariable("id") int id) {
        ReceiptEntity receipt = receiptService.getReceiptById(id);
        if (receipt == null) {
            return ResponseEntity.notFound().build();
        }
        else {
            return ResponseEntity.ok(receipt);
        }
    }
    //Save completo
    @PostMapping("/saveComplete")
    public ResponseEntity<ReceiptEntity> saveComplete(@RequestBody ReceiptEntity receipt) {
        ReceiptEntity savedReceipt = receiptService.save(receipt);
        if (savedReceipt == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(savedReceipt);
    }
    //Update
    @PutMapping("/update")
    public ResponseEntity<ReceiptEntity> updateReceipt(@RequestBody ReceiptEntity receipt) {
        ReceiptEntity updatedReceipt = receiptService.update(receipt);
        if (updatedReceipt == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(updatedReceipt);
    }
    //Get para obtener la tarifa correspondiente al recibo, segun su renta
    @GetMapping("/feePriceByReceiptId/{id}")
    public ResponseEntity<BigDecimal> getFeePriceByReceiptId(@PathVariable("id") int receiptId) {
        BigDecimal feePrice = receiptService.getFeePriceByReceiptId(receiptId);
        return ResponseEntity.ok(feePrice);
    }
    //Get para obtener el people_discount price del recibo, segun su renta
    @GetMapping("/peopleDiscountPriceByReceiptId/{id}")
    public ResponseEntity<BigDecimal> getPeopleDiscountPriceByReceiptId(@PathVariable("id") int receiptId) {
        BigDecimal peopleDiscountPrice = receiptService.getPeopleDiscountPriceByReceiptId(receiptId);
        return ResponseEntity.ok(peopleDiscountPrice);
    }

    //Funcion que calcula los distintos campos de un recibo creado, y la crea en la base de datos
    @PostMapping("/calcAndSave")
    public ResponseEntity<RentEntity> saveCalc(@RequestBody RentRequestDTO request) {
        RentEntity savedCalcReceipt = receiptService.saveRentWithReceipts(request.getRent(), request.getSubClients());
        return ResponseEntity.ok(savedCalcReceipt);
    }

    @PostMapping("/calculatePreview")
    public ResponseEntity<RentPreviewDTO> calculatePreview(@RequestBody RentRequestDTO request) {
        RentPreviewDTO preview = receiptService.calculateRentPreview(request.getRent(), request.getSubClients());
        return ResponseEntity.ok(preview);
    }

    // Updated save endpoint to accept preview data
    @PostMapping("/saveFromPreview")
    public ResponseEntity<RentEntity> saveFromPreview(@RequestBody RentPreviewDTO preview) {
        RentEntity savedRent = receiptService.saveRentFromPreview(preview);
        return ResponseEntity.ok(savedRent);
    }

}

