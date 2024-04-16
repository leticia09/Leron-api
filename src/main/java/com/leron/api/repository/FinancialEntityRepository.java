package com.leron.api.repository;

import com.leron.api.model.entities.FinancialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinancialEntityRepository extends JpaRepository<FinancialEntity, Long> {
    List<FinancialEntity> findAllByUserAuthIdAndDeletedFalse (Long userAuthId);
}
