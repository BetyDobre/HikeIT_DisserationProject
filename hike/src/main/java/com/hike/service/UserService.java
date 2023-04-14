package com.hike.service;

import com.hike.dto.RegistrationDto;
import com.hike.dto.UserDto;
import com.hike.exception.ObjectNotFoundException;
import com.hike.models.Traseu;
import com.hike.models.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {

    void saveUser(RegistrationDto registrationDto);

    void save(UserEntity user);

    UserEntity findByEmail(String email);

    UserEntity findByUsername(String username);

    Optional<UserEntity> findById(Long id);

    void processOAuthPostLogin(String username, String email, String nume, String prenume, String sex, String poza);

    void schimbaParolaToken(String token, String email) throws ObjectNotFoundException;

    void schimbaParola(UserEntity user, String newPassword);

    UserEntity findByToken(String token);

    void updateUser(UserDto userDto);

    void delete(Long id);

    Page<UserEntity> getAllUsers(Pageable pageable);

    Page<Traseu> getTraseeParcurseByUser(UserEntity user, Pageable pageable);

    int countTraseeParcurseByUser(UserEntity user);
}
