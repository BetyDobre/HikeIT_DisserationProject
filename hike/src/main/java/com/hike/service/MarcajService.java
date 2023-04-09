package com.hike.service;

import com.hike.dto.MarcajDto;
import com.hike.models.Marcaj;

import java.util.List;
import java.util.Optional;

public interface MarcajService {
    Marcaj findByTitlu(String titlu);
    void save(MarcajDto marcajDto);
    List<Marcaj> getAllMarcaje();
    Optional<Marcaj> findById(Long id);
    void delete(Long id);
}
