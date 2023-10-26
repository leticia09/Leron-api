package com.leron.api.repository;

import com.leron.api.model.entities.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointsRepository extends JpaRepository<Score, Long> {
    @Query("SELECT b FROM Score b WHERE b.userAuthId = :userAuthId")
    List<Score> findByUserAuthId(@Param("userAuthId") Long userAuthId);
}
