package com.leron.api.repository;

import com.leron.api.model.entities.Money;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoneyRepository extends JpaRepository<Money, Long> {

    List<Money> findAllByUserAuthIdAndDeletedFalse (Long userAuthId);
}
