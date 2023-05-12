package com.hike.service.impl;

import com.hike.models.Stire;
import com.hike.models.Traseu;
import com.hike.repository.StireRepository;
import com.hike.service.StireService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StireServiceImpl implements StireService {
    private StireRepository stireRepository;

    public StireServiceImpl(StireRepository stireRepository) {
        this.stireRepository = stireRepository;
    }

    @Override
    public List<Stire> findStiriByTraseu(Traseu traseu) {
        return stireRepository.findAllByTraseu(traseu);
    }
}
