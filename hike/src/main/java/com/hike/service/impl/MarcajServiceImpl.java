package com.hike.service.impl;

import com.hike.dto.MarcajDto;
import com.hike.exception.ObjectNotFoundException;
import com.hike.models.Marcaj;
import com.hike.repository.MarcajRepository;
import com.hike.service.MarcajService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MarcajServiceImpl implements MarcajService {

    private MarcajRepository marcajRepository;

    @Autowired
    public MarcajServiceImpl(MarcajRepository marcajRepository) {
        this.marcajRepository = marcajRepository;
    }

    @Override
    public Marcaj findByTitlu(String titlu) {
        return marcajRepository.findByTitlu(titlu);
    }

    @Override
    public void save(MarcajDto marcajDto) {
        Marcaj marcaj = new Marcaj();
        marcaj.setId(marcajDto.getId());
        marcaj.setTitlu(marcajDto.getTitlu());
        marcaj.setMarcaj(marcajDto.getMarcaj());

        marcajRepository.save(marcaj);
    }

    @Override
    public List<Marcaj> getAllMarcaje() {
        return marcajRepository.findAll();
    }

    @Override
    public Optional<Marcaj> findById(Long id) {
        return marcajRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        Optional<Marcaj> marcaj = marcajRepository.findById(id);
        if(marcaj.isPresent()){
            marcajRepository.delete(marcaj.get());
        };
    }
}
