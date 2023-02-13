package com.hike.mapper;

import com.hike.dto.GrupaMuntoasaDto;
import com.hike.models.GrupaMuntoasa;

public class GrupaMuntoasaMapper {
    public static GrupaMuntoasa mapToGrupa(GrupaMuntoasaDto grupaMuntoasaDto){
        GrupaMuntoasa grupaMuntoasa = GrupaMuntoasa.builder()
                .id(grupaMuntoasaDto.getId())
                .titlu(grupaMuntoasaDto.getTitlu())
                .pozaHarta(grupaMuntoasaDto.getPozaHarta())
                .build();
        return grupaMuntoasa;
    }

    public static GrupaMuntoasaDto mapToGrupaDto(GrupaMuntoasa grupaMuntoasa){
        GrupaMuntoasaDto grupaMuntoasaDto = GrupaMuntoasaDto.builder()
                .id(grupaMuntoasa.getId())
                .titlu(grupaMuntoasa.getTitlu())
                .pozaHarta(grupaMuntoasa.getPozaHarta())
                .build();
        return grupaMuntoasaDto;
    }
}
