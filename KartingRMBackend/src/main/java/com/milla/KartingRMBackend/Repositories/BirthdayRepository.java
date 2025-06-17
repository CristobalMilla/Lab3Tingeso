package com.milla.KartingRMBackend.Repositories;

import com.milla.KartingRMBackend.Entities.BirthdayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BirthdayRepository extends JpaRepository<BirthdayEntity, Integer> {

    BirthdayEntity findFirstByName(String name);
}
