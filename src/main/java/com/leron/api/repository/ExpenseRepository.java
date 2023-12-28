package com.leron.api.repository;

import com.leron.api.model.entities.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense,Long> {
    List<Expense> findAllByUserAuthIdAndDeletedFalse (Long userAuthId);

    List<Expense> findAllByUserAuthIdAndBankIdAndAccountIdAndFinalCard(@Param("userAuthId") Long userAuthId, @Param("bankId") Long bankId, @Param("accountId") Long accountId, @Param("finalCard") Long finalCard);

    List<Expense>  findAllByUserAuthIdAndDeletedFalseOrderByDateBuyDesc(@Param("userAuthId") Long userAuthId);

    List<Expense> findAllByUserAuthIdAndBankId(@Param("userAuthId") Long userAuthId, @Param("bankId") Long bankId);

    List<Expense> findAllByUserAuthIdAndFinancialEntityId(@Param("userAuthId") Long userAuthId, @Param("financialEntityId") Long financialEntityId);
}
