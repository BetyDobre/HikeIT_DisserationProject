package com.hike.dto;

import com.hike.models.Traseu;
import com.hike.models.UserEntity;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TraseuCommentDto {
    private Long id;

    @NotEmpty(message = "Textul este obligatoriu")
    private String text;

    private Traseu traseu;
    private UserEntity user;
    private LocalDateTime createdOn;
}
