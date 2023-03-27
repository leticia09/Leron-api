package com.leron.api.repository;

import com.leron.api.model.DTO.userAuth.UserValidResponse;
import com.leron.api.model.entities.BankAccountEntity;
import com.leron.api.model.entities.BankEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccountEntity, Long> {
    @Query(value = "SELECT new com.leron.api.model.entities.BankAccountEntity(u.id,u.nickName,u.accountNumber,u.status,u.idBank,u.idUser) FROM BankAccountEntity u " +
            "WHERE u.userAuthId = :id ")
    List<BankAccountEntity> findAllByAuthUserId(Long id);
    @Query(value = "SELECT new com.leron.api.model.entities.BankAccountEntity(u.id,u.nickName,u.accountNumber,u.status,u.idBank,u.idUser) FROM BankAccountEntity u " +
            "WHERE u.userAuthId = :id AND u.idBank = :bankId")
    List<BankAccountEntity> findAllByAuthUserIdAndBankId(Long id, Long bankId);

}
