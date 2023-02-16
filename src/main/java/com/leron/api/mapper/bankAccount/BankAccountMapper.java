package com.leron.api.mapper.bankAccount;

import com.leron.api.model.DTO.bankAccount.BankAccountRequest;
import com.leron.api.model.DTO.bankAccount.BankAccountResponse;
import com.leron.api.model.entities.BankAccountEntity;
import com.leron.api.model.entities.BankEntity;
import com.leron.api.model.entities.UserEntity;
import com.leron.api.responses.DataListResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class BankAccountMapper {
    public static DataListResponse<BankAccountResponse> bankAccountEntitiesToDataListResponse(List<BankAccountEntity> bankAccountEntities, List<BankEntity> bankEntityList, List<UserEntity> userEntityList){

        DataListResponse<BankAccountResponse> response = new DataListResponse<>();
        List<BankAccountResponse> responseList = new ArrayList<>();

        for (BankAccountEntity bankAccount : bankAccountEntities) {
            BankAccountResponse bankAccountResponse = new BankAccountResponse();
            bankEntityList.forEach(bank -> {
                if(bank.getId().equals(bankAccount.getIdBank())){
                    bankAccountResponse.setBankName(bank.getName());
                }
            });
            userEntityList.forEach(user -> {
                if(user.getId().equals(bankAccount.getIdUser())){
                    bankAccountResponse.setUserName(user.getName());
                }
            });
            bankAccountResponse.setId(bankAccount.getId());
            bankAccountResponse.setStatus(bankAccount.getStatus());
            bankAccountResponse.setAccountNumber(bankAccount.getAccountNumber());
            bankAccountResponse.setNickName(bankAccount.getNickName());
            bankAccountResponse.setIdUser(bankAccount.getIdUser());
            bankAccountResponse.setIdBank(bankAccount.getIdBank());
            bankAccountResponse.setIdBank(bankAccount.getIdBank());

            responseList.add(bankAccountResponse);
        }
        response.setData(responseList);

        return response;
    }

    public static BankAccountEntity createBankAccountFromBankAccountRequest(BankAccountRequest bankAccountRequest) {

        BankAccountEntity bankAccount = new BankAccountEntity();

        bankAccount.setStatus(bankAccountRequest.getStatus());
        bankAccount.setAccountNumber(bankAccountRequest.getAccountNumber());
        bankAccount.setNickName(bankAccountRequest.getNickName());
        bankAccount.setIdBank(bankAccountRequest.getIdBank());
        bankAccount.setIdUser(bankAccountRequest.getIdUser());
        bankAccount.setCreatedIn(new Date());

        return bankAccount;
    }

    public static BankAccountResponse createBankAccountResponse  (BankAccountEntity bank) {

        BankAccountResponse bankAccountResponse = new BankAccountResponse();

        bankAccountResponse.setStatus(bank.getStatus());
        bankAccountResponse.setIdBank(bank.getIdBank());
        bankAccountResponse.setIdUser(bank.getIdUser());
        bankAccountResponse.setAccountNumber(bank.getAccountNumber());
        bankAccountResponse.setNickName(bank.getNickName());

        return bankAccountResponse;
    }
}
