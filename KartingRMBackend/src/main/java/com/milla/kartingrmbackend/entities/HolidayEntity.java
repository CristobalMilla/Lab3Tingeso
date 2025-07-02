package com.milla.kartingrmbackend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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
