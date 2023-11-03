package com.leron.api.repository;

import com.leron.api.model.entities.SpecificGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecificGroupRepository extends JpaRepository<SpecificGroup, Long> {
}
