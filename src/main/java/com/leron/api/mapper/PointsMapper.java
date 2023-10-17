package com.leron.api.mapper;

import com.leron.api.model.DTO.points.PointsRequest;
import com.leron.api.model.DTO.points.PointsResponse;
import com.leron.api.model.entities.PointsEntity;
import com.leron.api.responses.DataListResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PointsMapper {
//    public static DataListResponse<PointsResponse> pointsEntitiesToDataListResponse(List<PointsEntity> entityList, List<BankEntity> bankEntityList, List<BankAccountEntity> bankAccountEntityList){
//        DataListResponse<PointsResponse> response = new DataListResponse<>();
//        List<PointsResponse> responseList = new ArrayList<>();
//
//        for (PointsEntity entity : entityList) {
//            PointsResponse responses = new PointsResponse();
//
//            bankEntityList.forEach(bank -> {
//                if(bank.getId().equals(entity.getBankId())){
//                    responses.setBankId(bank.getId());
//                    responses.setBankName(bank.getName());
//                }
//            });
//
//            bankAccountEntityList.forEach(account -> {
//                if(account.getId().equals(entity.getAccountId())){
//                    responses.setAccountId(account.getId());
//                    responses.setAccountName(account.getNickName());
//                    responses.setAccountNumber(account.getAccountNumber());
//                }
//            });
//
//            responses.setId(entity.getId());
//            responses.setProgram(entity.getProgram());
//            responses.setCurrency(entity.getCurrency());
//            responses.setPoint(entity.getPoint());
//            responses.setStatus(entity.getStatus());
//            responses.setUserAuthId(entity.getUserAuthId());
//            responseList.add(responses);
//
//        }
//        response.setData(responseList);
//        return response;
//    }

    public static PointsEntity createPointsFromRevenueRequest(PointsRequest request) {
        PointsEntity entity = new PointsEntity();

        entity.setUserAuthId(request.getUserAuthId());
        entity.setPoint(request.getPoint());
        entity.setCurrency(request.getCurrency());
        entity.setProgram(request.getProgram());
        entity.setAccountId(request.getAccountId());
        entity.setBankId(request.getBankId());
        entity.setStatus(request.getStatus());

        return entity;
    }

    public static PointsResponse createPointsResponse (PointsEntity entity) {

        PointsResponse response = new PointsResponse();
        response.setPoint(entity.getPoint());
        response.setProgram(entity.getProgram());
        response.setStatus(entity.getStatus());
        response.setId(entity.getId());
        response.setAccountId(entity.getAccountId());
        response.setBankId(entity.getBankId());
        response.setBankId(entity.getBankId());

        return response;
    }
}
