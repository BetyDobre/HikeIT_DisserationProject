package com.hike.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "stiri")
public class Stire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titlu;
    private String url;
    private String website;
    private String createdOn;
    private String rezumat;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name="traseu_id", referencedColumnName = "id")
    private Traseu traseu;
}
