package com.leron.api.repository;

import com.leron.api.model.entities.BankEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankRepository extends JpaRepository<BankEntity, Long> {
    @Query(value = "SELECT new com.leron.api.model.entities.BankEntity(u.id,u.name,u.status) FROM BankEntity u " +
            "WHERE u.userAuthId = :id ")
    List<BankEntity> findAllByAuthUserId(Long id);

}
