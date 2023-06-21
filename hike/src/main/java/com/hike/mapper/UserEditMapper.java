package com.hike.mapper;

import com.hike.dto.UserDto;
import com.hike.models.UserEntity;

public class UserEditMapper {
        public static UserEntity mapToUser(UserDto userDto){
            UserEntity user = UserEntity.builder()
                    .id(userDto.getId())
                    .nume(userDto.getNume())
                    .prenume(userDto.getPrenume())
                    .email(userDto.getEmail())
                    .username(userDto.getUsername())
                    .dataNastere(userDto.getDataNastere())
                    .sex(userDto.getSex())
                    .authProvider(userDto.getAuthProvider())
                    .newsletter(userDto.isNewsletter())
                    .pozaProfil(userDto.getPozaProfil())
                    .resetParolaToken(userDto.getResetParolaToken())
                    .createdOn(userDto.getCreatedOn())
                    .parola(userDto.getParola())
                    .roles(userDto.getRoles())
                    .pozaGoogle(userDto.getPozaGoogle())
                    .traseeParcurse(userDto.getTraseeParcurse())
                    .build();
            return user;
        }

        public static UserDto mapToUserDto(UserEntity user){
            UserDto userDto = UserDto.builder()
                    .id(user.getId())
                    .nume(user.getNume())
                    .prenume(user.getPrenume())
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .dataNastere(user.getDataNastere())
                    .sex(user.getSex())
                    .authProvider(user.getAuthProvider())
                    .newsletter(user.isNewsletter())
                    .pozaProfil(user.getPozaProfil())
                    .resetParolaToken(user.getResetParolaToken())
                    .createdOn(user.getCreatedOn())
                    .parola(user.getParola())
                    .roles(user.getRoles())
                    .pozaGoogle(user.getPozaGoogle())
                    .traseeParcurse(user.getTraseeParcurse())
                    .build();
            return userDto;
        }
}

