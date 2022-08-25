package com.leron.api.repository;

import com.leron.api.model.FinancialAgentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinancialAgentRepository extends JpaRepository<FinancialAgentModel, Long> {
}