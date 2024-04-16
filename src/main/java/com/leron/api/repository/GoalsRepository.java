package com.leron.api.repository;

import com.leron.api.model.entities.Goals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoalsRepository extends JpaRepository<Goals, Long> {

    List<Goals> findAllByUserAuthIdAndDeletedFalse (Long userAuthId);
}
