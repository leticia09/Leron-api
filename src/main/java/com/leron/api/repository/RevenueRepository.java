package com.leron.api.repository;

import com.leron.api.model.entities.RevenueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RevenueRepository extends JpaRepository<RevenueEntity, Long> {
    @Query(value = "SELECT new com.leron.api.model.entities.RevenueEntity(u.id,u.description,u.salaryId,u.type,u.status,u.receivingDate,u.value, u.bankId ,u.accountId) FROM RevenueEntity u " +
            "WHERE u.userAuthId = :id ")
    List<RevenueEntity> findAllByAuthUserId(Long id);
}
