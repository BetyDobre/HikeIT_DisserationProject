package com.hike.service;

import com.hike.dto.TraseuDto;
import com.hike.models.Traseu;
import com.hike.models.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface TraseuService {
    void save(TraseuDto traseuDto);
    Page<Traseu> getAllTraseeAprobate(Pageable pageable);
    Page<Traseu> getAllTraseeNeaprobate(Pageable pageable);
    Traseu getTraseuByTitlu(String titlu);
    Optional<Traseu> getTraseuById(Long id);
    List<Traseu> findAll();
    Page<Traseu> getAllByUser(UserEntity user, Pageable pageable);
    int countTraseeAprobateByUser(UserEntity user);

    void save(Traseu traseu);

    void respinge(Traseu traseu);

    void delete(Long id);

    Page<Traseu> findAll(Specification<Traseu> spec, Pageable pageable);
}
