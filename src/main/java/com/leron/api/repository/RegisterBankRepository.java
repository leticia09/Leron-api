package com.leron.api.repository;

import com.leron.api.model.entities.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegisterBankRepository extends JpaRepository<Bank, Long> {

    @Query("SELECT b FROM Bank b WHERE b.userAuthId = :userAuthId AND b.deleted = false ORDER BY b.name")
    List<Bank> findByUserAuthId(@Param("userAuthId") Long userAuthId);

    Bank findBankByUserAuthIdAndId(Long userAuthId, Long id);

    List<Bank> findAllBankByUserAuthIdAndId(Long userAuthId, Long id);
}