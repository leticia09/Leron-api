package com.leron.api.mapper.userAuth;

import com.leron.api.model.DTO.userAuth.UserAuthRequest;
import com.leron.api.model.DTO.userAuth.UserAuthRespose;
import com.leron.api.model.entities.UserAuthEntity;
import com.leron.api.responses.DataListResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserAuthMapper {
    public static DataListResponse<UserAuthRespose> userEntitiesToDataListResponse(List<UserAuthEntity> userEntities){
        DataListResponse<UserAuthRespose> response = new DataListResponse<>();
        List<UserAuthRespose> responseList = new ArrayList<>();

        for (UserAuthEntity user : userEntities) {
            UserAuthRespose userDTO = new UserAuthRespose();

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
        return user;
    }

    public static UserAuthRespose createUserResponse  (UserAuthEntity user) {
        UserAuthRespose userResponse = new UserAuthRespose();
        userResponse.setPassword(user.getPassword());
        userResponse.setName(user.getName());
        userResponse.setLogin(user.getLogin());
        userResponse.setId(userResponse.getId());
        userResponse.setCpf(userResponse.getCpf());
        userResponse.setSex(userResponse.getSex());
        return userResponse;
    }
}
