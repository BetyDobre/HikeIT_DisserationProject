package com.hike.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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

    private boolean aprobat;

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

    @OneToMany(mappedBy = "traseu", cascade = CascadeType.REMOVE)
    private Set<TraseuComment> comentarii;

    @ManyToMany(mappedBy = "traseeParcurse")
    private List<UserEntity> usersParcurs;

    @CreationTimestamp
    private LocalDateTime createdOn;

    @UpdateTimestamp
    private LocalDateTime updatedOn;
}
