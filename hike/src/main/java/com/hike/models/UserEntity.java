package com.hike.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nume;
    private String prenume;
    private String username;
    private String parola;
    private String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dataNastere;
    private String sex;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] pozaProfil;

    @CreationTimestamp
    private LocalDateTime createdOn;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "users_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}, uniqueConstraints = @UniqueConstraint(columnNames = {
            "user_id", "role_id"}))
    private List<Role> roles = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

    private String pozaGoogle;

    private String resetParolaToken;

    private boolean newsletter;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private Set<BlogPost> postari;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private Set<BlogComment> comentariiBlog;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private Set<Traseu> trasee;
}
