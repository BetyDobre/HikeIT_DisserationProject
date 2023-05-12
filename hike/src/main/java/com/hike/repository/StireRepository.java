package com.hike.repository;


import com.hike.models.Stire;
import com.hike.models.Traseu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StireRepository extends JpaRepository<Stire, Long> {
    List<Stire> findAllByTraseu(Traseu traseu);
}
