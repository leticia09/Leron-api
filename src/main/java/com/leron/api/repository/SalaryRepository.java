package com.leron.api.repository;

import com.leron.api.model.entities.SalaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SalaryRepository extends JpaRepository<SalaryEntity, Long> {

}
