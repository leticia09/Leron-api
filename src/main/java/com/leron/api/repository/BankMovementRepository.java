package com.leron.api.repository;

import com.leron.api.model.entities.BankMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankMovementRepository  extends JpaRepository<BankMovement, Long> {
    List<BankMovement> findAllByUserAuthIdAndDeletedFalse(@Param("userAuthId") Long userAuthId);
}
