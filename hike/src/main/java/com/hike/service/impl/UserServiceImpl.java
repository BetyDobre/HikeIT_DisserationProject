package com.hike.service.impl;

import com.hike.dto.RegistrationDto;
import com.hike.models.AuthProvider;
import com.hike.models.Role;
import com.hike.models.UserEntity;
import com.hike.repository.RoleRepository;
import com.hike.repository.UserRepository;
import com.hike.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Locale;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(RegistrationDto registrationDto) {
        UserEntity user = new UserEntity();

        user.setEmail(registrationDto.getEmail());
        user.setNume(registrationDto.getNume());
        user.setPrenume(registrationDto.getPrenume());
        user.setUsername(registrationDto.getUsername());
        user.setDataNastere(registrationDto.getDataNastere());
        user.setPozaProfil(registrationDto.getPozaProfil());
        user.setSex(registrationDto.getSex());
        user.setParola(passwordEncoder.encode(registrationDto.getParola()));
        user.setAuthProvider(AuthProvider.LOCAL);

        Role role = roleRepository.findByName("USER");
        user.setRoles(Arrays.asList(role));

        userRepository.save(user);
    }

    @Override
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void processOAuthPostLogin(String username, String email, String nume, String prenume, String sex, String poza) {
        UserEntity existUser = userRepository.findByUsername(username);

        if (existUser == null) {
            UserEntity newUser = new UserEntity();
            newUser.setUsername(username);
            newUser.setEmail(email);
            newUser.setNume(nume);
            newUser.setPrenume(prenume);
            newUser.setPozaGoogle(poza);
            if(sex != null){
                if(sex.toLowerCase().equals("male")){
                    newUser.setSex("M");
                }
                else if(sex.toLowerCase().equals("female")){
                    newUser.setSex("F");
                }
            }
            else {
                newUser.setSex("N");
            }
            newUser.setAuthProvider(AuthProvider.GOOGLE);

            Role role = roleRepository.findByName("USER");
            newUser.setRoles(Arrays.asList(role));

            userRepository.save(newUser);
        }
    }
}
