package com.hike.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
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
