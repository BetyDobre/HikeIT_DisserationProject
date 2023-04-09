package com.hike.service;

import com.hike.dto.TraseuDto;
import com.hike.models.Traseu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TraseuService {
    void save(TraseuDto traseuDto);
    Page<Traseu> getAllTraseeAprobate(Pageable pageable);
    Traseu getTraseuByTitlu(String titlu);
    Optional<Traseu> getTraseuById(Long id);
}
