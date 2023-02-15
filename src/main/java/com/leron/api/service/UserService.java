package com.leron.api.service;

import com.leron.api.mapper.user.UserMapper;
import com.leron.api.model.UserDTO;
import com.leron.api.model.UserRequest;
import com.leron.api.model.UserResponse;
import com.leron.api.model.entities.UserEntity;
import com.leron.api.repository.UserRepository;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.DataRequest;
import com.leron.api.responses.DataResponse;
import com.leron.api.validator.user.UserValidator;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public DataListResponse<UserDTO> getUser(){
        return UserMapper.userEntitiesToDataListResponse(userRepository.findAll());
    }

    public DataResponse<UserResponse> create(DataRequest<UserRequest> userRequest,
                                             String locale,
                                             String authorization) throws ApplicationBusinessException {
        DataResponse<UserResponse> response = new DataResponse<>();
        UserValidator.validatorUser(userRequest);

        UserEntity user = UserMapper.createUserFromUserRequest(userRequest.getData());
        userRepository.save(user);
        UserResponse userResponse = UserMapper.createUserResponse(user);
        response.setData(userResponse);
        response.setMessage("Sucesso");
        return response;
    }


}
