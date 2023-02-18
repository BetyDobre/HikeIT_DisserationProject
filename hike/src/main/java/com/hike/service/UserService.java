package com.hike.service;

import com.hike.dto.RegistrationDto;
import com.hike.models.UserEntity;

public interface UserService {

    void saveUser(RegistrationDto registrationDto);

    UserEntity findByEmail(String email);

    UserEntity findByUsername(String username);
}
