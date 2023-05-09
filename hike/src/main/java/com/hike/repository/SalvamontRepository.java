package com.hike.repository;

import com.hike.models.GrupaMuntoasa;
import com.hike.models.Salvamont;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SalvamontRepository extends JpaRepository<Salvamont, Long> {
    Salvamont findByTitlu(String titlu);
    Optional<Salvamont> findById(Long id);

    Salvamont findSalvamontByGrupaMuntoasa(GrupaMuntoasa grupaMuntoasa);
}

