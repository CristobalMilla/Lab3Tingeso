package com.milla.KartingRMBackend.Repositories;

import com.milla.KartingRMBackend.Entities.RentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RentRepository extends JpaRepository<RentEntity, Integer> {

    List<RentEntity> findByMainClient(String mainClient);
    List<RentEntity> findByRentDate(LocalDate date);
    @Query("SELECT r FROM RentEntity r WHERE r.rentDate BETWEEN :startDate AND :endDate")
    List<RentEntity> findRentsBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    // Fetch rents between two dates
    List<RentEntity> findByRentDateBetween(LocalDate startDate, LocalDate endDate);
}
