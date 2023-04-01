package com.hike.dto;

import com.hike.models.Dificultate;
import com.hike.models.GrupaMuntoasa;
import com.hike.models.Sezon;
import com.hike.models.UserEntity;
import com.hike.validator.OnlyDigits;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TraseuDto {
    private Long id;

    @NotEmpty(message = "Acest câmp este obligatoriu.")
    private String punctPlecare;

    @NotEmpty(message = "Acest câmp este obligatoriu.")
    private String punctSosire;

    @Length(min = 3, max = 13, message = "Menționați durata minimă și cea maximă a traselui.")
    private String durata;

    @NotEmpty(message = "Acest câmp este obligatoriu.")
    @OnlyDigits(message = "Distanța trebuie să fie un număr ce reprezintă km.")
    private Long distanta;

    @NotEmpty(message = "Acest câmp este obligatoriu.")
    @OnlyDigits(message = "Urcarea trebuie să fie un număr ce reprezintă metri.")
    private Long urcare;

    @NotEmpty(message = "Acest câmp este obligatoriu.")
    @OnlyDigits(message = "Coborârea trebuie să fie un număr ce reprezintă metri.")
    private Long coborare;

    @NotEmpty(message = "Trebuie să existe o descriere a traseului.")
    @Length(min = 10, max = 4000, message = "Descrierea trebuie să aibă între 10 și 4000 de caractere.")
    private String descriere;

    private String marcaj;

    @NotEmpty(message = "Acest câmp este obligatoriu")
    @Enumerated(EnumType.STRING)
    private Dificultate dificultate;

    @NotEmpty(message = "Acest câmp este obligatoriu")
    @Enumerated(EnumType.STRING)
    private Sezon sezon;

    @Lob
    private List<byte[]> pozeTraseu;

    @NotEmpty(message = "Acest câmp este obligatoriu")
    private GrupaMuntoasa grupaMuntoasa;

    private UserEntity user;

    private boolean aprobat;

    @NotNull(message = "Grupa este obligatorie!")
    private Long grupaMuntoasaId;
}
