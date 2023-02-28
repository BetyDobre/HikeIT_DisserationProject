package com.hike.service.impl;

import com.hike.dto.RegistrationDto;
import com.hike.dto.UserDto;
import com.hike.exception.ObjectNotFoundException;
import com.hike.models.AuthProvider;
import com.hike.models.Role;
import com.hike.models.UserEntity;
import com.hike.repository.RoleRepository;
import com.hike.repository.UserRepository;
import com.hike.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.hike.mapper.UserEditMapper.mapToUser;

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

        user.setNewsletter(false);
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
    public Optional<UserEntity> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void processOAuthPostLogin(String username, String email, String nume, String prenume, String sex, String poza) {
        UserEntity existUser = userRepository.findByUsername(username);

        if (existUser == null) {
            UserEntity newUser = new UserEntity();
            newUser.setNewsletter(false);
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

    @Override
    public void schimbaParolaToken(String token, String email) throws ObjectNotFoundException {
        UserEntity user = userRepository.findByEmail(email);

        if (user != null){
            user.setResetParolaToken(token);
            userRepository.save(user);
        }
        else{
            throw new ObjectNotFoundException("Nu s-a putut gasi user-ul cu emailul " + email);
        }
    }

    @Override
    public void schimbaParola(UserEntity user, String newPassword){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String parolaCriptata = passwordEncoder.encode(newPassword);

        user.setParola(parolaCriptata);
        user.setResetParolaToken(null);

        userRepository.save(user);
    }

    @Override
    public UserEntity findByToken(String token) {
        return  userRepository.findByResetParolaToken(token);
    }

    @Override
    public void updateUser(UserDto userDto) {
        UserEntity user = mapToUser(userDto);
        userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }
}
