package com.leron.api.repository;

import com.leron.api.model.entities.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {

    @Query("SELECT b FROM Transfer b WHERE b.userAuthId = :userAuthId")
    List<Transfer> findByUserAuthId(@Param("userAuthId") Long userAuthId);
}
