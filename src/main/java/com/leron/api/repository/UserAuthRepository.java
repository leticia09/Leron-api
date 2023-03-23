package com.leron.api.repository;

import com.leron.api.model.DTO.userAuth.UserValidResponse;
import com.leron.api.model.entities.UserAuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserAuthRepository extends JpaRepository<UserAuthEntity, Long> {
    public Optional<UserAuthEntity> findByLogin(String login);

    @Query(value = "SELECT new com.leron.api.model.DTO.userAuth.UserValidResponse(u.name,u.login,u.password, u.sex) FROM UserAuthEntity u " +
            "WHERE u.login = :login " +
            "AND u.password = :password ")
    UserValidResponse validate(String login, String password);

}
