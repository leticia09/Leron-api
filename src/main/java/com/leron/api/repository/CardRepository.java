package com.leron.api.repository;

import com.leron.api.model.entities.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    @Query("SELECT b FROM Card b WHERE b.userAuthId = :userAuthId")
    List<Card> findByUserAuthId(@Param("userAuthId") Long userAuthId);

    Card findCardByIdAndUserAuthId(Long userAuthId, Long id);
}
