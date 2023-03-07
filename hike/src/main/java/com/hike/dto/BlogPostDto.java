package com.hike.dto;

import com.hike.models.BlogCategory;
import com.hike.models.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

public class BlogPostDto {
    private Long id;

    @Length(min = 3, message = "Titlul este prea scurt pentru a fi valid.")
    private String titlu;

    @NotEmpty(message = "Descrierea este obligatorie.")
    private String descriere;

    @NotEmpty(message = "Conținutul este obligatoriu.")
    private String text;

    @Lob
    private byte[] pozaCopertă;

    private LocalDateTime createdOn;

    @NotEmpty(message = "Categoria este obligatorie.")
    private BlogCategory categorie;

    private UserEntity user;
}
