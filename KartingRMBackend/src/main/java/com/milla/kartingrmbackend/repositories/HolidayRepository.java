package com.milla.kartingrmbackend.repositories;

import com.milla.kartingrmbackend.entities.HolidayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HolidayRepository extends JpaRepository<HolidayEntity, Integer> {
    HolidayEntity findByMonthAndDay(int month, int day);
    @Query("SELECT CASE WHEN COUNT(h) > 0 THEN true ELSE false END " +
            "FROM HolidayEntity h WHERE h.month = :month AND h.day = :day")
    boolean existsByMonthAndDay(@Param("month") int month, @Param("day") int day);

}
