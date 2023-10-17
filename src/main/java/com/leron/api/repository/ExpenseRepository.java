package com.leron.api.repository;

import com.leron.api.model.entities.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long> {
    @Query(value = "SELECT new com.leron.api.model.entities.ExpenseEntity(u.id,u.userId,u.type,u.description,u.shoppingDate,u.local,u.group,u.price,u.formPayment,u.payer,u.cardId,u.advance,u.typePayment,u.status,u.installment,u.paymentDate) FROM ExpenseEntity u " +
            "WHERE u.userAuthId = :id ")
    List<ExpenseEntity> findAllByAuthUserId(Long id);

}
