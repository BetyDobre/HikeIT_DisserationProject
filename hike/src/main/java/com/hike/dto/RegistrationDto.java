package com.hike.dto;

import com.hike.validator.OnlyLetters;
import com.hike.validator.OnlyLettersAndDigits;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Date;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class RegistrationDto {
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


    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{6,}$", message = "Parola trebuie să conțină minimum 6 caractere, o literă mică, o literă mare și un număr.")
    private String parola;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{6,}$", message = "Parola trebuie să conțină minimum 6 caractere, o literă mică, o literă mare și un număr.")
    private String confirmareParola;

    @NotEmpty(message = "Acest câmp este obligatoriu.")
    @Email(message = "Email-ul introdus nu este valid.")
    private String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dataNastere;

    @NotEmpty(message = "Acest câmp este obligatoriu")
    private String sex;

    @Lob
    private byte[] pozaProfil;
}
