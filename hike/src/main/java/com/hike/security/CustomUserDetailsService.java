package com.hike.security;

import com.hike.models.CustomUserDetails;
import com.hike.models.UserEntity;
import com.hike.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findFirstByUsername(username);
        if(user !=  null){
            CustomUserDetails authUser = new CustomUserDetails(
                    user.getId(),
                    user.getUsername(),
                    user.getParola(),
                    user.getRoles().stream().map((role) -> new SimpleGrantedAuthority(role.getName()))
                            .collect(Collectors.toList()),
                    user.getPozaProfil(),
                    user.getNume(),
                    user.getPrenume(),
                    user.getPrenume() + " " + user.getNume(),
                    user.getAuthProvider().toString()
            );

            return authUser;
        }else {
            throw new UsernameNotFoundException("Username sau parola invalide.");
        }
    }
}
