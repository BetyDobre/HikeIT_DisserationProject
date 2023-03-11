package com.hike.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "categorii_blog")
public class BlogCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titlu;

    @OneToMany(mappedBy = "categorie", cascade = CascadeType.REMOVE)
    private List<BlogPost> postari;
}
