package com.hike.dto;

import com.hike.models.GrupaMuntoasa;
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
    private String telefon;

    @NotNull(message = "Grupa este obligatorie!")
    private Long grupaMuntoasaId;

    private GrupaMuntoasa grupaMuntoasa;
}
