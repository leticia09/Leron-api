package com.leron.api.repository;

import com.leron.api.model.entities.CardFinancialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardFinancialEntityRepository extends JpaRepository<CardFinancialEntity, Long> {
   List<CardFinancialEntity> findAllByUserAuthIdAndDeletedFalse (Long userAuthId);
}
