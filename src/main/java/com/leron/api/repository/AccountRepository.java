package com.leron.api.repository;

import com.leron.api.model.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findAllByUserAuthIdAndDeletedFalse(@Param("userAuthId") Long userAuthId);

}
