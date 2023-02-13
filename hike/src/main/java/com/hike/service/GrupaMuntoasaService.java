package com.hike.service;

import com.hike.dto.GrupaMuntoasaDto;
import com.hike.models.GrupaMuntoasa;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface GrupaMuntoasaService {
    List<GrupaMuntoasaDto> findAllGroups();
    Optional<GrupaMuntoasa> findGrupaMuntoasa(Long id);
}
