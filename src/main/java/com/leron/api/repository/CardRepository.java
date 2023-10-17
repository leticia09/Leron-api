package com.leron.api.repository;

import com.leron.api.model.entities.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
//    @Query(value = "SELECT new com.leron.api.model.entities.CardEntity(    u.id, u.nickName,u.userId,u.status,u.modality,u.finalCard,u.bankId,u.billClose,u.dueDate) FROM CardEntity u " +
//            "WHERE u.userAuthId = :id ")
//    List<Card> findAllByAuthUserId(Long id);
}
