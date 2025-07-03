package com.milla.kartingrmbackend.dto;

import com.milla.kartingrmbackend.entities.ReceiptEntity;
import com.milla.kartingrmbackend.entities.RentEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentPreviewDTO {
    private RentEntity rent;              // Complete rent with calculated totalPrice
    private List<ReceiptEntity> receipts; // Calculated receipts WITHOUT rentId (will be set on save)
}