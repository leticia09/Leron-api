package com.leron.api.repository;

import com.leron.api.model.entities.Entrance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntranceRepository extends JpaRepository<Entrance, Long> {

    List<Entrance> findAllByUserAuthIdAndDeletedFalseOrderByInitialDateDesc(@Param("userAuthId") Long userAuthId);
    List<Entrance> findAllByUserAuthIdAndDeletedFalse (Long userAuthId);

    List<Entrance> findAllByUserAuthIdAndBankId(@Param("userAuthId") Long userAuthId, @Param("bankId") Long bankId);

    List<Entrance> findAllByUserAuthIdAndFinancialEntityId(@Param("userAuthId") Long userAuthId, @Param("financialEntityId") Long financialEntityId);

    @Query("SELECT b FROM Entrance b WHERE b.userAuthId = :userAuthId AND b.ownerId in (:ids) AND b.deleted = false")
    List<Entrance> findAllByUserAuthIdAndOwnersId(Long userAuthId, List<Long> ids);
}
