package com.hike.service;

import com.hike.dto.SalvamontDto;
import com.hike.models.GrupaMuntoasa;
import com.hike.models.Marcaj;
import com.hike.models.Salvamont;

import java.util.List;

public interface SalvamontService {
    List<Salvamont> getAll();
    Salvamont findByGrupa(GrupaMuntoasa grupaMuntoasa);

    void save(SalvamontDto salvamontDto);
    void delete(Long id);

    Salvamont findById(Long id);
}
