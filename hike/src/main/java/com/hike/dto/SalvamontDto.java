package com.hike.dto;

import com.hike.models.GrupaMuntoasa;
import com.hike.validator.OnlyDigits;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalvamontDto {
    private Long id;
    private String titlu;

    @OnlyDigits(message = "Numărul de telefon trebuie sa contina numai cifre.")
    @Length(max = 10, min = 10, message = "Numărul de telefon trebuie să aibă 10 cifre.")
    private String telefon;

    @NotNull(message = "Grupa este obligatorie!")
    private Long grupaMuntoasaId;

    private GrupaMuntoasa grupaMuntoasa;
}
