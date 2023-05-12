package com.hike.service;

import com.hike.models.Stire;
import com.hike.models.Traseu;

import java.util.List;

public interface StireService {
    List<Stire> findStiriByTraseu(Traseu traseu);
}
