package com.leron.api.mapper.UserAuthMapper;

import com.leron.api.model.DTO.user.UserDTO;
import com.leron.api.model.DTO.user.UserRequest;
import com.leron.api.model.DTO.user.UserResponse;
import com.leron.api.model.DTO.userAuth.UserAuthRequest;
import com.leron.api.model.DTO.userAuth.UserAuthRespose;
import com.leron.api.model.entities.UserAuthEntity;
import com.leron.api.model.entities.UserEntity;
import com.leron.api.responses.DataListResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class UserAuthMapper {
    public static DataListResponse<UserAuthRespose> userEntitiesToDataListResponse(List<UserAuthEntity> userEntities){
        DataListResponse<UserAuthRespose> response = new DataListResponse<>();
        List<UserAuthRespose> responseList = new ArrayList<>();

        for (UserAuthEntity user : userEntities) {
            UserAuthRespose userDTO = new UserAuthRespose();

            userDTO.setId(user.getId());
            userDTO.setLogin(user.getLogin());
            userDTO.setPassword(user.getPassword());
            responseList.add(userDTO);
        }
        response.setData(responseList);
        return response;
    }

    public static UserAuthEntity createUserFromUserRequest(UserAuthRequest userRequest) {
        UserAuthEntity user = new UserAuthEntity();
        user.setLogin(userRequest.getLogin());
        user.setPassword(userRequest.getPassword());
        return user;
    }

    public static UserAuthRespose createUserResponse  (UserAuthEntity user) {
        UserAuthRespose userResponse = new UserAuthRespose();
        userResponse.setPassword(user.getPassword());
        userResponse.setLogin(user.getLogin());
        userResponse.setId(userResponse.getId());
        return userResponse;
    }
}
