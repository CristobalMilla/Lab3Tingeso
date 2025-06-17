package com.milla.KartingRMBackend.Repositories;

import com.milla.KartingRMBackend.Entities.PeopleDiscountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PeopleDiscountRepository extends JpaRepository<PeopleDiscountEntity, Integer> {
    //Funcion que retorna un PeopleDiscountEntity segun un numero de personas de entrada
    @Query(value = "SELECT * FROM people_discount WHERE :peopleAmount BETWEEN min_people AND max_people LIMIT 1", nativeQuery = true)
    PeopleDiscountEntity findByPeopleAmount(@Param("peopleAmount") int peopleAmount);
}
