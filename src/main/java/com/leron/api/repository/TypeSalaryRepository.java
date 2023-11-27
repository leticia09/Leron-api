package com.leron.api.repository;

import com.leron.api.model.entities.TypeSalary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TypeSalaryRepository extends JpaRepository<TypeSalary, Long> {
    List<TypeSalary> findAllByUserAuthId(Long userAuthId);

}
