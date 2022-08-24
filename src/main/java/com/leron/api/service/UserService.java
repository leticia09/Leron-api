package com.leron.api.service;

import com.leron.api.model.UserModel;
import com.leron.api.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Long getUser(){
        UserModel userModel = userRepository.save(new UserModel());
        return userModel.getId();
    }
}
