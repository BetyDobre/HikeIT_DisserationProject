package com.hike.service.impl;

import com.hike.controller.SalvamontController;
import com.hike.dto.SalvamontDto;
import com.hike.models.GrupaMuntoasa;
import com.hike.models.Marcaj;
import com.hike.models.Salvamont;
import com.hike.repository.SalvamontRepository;
import com.hike.service.GrupaMuntoasaService;
import com.hike.service.SalvamontService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SalvamontServiceImpl implements SalvamontService {
    private SalvamontRepository salvamontRepository;

    @Autowired
    public SalvamontServiceImpl(SalvamontRepository salvamontRepository) {
        this.salvamontRepository = salvamontRepository;
    }

    @Override
    public List<Salvamont> getAll() {
        return salvamontRepository.findAll();
    }

    @Override
    public Salvamont findByGrupa(GrupaMuntoasa grupaMuntoasa) {
        return salvamontRepository.findSalvamontByGrupaMuntoasa(grupaMuntoasa);
    }

    @Override
    public void save(SalvamontDto salvamontDto) {
        Salvamont salvamont = new Salvamont();
        salvamont.setId(salvamontDto.getId());
        salvamont.setGrupaMuntoasa(salvamontDto.getGrupaMuntoasa());
        salvamont.setTitlu("Salvamont " + salvamontDto.getGrupaMuntoasa().getTitlu());
        salvamont.setTelefon(salvamontDto.getTelefon());

        salvamontRepository.save(salvamont);
    }

    @Override
    public void delete(Long id) {
        Optional<Salvamont> salvamont = salvamontRepository.findById(id);
        if(salvamont.isPresent()){
            salvamontRepository.delete(salvamont.get());
        };
    }

    @Override
    public Salvamont findById(Long id) {
        return salvamontRepository.findById(id).get();
    }
}
