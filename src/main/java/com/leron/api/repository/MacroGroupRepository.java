package com.leron.api.repository;

import com.leron.api.model.entities.MacroGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MacroGroupRepository extends JpaRepository<MacroGroupEntity, Long> {
    @Query(value = "SELECT new com.leron.api.model.entities.MacroGroupEntity(u.id,u.name) FROM MacroGroupEntity u " +
            "WHERE u.userAuthId = :id ")
    List<MacroGroupEntity> findAllByAuthUserId(Long id);

}
