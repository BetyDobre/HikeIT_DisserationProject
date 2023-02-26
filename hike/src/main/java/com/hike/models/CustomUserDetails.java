package com.hike.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

@Setter
@Getter
public class CustomUserDetails extends User {
    private byte[] pozaProfil;
    private Long id;
    private String fullName;
    private String nume;
    private String prenume;
    private String provider;

    public CustomUserDetails(Long id,String username, String password, Collection<? extends GrantedAuthority> authorities, byte[] pozaProfil, String nume, String prenume, String fullName, String provider) {
        super(username, password, authorities);
        this.id = id;
        this.pozaProfil = pozaProfil;
        this.nume = nume;
        this.prenume = prenume;
        this.fullName = fullName;
        this.provider = provider;
    }
}
