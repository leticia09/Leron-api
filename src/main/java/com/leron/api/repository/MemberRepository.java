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
    List<MemberEntity> findAllByUserAuthIdAndDeletedFalseOrderByNameAsc(@Param("userAuthId") Long userAuthId);

    MemberEntity findMemberByIdAndUserAuthId(Long userAuthId, Long id);
}
