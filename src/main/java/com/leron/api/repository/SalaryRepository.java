package com.leron.api.repository;

import com.leron.api.model.entities.SalaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SalaryRepository extends JpaRepository<SalaryEntity, Long> {
    @Query(value = "SELECT new com.leron.api.model.entities.SalaryEntity( u.id,u.name,u.type,u.userId,u.status) FROM SalaryEntity u " +
            "WHERE u.userAuthId = :id ")
    List<SalaryEntity> findAllByAuthUserId(Long id);

}
