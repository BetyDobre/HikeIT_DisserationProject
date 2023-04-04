package com.hike.service.impl;

import com.hike.dto.TraseuDto;
import com.hike.mapper.TraseuMapper;
import com.hike.models.GrupaMuntoasa;
import com.hike.models.Traseu;
import com.hike.repository.TraseuRepository;
import com.hike.service.GrupaMuntoasaService;
import com.hike.service.TraseuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TraseuServiceImpl implements TraseuService {
    private TraseuRepository traseuRepository;
    private GrupaMuntoasaService grupaMuntoasaService;

    @Autowired
    public TraseuServiceImpl(TraseuRepository traseuRepository, GrupaMuntoasaService grupaMuntoasaService) {
        this.traseuRepository = traseuRepository;
        this.grupaMuntoasaService = grupaMuntoasaService;
    }

    @Override
    public void save(TraseuDto traseuDto) {
        Traseu traseu = TraseuMapper.mapToTraseu(traseuDto);

        Optional<GrupaMuntoasa> grupaMuntoasa = grupaMuntoasaService.findGrupaMuntoasa(traseuDto.getGrupaMuntoasaId());
        if(grupaMuntoasa.isPresent()){
            traseu.setGrupaMuntoasa(grupaMuntoasa.get());
        }

        traseuRepository.save(traseu);
    }
}
