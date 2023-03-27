package com.leron.api.repository;

import com.leron.api.model.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query(value = "SELECT new com.leron.api.model.entities.UserEntity(u.id,name,u.cpf,u.email,u.permissao) FROM UserEntity u " +
            "WHERE u.userAuthId = :id ")
    List<UserEntity> findAllByAuthUserId(Long id);
}
