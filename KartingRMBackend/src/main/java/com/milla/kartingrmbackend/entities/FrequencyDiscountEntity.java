package com.milla.kartingrmbackend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "frequency_discount")
@NoArgsConstructor
@AllArgsConstructor
public class FrequencyDiscountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "frequency_discount_id")
    private int frequencyDiscountId;
    @Column(name = "category")
    private String category;
    @Column(name = "min_frequency")
    private int minFrequency;
    @Column(name = "max_frequency")
    private int maxFrequency;
    @Column(name = "discount")
    private BigDecimal discount;
}
