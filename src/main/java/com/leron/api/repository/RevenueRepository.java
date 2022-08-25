package com.leron.api.repository;

import com.leron.api.model.entities.RevenueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RevenueRepository extends JpaRepository<RevenueEntity, Long> {
}
