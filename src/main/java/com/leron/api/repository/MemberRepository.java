package com.leron.api.repository;

import com.leron.api.model.entities.MemberEntity;
import com.leron.api.model.entities.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    @Query("SELECT b FROM MemberEntity b WHERE b.userAuthId = :userAuthId")
    List<MemberEntity> findByUserAuthId(@Param("userAuthId") Long userAuthId);
}
