package com.milla.KartingRMBackend.Repositories;


import com.milla.KartingRMBackend.Entities.FeeTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeeTypeRepository extends JpaRepository<FeeTypeEntity, Integer> {
}
