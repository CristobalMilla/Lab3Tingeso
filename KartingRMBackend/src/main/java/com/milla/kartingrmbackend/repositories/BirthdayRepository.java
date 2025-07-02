package com.milla.kartingrmbackend.repositories;

import com.milla.kartingrmbackend.entities.BirthdayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BirthdayRepository extends JpaRepository<BirthdayEntity, Integer> {

    BirthdayEntity findFirstByName(String name);
}
