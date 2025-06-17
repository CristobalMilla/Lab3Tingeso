package com.milla.KartingRMBackend.Repositories;

import com.milla.KartingRMBackend.Entities.HolidayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface HolidayRepository extends JpaRepository<HolidayEntity, Integer> {
    HolidayEntity findByDate(int month, int day);
    @Query("SELECT CASE WHEN COUNT(h) > 0 THEN true ELSE false END " +
            "FROM HolidayEntity h WHERE h.month = :month AND h.day = :day")
    boolean existsByMonthAndDay(@Param("month") int month, @Param("day") int day);

}
