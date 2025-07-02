package com.milla.kartingrmbackend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "receipt")
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "receipt_id")
    private int receiptId;
    @Column(name = "rent_id")
    private int rentId;
    @Column(name = "sub_client_name")
    private String subClientName;
    @Column(name = "sub_client_email")
    private String subClientEmail;
    @Column(name = "base_tariff")
    private BigDecimal baseTariff;
    @Column(name = "size_discount")
    private BigDecimal sizeDiscount;
    @Column(name = "special_discount")
    private BigDecimal specialDiscount;
    @Column(name = "aggregated_price")
    private BigDecimal aggregatedPrice;
    @Column(name = "iva_price")
    private BigDecimal ivaPrice;
    @Column(name = "final_price")
    private BigDecimal finalPrice;

    public ReceiptEntity(int receiptId, int rentId, String subClientName, String subClientEmail) {
        this.receiptId = receiptId;
        this.rentId = rentId;
        this.subClientName = subClientName;
        this.subClientEmail = subClientEmail;
    }
}