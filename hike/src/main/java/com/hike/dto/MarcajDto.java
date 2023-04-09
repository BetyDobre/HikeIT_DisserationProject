package com.hike.dto;

import com.hike.validator.OnlyLetters;
import jakarta.persistence.Lob;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class MarcajDto {
    private Long id;

    @Length(min = 3, message = "Titlul este prea scurt pentru a fi valid.")
    private String titlu;

    @Lob
    private byte[] marcaj;
}
