package com.leron.api.mapper.user;

import com.leron.api.model.DTO.user.UserDTO;
import com.leron.api.model.DTO.user.UserRequest;
import com.leron.api.model.DTO.user.UserResponse;
import com.leron.api.model.entities.UserEntity;
import com.leron.api.responses.DataListResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {
    public static DataListResponse<UserDTO> userEntitiesToDataListResponse(List<UserEntity> userEntities){
        DataListResponse<UserDTO> response = new DataListResponse<>();
        List<UserDTO> responseList = new ArrayList<>();

        for (UserEntity user : userEntities) {
            UserDTO userDTO = new UserDTO();

            userDTO.setId(user.getId());
            userDTO.setName(user.getName());
            userDTO.setCpf(user.getCpf());
            userDTO.setEmail(user.getEmail());
            userDTO.setPermissao(user.getPermissao());

            responseList.add(userDTO);
        }
        response.setData(responseList);
        return response;
    }

    public static UserEntity createUserFromUserRequest(UserRequest userRequest) {
        UserEntity user = new UserEntity();
        user.setCpf(userRequest.getCpf());
        user.setEmail(userRequest.getEmail());
        user.setName(userRequest.getName());
        user.setPermissao(userRequest.getPermissao());
        return user;
    }

    public static UserResponse createUserResponse  (UserEntity user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setCpf(user.getCpf());
        userResponse.setEmail(user.getEmail());
        userResponse.setName(user.getName());
        userResponse.setPermissao(user.getPermissao());
        userResponse.setId(userResponse.getId());
        return userResponse;
    }
}
