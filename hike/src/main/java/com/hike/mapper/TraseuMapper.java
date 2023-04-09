package com.hike.mapper;

import com.hike.dto.TraseuDto;
import com.hike.models.Traseu;

public class TraseuMapper {
    public static Traseu mapToTraseu(TraseuDto traseuDto){
        Traseu traseu = Traseu.builder()
                .id(traseuDto.getId())
                .titlu(traseuDto.getTitlu())
                .descriere(traseuDto.getDescriere())
                .dificultate(traseuDto.getDificultate())
                .sezon(traseuDto.getSezon())
                .coborare(traseuDto.getCoborare())
                .urcare(traseuDto.getUrcare())
                .durataMinima(traseuDto.getDurataMinima())
                .durataMaxima(traseuDto.getDurataMaxima())
                .aprobat(traseuDto.isAprobat())
                .distanta(traseuDto.getDistanta())
                .grupaMuntoasa(traseuDto.getGrupaMuntoasa())
                .user(traseuDto.getUser())
                .pozeTraseu(traseuDto.getPozeTraseu())
                .punctPlecare(traseuDto.getPunctPlecare())
                .punctSosire(traseuDto.getPunctSosire())
                .marcaj(traseuDto.getMarcaj())
                .build();
        return traseu;
    }

    public static TraseuDto mapToTraseuDto(Traseu traseu){
        TraseuDto traseuDto = TraseuDto.builder()
                .id(traseu.getId())
                .titlu(traseu.getTitlu())
                .descriere(traseu.getDescriere())
                .dificultate(traseu.getDificultate())
                .sezon(traseu.getSezon())
                .coborare(traseu.getCoborare())
                .urcare(traseu.getUrcare())
                .durataMaxima(traseu.getDurataMaxima())
                .durataMinima(traseu.getDurataMinima())
                .aprobat(traseu.isAprobat())
                .distanta(traseu.getDistanta())
                .grupaMuntoasa(traseu.getGrupaMuntoasa())
                .user(traseu.getUser())
                .pozeTraseu(traseu.getPozeTraseu())
                .punctPlecare(traseu.getPunctPlecare())
                .punctSosire(traseu.getPunctSosire())
                .marcaj(traseu.getMarcaj())
                .build();
        return traseuDto;
    }
}
