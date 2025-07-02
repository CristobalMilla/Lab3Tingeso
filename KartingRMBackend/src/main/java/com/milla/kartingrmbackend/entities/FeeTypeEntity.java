package com.milla.kartingrmbackend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "fee_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeeTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fee_type_id")
    private int feeTypeId;
    @Column(name = "lap_number")
    private int lapNumber;
    @Column(name = "max_time")
    private int maxTime;
    @Column(name = "price")
    private BigDecimal price;
    @Column(name = "duration")
    private int duration;
}
