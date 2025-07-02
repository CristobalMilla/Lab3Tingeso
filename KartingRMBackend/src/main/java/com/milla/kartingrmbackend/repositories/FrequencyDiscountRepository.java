package com.milla.kartingrmbackend.repositories;

import com.milla.kartingrmbackend.entities.FrequencyDiscountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FrequencyDiscountRepository extends JpaRepository<FrequencyDiscountEntity, Integer> {

    @Query(value = "SELECT * FROM frequency_discount WHERE :clientFrequency BETWEEN min_frequency AND max_frequency LIMIT 1", nativeQuery = true)
    FrequencyDiscountEntity findByClientFrequency(@Param("clientFrequency") int clientFrequency);

}
