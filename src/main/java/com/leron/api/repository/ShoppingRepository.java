package com.leron.api.repository;

import com.leron.api.model.entities.ShoppingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingRepository extends JpaRepository<ShoppingEntity, Long> {
}
