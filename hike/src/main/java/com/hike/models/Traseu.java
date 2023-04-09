package com.hike.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "trasee")
public class Traseu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titlu;
    private String punctPlecare;
    private String punctSosire;
    private String durataMinima;
    private String durataMaxima;
    private Long distanta;
    private Long urcare;
    private Long coborare;
    private String descriere;

    @Enumerated(EnumType.STRING)
    private Dificultate dificultate;

    @Enumerated(EnumType.STRING)
    private Sezon sezon;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private List<byte[]> pozeTraseu;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name="grupaMuntoasa_id", referencedColumnName = "id")
    private GrupaMuntoasa grupaMuntoasa;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name="marcaj_id", referencedColumnName = "id")
    private Marcaj marcaj;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private UserEntity user;

    private boolean aprobat;
}
