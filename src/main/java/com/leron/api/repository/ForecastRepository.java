package com.leron.api.repository;

import com.leron.api.model.entities.Forecast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForecastRepository  extends JpaRepository<Forecast, Long> {

    List<Forecast> findAllByUserAuthIdAndDeletedFalse (Long userAuthId);
}
