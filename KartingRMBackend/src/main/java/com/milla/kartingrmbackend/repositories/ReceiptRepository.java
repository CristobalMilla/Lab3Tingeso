package com.milla.kartingrmbackend.repositories;

import com.milla.kartingrmbackend.entities.ReceiptEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceiptRepository extends JpaRepository<ReceiptEntity, Integer> {
    List<ReceiptEntity> getReceiptsByRentId(int rentId);
}
