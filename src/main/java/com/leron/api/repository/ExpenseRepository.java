package com.leron.api.repository;

import com.leron.api.model.entities.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense,Long> {
    List<Expense> findAllByUserAuthIdAndDeletedFalse (Long userAuthId);
}
