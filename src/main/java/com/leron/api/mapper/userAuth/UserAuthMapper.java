package com.leron.api.mapper.userAuth;

import com.leron.api.model.DTO.userAuth.UserAuthRequest;
import com.leron.api.model.DTO.userAuth.UserAuthResponse;
import com.leron.api.model.entities.UserAuthEntity;
import com.leron.api.responses.DataListResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class UserAuthMapper {
    public static DataListResponse<UserAuthResponse> userEntitiesToDataListResponse(List<UserAuthEntity> userEntities){
        DataListResponse<UserAuthResponse> response = new DataListResponse<>();
        List<UserAuthResponse> responseList = new ArrayList<>();

        for (UserAuthEntity user : userEntities) {
            UserAuthResponse userDTO = new UserAuthResponse();

            userDTO.setId(user.getId());
            userDTO.setName(user.getName());
            userDTO.setLogin(user.getLogin());
            userDTO.setPassword(user.getPassword());
            userDTO.setCpf(user.getCpf());
            userDTO.setSex(user.getSex());
            responseList.add(userDTO);
        }
        response.setData(responseList);
        return response;
    }

    public static UserAuthEntity createUserFromUserRequest(UserAuthRequest userRequest) {
        UserAuthEntity user = new UserAuthEntity();
        user.setName(userRequest.getName());
        user.setLogin(userRequest.getLogin());
        user.setPassword(userRequest.getPassword());
        user.setCpf(userRequest.getCpf());
        user.setSex(userRequest.getSex());
        user.setDeleted(false);
        user.setCreatedIn(new Date());
        return user;
    }

    public static UserAuthResponse createUserResponse  (UserAuthEntity user) {
        UserAuthResponse userResponse = new UserAuthResponse();
        userResponse.setPassword(user.getPassword());
        userResponse.setName(user.getName());
        userResponse.setLogin(user.getLogin());
        userResponse.setId(userResponse.getId());
        userResponse.setCpf(userResponse.getCpf());
        userResponse.setSex(userResponse.getSex());
        return userResponse;
    }
}
