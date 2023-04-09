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
@Entity(name = "marcaje")
public class Marcaj {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titlu;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] marcaj;

    @OneToMany(mappedBy = "marcaj", cascade = CascadeType.REMOVE)
    private List<Traseu> trasee;
}
