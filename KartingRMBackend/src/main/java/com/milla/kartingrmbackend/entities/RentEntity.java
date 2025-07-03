package com.milla.kartingrmbackend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@Table(name = "rent")
@NoArgsConstructor
@AllArgsConstructor
public class RentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rent_id")
    private Integer rentId;
    @Column(name = "rent_code")
    private String rentCode;
    @Column(name = "rent_date")
    private LocalDate rentDate;
    @Column(name = "rent_time")
    private LocalTime rentTime;
    @Column(name = "fee_type_id")
    private int feeTypeId;
    @Column(name = "people_number")
    private int peopleNumber;
    @Column(name = "main_client")
    private String mainClient;
    @Column(name = "total_price")
    private BigDecimal totalPrice;

    public RentEntity(Integer rentId, String rentCode, LocalDate rentDate, LocalTime rentTime, int feeTypeId, int peopleNumber, String mainClient) {
        this.rentId = rentId;
        this.rentCode = rentCode;
        this.rentDate = rentDate;
        this.rentTime = rentTime;
        this.feeTypeId = feeTypeId;
        this.peopleNumber = peopleNumber;
        this.mainClient = mainClient;
    }
}
