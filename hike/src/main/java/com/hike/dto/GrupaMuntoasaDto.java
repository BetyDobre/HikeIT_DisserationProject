package com.hike.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GrupaMuntoasaDto {
    private Long id;
    private String titlu;
    private byte[] pozaHarta;
}
