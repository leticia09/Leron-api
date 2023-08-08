package com.leron.api.repository;

import com.leron.api.model.entities.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    @Query(value = "SELECT new com.leron.api.model.entities.MemberEntity(u.id,name) FROM MemberEntity u " +
            "WHERE u.userAuthId = :id ")
    List<MemberEntity> findAllByAuthUserId(Long id);
}
