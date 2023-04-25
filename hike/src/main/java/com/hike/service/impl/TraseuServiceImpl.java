package com.hike.service.impl;

import com.hike.dto.TraseuDto;
import com.hike.exception.ObjectNotFoundException;
import com.hike.mapper.TraseuMapper;
import com.hike.models.*;
import com.hike.repository.TraseuRepository;
import com.hike.service.GrupaMuntoasaService;
import com.hike.service.MarcajService;
import com.hike.service.TraseuCommentService;
import com.hike.service.TraseuService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class TraseuServiceImpl implements TraseuService {
    private TraseuRepository traseuRepository;
    private GrupaMuntoasaService grupaMuntoasaService;
    private MarcajService marcajService;
    private TraseuCommentService traseuCommentService;

    @Autowired
    public TraseuServiceImpl(TraseuRepository traseuRepository, GrupaMuntoasaService grupaMuntoasaService, MarcajService marcajService, TraseuCommentService traseuCommentService) {
        this.traseuRepository = traseuRepository;
        this.grupaMuntoasaService = grupaMuntoasaService;
        this.marcajService = marcajService;
        this.traseuCommentService = traseuCommentService;
    }

    @Override
    public void save(TraseuDto traseuDto) {
        Traseu traseu = TraseuMapper.mapToTraseu(traseuDto);

        Optional<GrupaMuntoasa> grupaMuntoasa = grupaMuntoasaService.findGrupaMuntoasa(traseuDto.getGrupaMuntoasaId());
        if(grupaMuntoasa.isPresent()){
            traseu.setGrupaMuntoasa(grupaMuntoasa.get());
        }
        Optional<Marcaj> marcaj = marcajService.findById(traseuDto.getMarcajId());
        if(marcaj.isPresent()){
            traseu.setMarcaj(marcaj.get());
        }

        traseuRepository.save(traseu);
    }

    @Override
    public Page<Traseu> getAllTraseeAprobate(Pageable pageable) {
        return traseuRepository.getAllByAprobat(true, pageable);
    }

    @Override
    public Page<Traseu> getAllTraseeNeaprobate(Pageable pageable) {
        return traseuRepository.getAllByAprobat(false, pageable);
    }

    @Override
    public Traseu getTraseuByTitlu(String titlu) {
        return traseuRepository.getTraseuByTitlu(titlu);
    }

    @Override
    public Optional<Traseu> getTraseuById(Long id) {
        return traseuRepository.findById(id);
    }

    @Override
    public List<Traseu> findAll() {
        return traseuRepository.findAll();
    }

    @Override
    public Page<Traseu> getAllByUser(UserEntity user, Pageable pageable) {
        return traseuRepository.getAllByUser(user, pageable);
    }

    @Override
    public int countTraseeAprobateByUser(UserEntity user) {
        return traseuRepository.countAllByUserAndAprobat(user, true);
    }

    @Override
    public void save(Traseu traseu) {
        traseuRepository.save(traseu);
    }

    @Override
    public void respinge(Traseu traseu) {
        traseuRepository.delete(traseu);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        try {
            Traseu traseu = traseuRepository.findById(id)
                    .orElseThrow(() -> new ObjectNotFoundException("Nu s-a gasit traseul cu ID-ul " + id));

            List<TraseuComment> comentarii = traseuCommentService.getAllCommentsByTraseu(traseu);
            for (TraseuComment comentariu : comentarii) {
                traseuCommentService.delete(comentariu.getId());
            }
            traseu.getUsersParcurs().forEach(u -> u.getTraseeParcurse().remove(traseu));
            traseu.setUsersParcurs(Collections.emptyList());

        } catch (Exception ex) {
            throw new RuntimeException("Nu s-a putut sterge postarea cu ID-ul " + id, ex);
        }

        traseuRepository.deleteById(id);
    }

    @Override
    public Page<Traseu> findAll(Specification<Traseu> spec, Pageable pageable) {
        return traseuRepository.findAll(spec, pageable);
    }
}
