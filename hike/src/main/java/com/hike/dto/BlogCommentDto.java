package com.hike.dto;

import com.hike.models.BlogPost;
import com.hike.models.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogCommentDto {
    private Long id;

    @NotEmpty(message = "Textul este obligatoriu")
    private String text;

    private BlogPost postare;
    private UserEntity user;
    private LocalDateTime createdOn;
}
