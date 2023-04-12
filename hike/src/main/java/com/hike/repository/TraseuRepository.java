package com.hike.repository;

import com.hike.models.Traseu;
import com.hike.models.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TraseuRepository extends JpaRepository<Traseu, Long> {
    Page<Traseu> getAllByAprobat(boolean aprobat, Pageable pageable);
    Traseu getTraseuByTitlu(String titlu);
    Optional<Traseu> findById(Long id);
    Page<Traseu> getAllByUser(UserEntity user, Pageable pageable);
}
