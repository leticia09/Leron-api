package com.leron.api.repository;

import com.leron.api.model.entities.MacroGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MacroGroupRepository extends JpaRepository<MacroGroupEntity, Long> {

}
