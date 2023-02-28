package com.leron.api.mapper.salary;

import com.leron.api.model.DTO.card.CardDTO;
import com.leron.api.model.DTO.card.CardRequest;
import com.leron.api.model.DTO.card.CardResponse;
import com.leron.api.model.DTO.salary.SalaryRequest;
import com.leron.api.model.DTO.salary.SalaryResponse;
import com.leron.api.model.entities.BankEntity;
import com.leron.api.model.entities.CardEntity;
import com.leron.api.model.entities.SalaryEntity;
import com.leron.api.model.entities.UserEntity;
import com.leron.api.responses.DataListResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class SalaryMapper {
    public static DataListResponse<SalaryResponse> salaryEntitiesToDataListResponse(List<SalaryEntity> salaryEntityList, List<UserEntity> userEntityList){

        DataListResponse<SalaryResponse> response = new DataListResponse<>();
        List<SalaryResponse> responseList = new ArrayList<>();

        for (SalaryEntity salary : salaryEntityList) {
            SalaryResponse salaryResponse = new SalaryResponse();
            userEntityList.forEach(user -> {
                if(user.getId().equals(salary.getUserId())){
                    salaryResponse.setUserName(user.getName());
                }
            });
            salaryResponse.setType(salary.getType());
            salaryResponse.setId(salary.getId());
            salaryResponse.setStatus(salary.getStatus());
            salaryResponse.setName(salary.getName());
            salaryResponse.setPrice(salary.getPrice());

            responseList.add(salaryResponse);
        }
        response.setData(responseList);

        return response;
    }

    public static SalaryEntity createSalaryFromSalaryRequest(SalaryRequest salaryRequest) {

        SalaryEntity salary = new SalaryEntity();

        salary.setStatus(salaryRequest.getStatus());
        salary.setName(salaryRequest.getName());
        salary.setUserId(salaryRequest.getUserId());
        salary.setPrice(salaryRequest.getPrice());
        salary.setType(salaryRequest.getType());

        salary.setCreatedIn(new Date());

        return salary;
    }

    public static SalaryResponse createSalaryResponse  (SalaryEntity salary) {

        SalaryResponse salaryResponse = new SalaryResponse();

        salaryResponse.setType(salary.getType());
        salaryResponse.setStatus(salary.getStatus());
        salaryResponse.setUserId(salary.getUserId());
        salaryResponse.setId(salary.getId());
        salaryResponse.setPrice(salary.getPrice());
        salaryResponse.setName(salary.getName());

        return salaryResponse;
    }
}