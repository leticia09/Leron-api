package com.leron.api.repository;

import com.leron.api.model.entities.Forecast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForecastRepository extends JpaRepository<Forecast, Long> {

    List<Forecast> findAllByUserAuthIdAndDeletedFalse(Long userAuthId);

    @Query("SELECT b FROM Forecast b WHERE b.userAuthId = :userAuthId AND b.ownerId in (:ids) AND b.deleted = false")
    List<Forecast> findAllByUserAuthIdAndDeletedFalseAndOwnersId(Long userAuthId, List<Long> ids);

    @Query("SELECT DISTINCT f FROM Forecast f " +
            "INNER JOIN FETCH f.forecastDates fd " +
            "WHERE f.ownerId IN (:ownerIds) " +
            "AND f.deleted = false " +
            "AND fd.month = :month " +
            "AND fd.year = :year " +
            "AND f.userAuthId = :userAuthId " +
            "AND fd.deleted = false")
    List<Forecast> findAllByUserAuthIdAndMonthAndYearAndOwnersId(
            @Param("userAuthId") Long userAuthId,
            @Param("month") String month,
            @Param("year") Long year,
            @Param("ownerIds") List<Long> ownerIds
    );

    @Query("SELECT DISTINCT f.id FROM Forecast f " +
            "WHERE f.ownerId IN (:ownerIds) " +
            "AND f.deleted = false " +
            "AND f.userAuthId = :userAuthId ")
    List<Long> findAllByUserAuthIdAndOwnersId(
            @Param("userAuthId") Long userAuthId,
            @Param("ownerIds") List<Long> ownerIds
    );
}
