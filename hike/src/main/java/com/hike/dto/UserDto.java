package com.hike.dto;

import com.hike.models.AuthProvider;
import com.hike.models.Role;
import com.hike.validator.OnlyLetters;
import com.hike.validator.OnlyLettersAndDigits;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class UserDto {
    private Long id;

    @Length(min = 4, max = 100, message = "Numele trebuie să aibă între 4 și 100 de caractere.")
    @OnlyLetters(message = "Numele trebuie să conțină doar litere.")
    private String nume;

    @Length(min = 3, max = 100, message = "Prenumele trebuie să fie între 3 și 100 de caractere")
    @OnlyLetters(message = "Prenumele trebuie să conțină doar litere.")
    private String prenume;

    @Length(min = 4, max = 100, message = "Numele de utilizator trebuie să aibă între 4 și 100 de caractere")
    @OnlyLettersAndDigits(message = "Numele de utilizator trebuie să conțină numai litere și/sau cifre.")
    private String username;

    @NotEmpty(message = "Acest câmp este obligatoriu.")
    @Email(message = "Email-ul introdus nu este valid.")
    private String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dataNastere;

    @NotEmpty(message = "Acest câmp este obligatoriu")
    private String sex;

    @Lob
    private byte[] pozaProfil;

    private String pozaGoogle;

    private String parola;

    private List<Role> roles;

    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

    private boolean newsletter;

    private String resetParolaToken;

    private LocalDateTime createdOn;
}
