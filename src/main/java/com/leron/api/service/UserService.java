package com.leron.api.service;

import com.leron.api.model.entities.UserEntity;
import com.leron.api.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Long getUser(){
        UserEntity userModel = userRepository.save(new UserEntity());
        return userModel.getId();
    }
}
