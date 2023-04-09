package com.hike.service.impl;

import com.hike.dto.TraseuDto;
import com.hike.mapper.TraseuMapper;
import com.hike.models.GrupaMuntoasa;
import com.hike.models.Marcaj;
import com.hike.models.Traseu;
import com.hike.repository.TraseuRepository;
import com.hike.service.GrupaMuntoasaService;
import com.hike.service.MarcajService;
import com.hike.service.TraseuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TraseuServiceImpl implements TraseuService {
    private TraseuRepository traseuRepository;
    private GrupaMuntoasaService grupaMuntoasaService;
    private MarcajService marcajService;

    @Autowired
    public TraseuServiceImpl(TraseuRepository traseuRepository, GrupaMuntoasaService grupaMuntoasaService, MarcajService marcajService) {
        this.traseuRepository = traseuRepository;
        this.grupaMuntoasaService = grupaMuntoasaService;
        this.marcajService = marcajService;
    }

    @Override
    public void save(TraseuDto traseuDto) {
        Traseu traseu = TraseuMapper.mapToTraseu(traseuDto);

        Optional<GrupaMuntoasa> grupaMuntoasa = grupaMuntoasaService.findGrupaMuntoasa(traseuDto.getGrupaMuntoasaId());
        if(grupaMuntoasa.isPresent()){
            traseu.setGrupaMuntoasa(grupaMuntoasa.get());
        }
        Optional<Marcaj> marcaj = marcajService.findById(traseuDto.getMarcajId());
        if(marcaj.isPresent()){
            traseu.setMarcaj(marcaj.get());
        }

        traseuRepository.save(traseu);
    }

    @Override
    public Page<Traseu> getAllTraseeAprobate(Pageable pageable) {
        return traseuRepository.getAllByAprobat(true, pageable);
    }

    @Override
    public Traseu getTraseuByTitlu(String titlu) {
        return traseuRepository.getTraseuByTitlu(titlu);
    }

    @Override
    public Optional<Traseu> getTraseuById(Long id) {
        return traseuRepository.findById(id);
    }
}
