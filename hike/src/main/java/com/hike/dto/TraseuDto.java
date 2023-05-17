package com.hike.dto;

import com.hike.models.*;
import com.hike.validator.OnlyDigits;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TraseuDto {
    private Long id;

    @NotEmpty(message = "Acest câmp este obligatoriu.")
    @Length(max = 100, message = "Titlul trebuie să aibă maxim 100 de caractere.")
    private String titlu;

    @NotEmpty(message = "Punctul de plecare este obligatoriu.")
    private String punctPlecare;

    @NotEmpty(message = "Punctul de sosire este obligatoriu.")
    private String punctSosire;

    @Length(min = 3, max = 13, message = "Menționați durata minimă a traselui.")
    private String durataMinima;

    @Length(min = 3, max = 13, message = "Menționați durata maximă a traselui.")
    private String durataMaxima;

    @NotNull(message = "Acest câmp este obligatoriu.")
    @Digits(integer = 3, fraction = 0, message = "Doar cifre permise")
    private Long distanta;

    @NotNull(message = "Acest câmp este obligatoriu.")
    @Digits(integer = 4, fraction = 0, message = "Doar cifre permise")
    private Long urcare;

    @NotNull(message = "Acest câmp este obligatoriu.")
    @Digits(integer = 4, fraction = 0, message = "Doar cifre permise")
    private Long coborare;

    @NotEmpty(message = "Trebuie să existe o descriere a traseului.")
    @Length(min = 10, max = 10000, message = "Descrierea trebuie să aibă între 10 și 4000 de caractere.")
    private String descriere;

    private Marcaj marcaj;

    @Enumerated(EnumType.STRING)
    private Dificultate dificultate;

    @Enumerated(EnumType.STRING)
    private Sezon sezon;

    @Lob
    private List<byte[]> pozeTraseu;

    private GrupaMuntoasa grupaMuntoasa;

    private UserEntity user;

    private boolean aprobat;

    @NotNull(message = "Grupa este obligatorie!")
    private Long grupaMuntoasaId;

    @NotNull(message = "Marcajul este obligatoriu!")
    private Long marcajId;

    private LocalDateTime createdOn;

    private LocalDateTime updatedOn;

}
