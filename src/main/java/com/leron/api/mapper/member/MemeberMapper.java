package com.leron.api.mapper.member;

import com.leron.api.model.DTO.user.MemberRequest;
import com.leron.api.model.DTO.user.MemberResponse;
import com.leron.api.model.entities.MemberEntity;
import com.leron.api.responses.DataListResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class MemeberMapper {
    public static DataListResponse<MemberResponse> userEntitiesToDataListResponse(List<MemberEntity> userEntities){
        DataListResponse<MemberResponse> response = new DataListResponse<>();
        List<MemberResponse> responseList = new ArrayList<>();

        for (MemberEntity user : userEntities) {
            MemberResponse userDTO = new MemberResponse();

            userDTO.setId(user.getId());
            userDTO.setName(user.getName());
            userDTO.setUserAuthId(user.getUserAuthId());

            responseList.add(userDTO);
        }
        response.setData(responseList);
        return response;
    }

    public static List<MemberEntity> createUserFromUserRequest(List<MemberRequest> userRequest) {
        List<MemberEntity> entities = new ArrayList<>();
        userRequest.forEach( user -> {
            MemberEntity entity = new MemberEntity();
            entity.setName(user.getName());
            entity.setCreatedIn(new Date());
            entity.setUserAuthId(user.getUserAuthId());
            entities.add(entity);
        });


        return entities;
    }

    public static List<MemberResponse> createUserResponse  (List<MemberEntity> entities) {
        List<MemberResponse> responses = new ArrayList<>();
        entities.forEach(entity -> {
            MemberResponse userResponse = new MemberResponse();
            userResponse.setName(entity.getName());
            userResponse.setId(userResponse.getId());
            userResponse.setUserAuthId(userResponse.getUserAuthId());

            responses.add(userResponse);
        });


        return responses;
    }
}
