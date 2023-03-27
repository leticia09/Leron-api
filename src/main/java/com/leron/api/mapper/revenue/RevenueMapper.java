package com.leron.api.mapper.revenue;



import com.leron.api.model.DTO.revenue.RevenueRequest;
import com.leron.api.model.DTO.revenue.RevenueResponse;
import com.leron.api.model.entities.*;
import com.leron.api.responses.DataListResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class RevenueMapper {
    public static DataListResponse<RevenueResponse> revenueEntitiesToDataListResponse(List<RevenueEntity> revenueEntityList, List<BankEntity> bankEntityList, List<BankAccountEntity> bankAccountEntityList, List<SalaryEntity> salaryEntityList){
        DataListResponse<RevenueResponse> response = new DataListResponse<>();
        List<RevenueResponse> responseList = new ArrayList<>();

        for (RevenueEntity revenue : revenueEntityList) {
            RevenueResponse revenueResponse = new RevenueResponse();

            bankEntityList.forEach(bank -> {
                if(bank.getId().equals(revenue.getBankId())){
                    revenueResponse.setBankId(bank.getId());
                    revenueResponse.setBankName(bank.getName());
                }
            });

            bankAccountEntityList.forEach(account -> {
                if(account.getId().equals(revenue.getAccountId())){
                    revenueResponse.setAccountId(account.getId());
                    revenueResponse.setAccountName(account.getNickName());
                    revenueResponse.setAccountNumber(account.getAccountNumber());
                }
            });

            salaryEntityList.forEach(salary -> {
                if(salary.getId().equals(revenue.getSalaryId())){
                    revenueResponse.setSalaryId(salary.getId());
                    revenueResponse.setSalaryName(salary.getName());
                }
            });

            revenueResponse.setId(revenue.getId());
            revenueResponse.setType(revenue.getType());
            revenueResponse.setDescription(revenue.getDescription());
            revenueResponse.setReceivingDate(revenue.getReceivingDate());
            revenueResponse.setValue(revenue.getValue());
            revenueResponse.setStatus(revenue.getStatus());
            revenueResponse.setUserAuthId(revenue.getUserAuthId());
            responseList.add(revenueResponse);
        }
        response.setData(responseList);
        return response;
    }

    public static RevenueEntity createRevenueFromRevenueRequest(RevenueRequest revenueRequest) {
        RevenueEntity revenueEntity = new RevenueEntity();

        revenueEntity.setDescription(revenueRequest.getDescription());
        revenueEntity.setType(revenueRequest.getType());
        revenueEntity.setSalaryId(revenueRequest.getSalaryId());
        revenueEntity.setReceivingDate(revenueRequest.getReceivingDate());
        revenueEntity.setValue(revenueRequest.getValue());
        revenueEntity.setStatus(revenueRequest.getStatus());
        revenueEntity.setBankId(revenueRequest.getBankId());
        revenueEntity.setAccountId(revenueRequest.getAccountId());
        revenueEntity.setUserAuthId(revenueRequest.getUserAuthId());


        return revenueEntity;
    }

    public static RevenueResponse createRevenueResponse (RevenueEntity entity) {

        RevenueResponse revenueResponse = new RevenueResponse();
        revenueResponse.setType(entity.getType());
        revenueResponse.setDescription(entity.getDescription());
        revenueResponse.setStatus(entity.getStatus());
        revenueResponse.setSalaryId(entity.getSalaryId());
        revenueResponse.setReceivingDate(entity.getReceivingDate());
        revenueResponse.setValue(entity.getValue());
        revenueResponse.setBankId(entity.getBankId());
        revenueResponse.setAccountId(entity.getAccountId());
        revenueResponse.setUserAuthId(entity.getUserAuthId());
        return revenueResponse;
    }
}
