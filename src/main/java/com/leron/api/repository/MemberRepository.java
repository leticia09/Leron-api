package com.leron.api.repository;

import com.leron.api.model.entities.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findAllByUserAuthIdAndDeletedFalseOrderByNameAsc(@Param("userAuthId") Long userAuthId);

    List<Member> findAllByUserAuthIdAndDeletedFalseAndStatusOrderByNameAsc(@Param("userAuthId") Long userAuthId, @Param("status") String status);

    Member findMemberByIdAndUserAuthId(Long userAuthId, Long id);
}
