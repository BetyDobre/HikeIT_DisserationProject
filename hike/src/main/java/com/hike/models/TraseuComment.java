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
@Entity(name = "comentarii_traseu")
public class TraseuComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @CreationTimestamp
    private LocalDateTime createdOn;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "traseu_id", referencedColumnName = "id")
    private Traseu traseu;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private UserEntity user;
}