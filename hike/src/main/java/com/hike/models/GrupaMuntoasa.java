package com.hike.models;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "grupe_muntoase")
public class GrupaMuntoasa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titlu;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] pozaHarta;
}
