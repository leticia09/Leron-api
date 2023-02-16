package com.leron.api.mapper.bank;

import com.leron.api.model.DTO.bank.BankDTO;
import com.leron.api.model.DTO.bank.BankRequest;
import com.leron.api.model.DTO.bank.BankResponse;
import com.leron.api.model.entities.BankEntity;
import com.leron.api.responses.DataListResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class BankMapper {
    public static DataListResponse<BankDTO> bankEntitiesToDataListResponse(List<BankEntity> bankEntities){
        DataListResponse<BankDTO> response = new DataListResponse<>();
        List<BankDTO> responseList = new ArrayList<>();

        for (BankEntity bank : bankEntities) {
            BankDTO bankDTO = new BankDTO();

            bankDTO.setId(bank.getId());
            bankDTO.setName(bank.getName());
            bankDTO.setStatus(bank.getStatus());

            responseList.add(bankDTO);
        }
        response.setData(responseList);

        return response;
    }

    public static BankEntity createBankFromBankRequest(BankRequest userRequest) {
        BankEntity bank = new BankEntity();
        bank.setStatus(userRequest.getStatus());
        bank.setName(userRequest.getName());
        bank.setCreatedIn(new Date());

        return bank;
    }

    public static BankResponse createBankResponse  (BankEntity user) {
        BankResponse bankResponse = new BankResponse();
        bankResponse.setStatus(user.getStatus());
        bankResponse.setName(user.getName());
        bankResponse.setId(user.getId());

        return bankResponse;
    }
}
