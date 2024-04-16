package com.leron.api.repository;

import com.leron.api.model.entities.BankMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankMovementRepository extends JpaRepository<BankMovement, Long> {
    List<BankMovement> findAllByUserAuthIdAndDeletedFalseOrderByDateMovementDesc(@Param("userAuthId") Long userAuthId);

    List<BankMovement> findAllByUserAuthIdAndDeletedFalse(@Param("userAuthId") Long userAuthId);

    List<BankMovement> findAllByUserAuthIdAndBankId(@Param("userAuthId") Long userAuthId, @Param("bankId") Long bankId);

    @Query("SELECT b FROM BankMovement b WHERE b.userAuthId = :userAuthId AND b.ownerId in (:ownersId) AND b.referencePeriod = :referencePeriod AND b.deleted = false")
    List<BankMovement> findAllByUserAuthIdAndDeletedFalseAndReferencePeriodAndOwnersId(
            @Param("userAuthId") Long userAuthId,
            @Param("referencePeriod") String referencePeriod,
            @Param("ownersId") List<Long> ownersId);

    @Query("SELECT b FROM BankMovement b WHERE b.userAuthId = :userAuthId AND b.ownerId in (:ownersId) AND b.deleted = false")
    List<BankMovement> findAllByUserAuthIdAndDeletedFalseAndOwnersId(
            @Param("userAuthId") Long userAuthId,
            @Param("ownersId") List<Long> ownersId);

    List<BankMovement> findAllByUserAuthIdAndFinancialEntityId(@Param("userAuthId") Long userAuthId, @Param("financialEntityId") Long financialEntityId);
}
