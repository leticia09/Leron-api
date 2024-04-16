package com.leron.api.repository;

import com.leron.api.model.entities.ForecastDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForecastDateRepository extends JpaRepository<ForecastDate, Long> {
    @Query("SELECT fd FROM ForecastDate fd " +
            "WHERE fd.forecast.id IN :forecastIds " +
            "AND fd.deleted = false " +
            "AND fd.year = :year")
    List<ForecastDate> findAllByUserAuthIdAndMonthAndYearAndOwnersId(
            @Param("forecastIds") List<Long> forecastIds,
            @Param("year") Long year
    );
}
