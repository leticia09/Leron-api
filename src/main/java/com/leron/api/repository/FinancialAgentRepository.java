package com.leron.api.repository;

import com.leron.api.model.entities.FinancialAgentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinancialAgentRepository extends JpaRepository<FinancialAgentEntity, Long> {
}