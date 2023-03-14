package com.hike.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "comentarii_blog")
public class BlogComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @CreationTimestamp
    private LocalDateTime createdOn;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "postare_id", referencedColumnName = "id")
    private BlogPost postare;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private UserEntity user;
}
