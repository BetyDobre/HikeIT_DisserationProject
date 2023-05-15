package com.hike.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "postari_blog")
public class BlogPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titlu;
    private String descriere;
    private String text;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] pozaCoperta;

    @CreationTimestamp
    private LocalDateTime createdOn;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "categorie_id", referencedColumnName = "id")
    private BlogCategory categorie;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private UserEntity user;

    @OneToMany(mappedBy = "postare", cascade = CascadeType.REMOVE)
    private Set<BlogComment> comentarii;
}
