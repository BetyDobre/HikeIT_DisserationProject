package com.hike.mapper;

import com.hike.dto.SalvamontDto;
import com.hike.models.Salvamont;

public class SalvamontMapper {
    public static Salvamont mapToSalvamont(SalvamontDto salvamontDto){
        Salvamont salvamont = Salvamont.builder()
                .id(salvamontDto.getId())
                .titlu(salvamontDto.getTitlu())
                .telefon(salvamontDto.getTelefon())
                .grupaMuntoasa(salvamontDto.getGrupaMuntoasa())
                .build();
        return salvamont;
    }

    public static SalvamontDto mapToSalvamontDto(Salvamont salvamont){
        SalvamontDto salvamontDto = SalvamontDto.builder()
                .id(salvamont.getId())
                .titlu(salvamont.getTitlu())
                .telefon(salvamont.getTelefon())
                .grupaMuntoasa(salvamont.getGrupaMuntoasa())
                .grupaMuntoasaId(salvamont.getGrupaMuntoasa().getId())
                .build();
        return salvamontDto;
    }
}