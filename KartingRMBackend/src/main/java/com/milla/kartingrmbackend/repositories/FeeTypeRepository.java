package com.milla.kartingrmbackend.repositories;


import com.milla.kartingrmbackend.entities.FeeTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeeTypeRepository extends JpaRepository<FeeTypeEntity, Integer> {
}
