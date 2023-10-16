package com.leron.api.repository;

import com.leron.api.model.entities.PointsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointsRepository extends JpaRepository<PointsEntity, Long> {

    @Query(value = "SELECT new com.leron.api.model.entities.PointsEntity(u.id,u.program,u.bankId,u.accountId,u.currency,u.point, u.status) FROM PointsEntity u " +
            "WHERE u.userAuthId = :id ")
    List<PointsEntity> findAllByAuthUserId(Long id);
}
