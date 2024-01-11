package com.leron.api.repository;

import com.leron.api.model.entities.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    @Query("SELECT b FROM Card b WHERE b.userAuthId = :userAuthId AND b.deleted = false")
    List<Card> findByUserAuthId(@Param("userAuthId") Long userAuthId);

    Card findCardByIdAndUserAuthId(Long userAuthId, Long id);
    @Query("SELECT b FROM Card b WHERE b.finalNumber = :finalNumber AND b.deleted = false")
    Card findCardByFinalNumber(Long finalNumber);
}
