package com.hike.dto;

import com.hike.validator.OnlyLetters;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class BlogCategoryDto {
    private Long id;

    @Length(min = 3, message = "Titlul este prea scurt pentru a fi valid.")
    @OnlyLetters(message = "Titlul trebuie să conțină numai litere")
    private String titlu;
}
