package com.leron.api.mapper.member;

import com.leron.api.model.DTO.user.UserRequest;
import com.leron.api.model.DTO.user.UserResponse;
import com.leron.api.model.entities.MemberEntity;
import com.leron.api.responses.DataListResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class MemeberMapper {
    public static DataListResponse<UserResponse> userEntitiesToDataListResponse(List<MemberEntity> userEntities){
        DataListResponse<UserResponse> response = new DataListResponse<>();
        List<UserResponse> responseList = new ArrayList<>();

        for (MemberEntity user : userEntities) {
            UserResponse userDTO = new UserResponse();

            userDTO.setId(user.getId());
            userDTO.setName(user.getName());
            userDTO.setCpf(user.getCpf());
            userDTO.setEmail(user.getEmail());
            userDTO.setPermission(user.getPermission());
            userDTO.setUserAuthId(user.getUserAuthId());

            responseList.add(userDTO);
        }
        response.setData(responseList);
        return response;
    }

    public static List<MemberEntity> createUserFromUserRequest(List<UserRequest> userRequest) {
        List<MemberEntity> entities = new ArrayList<>();
        userRequest.forEach( user -> {
            MemberEntity entity = new MemberEntity();
            entity.setCpf(user.getCpf());
            entity.setEmail(user.getEmail());
            entity.setName(user.getName());
            entity.setPermission(user.getPermission());
            entity.setCreatedIn(new Date());
            entity.setUserAuthId(user.getUserAuthId());
            entities.add(entity);
        });


        return entities;
    }

    public static List<UserResponse> createUserResponse  (List<MemberEntity> entities) {
        List<UserResponse> responses = new ArrayList<>();
        entities.forEach(entity -> {
            UserResponse userResponse = new UserResponse();
            userResponse.setCpf(entity.getCpf());
            userResponse.setEmail(entity.getEmail());
            userResponse.setName(entity.getName());
            userResponse.setPermission(entity.getPermission());
            userResponse.setId(userResponse.getId());
            userResponse.setUserAuthId(userResponse.getUserAuthId());

            responses.add(userResponse);
        });


        return responses;
    }
}
