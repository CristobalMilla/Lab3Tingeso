package com.milla.KartingRMBackend.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "holiday")
@NoArgsConstructor
@AllArgsConstructor
public class HolidayEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "holiday_id")
    private int holidayId;
    @Column(name = "name")
    private String name;
    @Column(name = "month")
    private int month;
    @Column(name = "day")
    private int day;
    @Column(name = "discount")
    BigDecimal discount;
}
