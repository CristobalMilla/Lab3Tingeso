package com.milla.KartingRMBackend.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "people_discount")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PeopleDiscountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "people_discount_id")
    private int peopleDiscountId;
    @Column(name = "min_people")
    private int minPeople;
    @Column(name = "max_people")
    private int maxPeople;
    @Column(name = "discount")
    private BigDecimal discount;
}
