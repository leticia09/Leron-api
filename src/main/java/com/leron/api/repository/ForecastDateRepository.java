package com.leron.api.repository;

import com.leron.api.model.entities.ForecastDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForecastDateRepository extends JpaRepository<ForecastDate, Long> {

}
