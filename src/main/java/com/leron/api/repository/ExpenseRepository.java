package com.leron.api.repository;

import com.leron.api.model.entities.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense,Long> {
    List<Expense> findAllByUserAuthIdAndDeletedFalse (Long userAuthId);

    List<Expense> findAllByUserAuthIdAndFinalCard(@Param("userAuthId") Long userAuthId, @Param("finalCard") Long finalCard);


    List<Expense> findAllByUserAuthIdAndBankIdAndAccountIdAndFinalCard(@Param("userAuthId") Long userAuthId, @Param("bankId") Long bankId, @Param("accountId") Long accountId, @Param("finalCard") Long finalCard);

    List<Expense>  findAllByUserAuthIdAndDeletedFalseOrderByDateBuyDesc(@Param("userAuthId") Long userAuthId);

    List<Expense>  findAllByUserAuthIdAndDeletedFalseAndHasFixedTrueAndOwnerId(@Param("userAuthId") Long userAuthId, Long ownerId);

    List<Expense>  findAllByUserAuthIdAndDeletedFalseAndHasSplitExpenseTrueAndOwnerId(@Param("userAuthId") Long userAuthId, Long ownerId);

    List<Expense> findAllByUserAuthIdAndBankId(@Param("userAuthId") Long userAuthId, @Param("bankId") Long bankId);

    List<Expense> findAllByUserAuthIdAndFinancialEntityId(@Param("userAuthId") Long userAuthId, @Param("financialEntityId") Long financialEntityId);

    @Query("SELECT b FROM Expense b WHERE b.userAuthId = :userAuthId AND b.ownerId in (:ids) AND b.deleted = false")
    List<Expense> findAllByUserAuthIdAndOwnersId(Long userAuthId, List<Long> ids);
}
