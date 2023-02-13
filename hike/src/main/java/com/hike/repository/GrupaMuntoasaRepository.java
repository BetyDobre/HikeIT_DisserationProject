package com.hike.repository;

import com.hike.models.GrupaMuntoasa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface GrupaMuntoasaRepository extends JpaRepository<GrupaMuntoasa, Long> {
    Optional<GrupaMuntoasa> findById(Long id);
}
