package com.hike.service.impl;

import com.hike.repository.TraseuRepository;
import com.hike.service.TraseuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TraseuServiceImpl implements TraseuService {
    private TraseuRepository traseuRepository;

    @Autowired
    public TraseuServiceImpl(TraseuRepository traseuRepository) {
        this.traseuRepository = traseuRepository;
    }
}
