package com.milla.KartingRMBackend.Repositories;

import com.milla.KartingRMBackend.Entities.ReceiptEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceiptRepository extends JpaRepository<ReceiptEntity, Integer> {
    List<ReceiptEntity> getReceiptsByRentId(int rent_id);
}
