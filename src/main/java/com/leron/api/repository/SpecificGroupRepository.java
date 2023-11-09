package com.leron.api.repository;

import com.leron.api.model.entities.SpecificGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecificGroupRepository extends JpaRepository<SpecificGroup, Long> {
}
