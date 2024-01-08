package com.leron.api.repository;

import com.leron.api.model.entities.BankMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankMovementRepository extends JpaRepository<BankMovement, Long> {
    List<BankMovement> findAllByUserAuthIdAndDeletedFalseOrderByDateMovementDesc(@Param("userAuthId") Long userAuthId);

    List<BankMovement> findAllByUserAuthIdAndDeletedFalse(@Param("userAuthId") Long userAuthId);

    List<BankMovement> findAllByUserAuthIdAndBankId(@Param("userAuthId") Long userAuthId, @Param("bankId") Long bankId);

    List<BankMovement> findAllByUserAuthIdAndDeletedFalseAndReferencePeriod(@Param("userAuthId") Long userAuthId, @Param("referencePeriod") String referencePeriod);

    List<BankMovement> findAllByUserAuthIdAndFinancialEntityId(@Param("userAuthId") Long userAuthId, @Param("financialEntityId") Long financialEntityId);
}
