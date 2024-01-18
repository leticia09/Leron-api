package com.leron.api.repository;

import com.leron.api.model.entities.Forecast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForecastRepository  extends JpaRepository<Forecast, Long> {

    List<Forecast> findAllByUserAuthIdAndDeletedFalse (Long userAuthId);

    @Query("SELECT b FROM Forecast b WHERE b.userAuthId = :userAuthId AND b.ownerId in (:ids) AND b.deleted = false")
    List<Forecast> findAllByUserAuthIdAndDeletedFalseAndOwnersId(Long userAuthId, List<Long> ids);
}
