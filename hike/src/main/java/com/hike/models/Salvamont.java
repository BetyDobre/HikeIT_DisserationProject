package com.hike.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "salvamont")
public class Salvamont {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titlu;
    private String telefon;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "grupaMuntoasa_id", referencedColumnName = "id")
    private GrupaMuntoasa grupaMuntoasa;
}
