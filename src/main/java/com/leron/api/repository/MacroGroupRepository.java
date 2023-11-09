package com.leron.api.repository;

import com.leron.api.model.entities.Card;
import com.leron.api.model.entities.MacroGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MacroGroupRepository extends JpaRepository<MacroGroup, Long> {
    List<MacroGroup> findAllByUserAuthIdAndDeletedFalseOrderByNameAsc(Long userAuthId);

}
