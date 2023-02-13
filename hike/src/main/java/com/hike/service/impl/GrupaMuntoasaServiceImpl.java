package com.hike.service.impl;

import com.hike.dto.GrupaMuntoasaDto;
import com.hike.models.GrupaMuntoasa;
import com.hike.repository.GrupaMuntoasaRepository;
import com.hike.service.GrupaMuntoasaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.hike.mapper.GrupaMuntoasaMapper.mapToGrupaDto;

@Service
public class GrupaMuntoasaServiceImpl implements GrupaMuntoasaService {

    private GrupaMuntoasaRepository grupaMuntoasaRepository;

    @Autowired
    public GrupaMuntoasaServiceImpl(GrupaMuntoasaRepository grupaMuntoasaRepository){
        this.grupaMuntoasaRepository = grupaMuntoasaRepository;
    }

    @Override
    public List<GrupaMuntoasaDto> findAllGroups() {
        List<GrupaMuntoasa> grupe = grupaMuntoasaRepository.findAll();
        return grupe.stream().map((g) -> mapToGrupaDto(g)).collect(Collectors.toList());
    }

    public Optional<GrupaMuntoasa> findGrupaMuntoasa(Long id) {
        return grupaMuntoasaRepository.findById(id);
    }
}
