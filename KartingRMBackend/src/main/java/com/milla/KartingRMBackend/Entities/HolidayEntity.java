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
    private String name;
    @Column(name = "holiday_month")
    private int month;
    @Column(name = "holiday_day")
    private int day;
    BigDecimal discount;
}
