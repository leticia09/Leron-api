package com.leron.api.repository;

import com.leron.api.model.entities.Entrance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntranceRepository extends JpaRepository<Entrance, Long> {

    List<Entrance> findAllByUserAuthIdAndDeletedFalse (Long userAuthId);
}
