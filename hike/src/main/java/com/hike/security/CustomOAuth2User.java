package com.hike.security;

import com.hike.models.UserEntity;
import com.hike.service.UserService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private OAuth2User oAuth2User;

    private UserService userService;

    public CustomOAuth2User(OAuth2User oAuth2User, UserService userService) {
        this.oAuth2User = oAuth2User;
        this.userService = userService;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oAuth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return oAuth2User.getName();
    }

    public String getFullName(){
        return oAuth2User.getAttribute("name");
    }

    public String getUsername(){
        return oAuth2User.getAttribute("name");
    }

    public String getEmail() {
        return oAuth2User.getAttribute("email");
    }

    public String getLastName() {
        return oAuth2User.getAttribute("family_name");
    }

    public String getFirstName(){
        return oAuth2User.getAttribute("given_name");
    }

    public String getGender(){
        return oAuth2User.getAttribute("gender");
    }

    public String getPhoto(){
        return oAuth2User.getAttribute("picture");
    }

    public String getProvider() {return "GOOGLE";}

    public boolean getBloggerRole() {
        UserEntity user = userService.findByUsername(this.getUsername());
        return user.getRoles().stream().anyMatch(role -> role.getName().equals("BLOGGER"));
    }
}
