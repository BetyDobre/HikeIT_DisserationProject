package com.hike.repository;

import com.hike.models.Marcaj;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MarcajRepository extends JpaRepository<Marcaj, Long> {
    Marcaj findByTitlu(String titlu);
    Optional<Marcaj> findById(Long id);
}
