package com.leron.api.repository;

import com.leron.api.model.entities.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long> {

}
