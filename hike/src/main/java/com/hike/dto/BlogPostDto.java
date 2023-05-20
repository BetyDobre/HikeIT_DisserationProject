package com.hike.dto;

import com.hike.models.BlogCategory;
import com.hike.models.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogPostDto {
    private Long id;

    @Length(min = 3, message = "Titlul este prea scurt pentru a fi valid.")
    private String titlu;

    @NotEmpty(message = "Descrierea este obligatorie.")
    @Length(min = 10, max = 1000, message = "Descrierea trebuie să aibă între 10 și 1000 de caractere.")
    private String descriere;

    @NotEmpty(message = "Conținutul este obligatoriu.")
    @Length(min = 10, max = 30000, message = "Conținutul trebuie să aibă între 10 și 30000 de caractere.")
    private String text;

    @Lob
    private byte[] pozaCoperta;

    private LocalDateTime createdOn;

    @NotNull(message = "Categoria este obligatorie!")
    private Long categorieId;

    private UserEntity user;
}
