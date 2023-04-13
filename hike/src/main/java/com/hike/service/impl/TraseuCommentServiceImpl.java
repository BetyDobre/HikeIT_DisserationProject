package com.hike.service.impl;

import com.hike.dto.TraseuCommentDto;
import com.hike.models.Traseu;
import com.hike.models.TraseuComment;
import com.hike.models.UserEntity;
import com.hike.repository.TraseuCommentRepository;
import com.hike.service.TraseuCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TraseuCommentServiceImpl implements TraseuCommentService {
    private TraseuCommentRepository traseuCommentRepository;

    @Autowired
    public TraseuCommentServiceImpl(TraseuCommentRepository traseuCommentRepository) {
        this.traseuCommentRepository = traseuCommentRepository;
    }

    @Override
    public Page<TraseuComment> getAllCommentsByTraseu(Traseu traseu, Pageable pageable) {
        return traseuCommentRepository.findAllByTraseu(traseu, pageable);
    }

    @Override
    public List<TraseuComment> getAllCommentsByTraseu(Traseu traseu) {
        return  traseuCommentRepository.findAllByTraseu(traseu);
    }

    @Override
    public int noOfCommentsByTraseu(Long id) {
        return traseuCommentRepository.noOfCommentsByTraseu(id);
    }

    @Override
    public void save(TraseuCommentDto traseuCommentDto) {
        TraseuComment traseuComment = TraseuComment.builder()
                .text(traseuCommentDto.getText())
                .createdOn(traseuCommentDto.getCreatedOn())
                .traseu(traseuCommentDto.getTraseu())
                .user(traseuCommentDto.getUser())
                .build();

        traseuCommentRepository.save(traseuComment);
    }

    @Override
    public void delete(Long commId) {
        traseuCommentRepository.deleteById(commId);
    }

    @Override
    public int countAllByUser(UserEntity user) {
        return traseuCommentRepository.countAllByUser(user);
    }
}
