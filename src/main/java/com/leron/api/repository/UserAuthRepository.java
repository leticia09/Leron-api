package com.leron.api.repository;

import com.leron.api.model.entities.UserAuthEntity;
import com.leron.api.model.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserAuthRepository extends JpaRepository<UserAuthEntity, Long> {
    public Optional<UserAuthEntity> findByLogin(String login);

    @Query(value = "SELECT u.login,u.password FROM UserAuthEntity u " +
            "WHERE u.login = :login " +
            "AND u.password = :password ")
    List<String> validate(String login, String password);

}
