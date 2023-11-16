package com.leron.api.repository;

import com.leron.api.model.DTO.points.TypeScoreDTO;
import com.leron.api.model.entities.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PointsRepository extends JpaRepository<Score, Long> {
    @Query("SELECT b FROM Score b WHERE b.userAuthId = :userAuthId AND b.deleted = false GROUP BY b.typeOfScore, b.id ORDER BY b.typeOfScore, b.program")
    List<Score> findByUserAuthId(@Param("userAuthId") Long userAuthId);

    @Query("SELECT b FROM Score b WHERE b.userAuthId = :userAuthId AND id = :id AND b.ownerId = :ownerId AND b.deleted = false")
    Score findScoreById(@Param("id") Long id, @Param("userAuthId") Long userAuthId, @Param("ownerId") Long ownerId);

    @Query("SELECT b FROM Score b WHERE b.userAuthId = :userAuthId AND id = :id AND b.deleted = false")
    Score findScoreByIdWithoutOwner(@Param("id") Long id, @Param("userAuthId") Long userAuthId);

    @Query("SELECT s.value FROM Score s WHERE s.id = :id AND s.userAuthId = :userAuthId AND s.deleted = false")
    BigDecimal findValueByIdAndUserAuthId(@Param("id") Long id, @Param("userAuthId") Long userAuthId);

    @Modifying
    @Query("UPDATE Score s SET s.value = :value WHERE s.id = :id AND s.userAuthId = :userAuthId AND s.deleted = false")
    void updateValueByIdAndUserAuthId(@Param("id") Long id, @Param("userAuthId") Long userAuthId, @Param("value") BigDecimal value);

    @Query("SELECT s.id, s.program FROM Score s WHERE s.userAuthId = :userAuthId AND s.status != 'INACTIVE' AND s.deleted = false")
    List<Object[]> findIdAndProgramByUserAuthId(@Param("userAuthId") Long userAuthId);

    List<Score> findAllByOwnerIdAndDeletedFalse(Long ownerId);

    List<Score> findAllByUserAuthIdAndDeletedFalse(Long userAuthId);

}
