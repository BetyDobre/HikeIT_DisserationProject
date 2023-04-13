package com.hike.service;

import com.hike.dto.TraseuCommentDto;
import com.hike.models.Traseu;
import com.hike.models.TraseuComment;
import com.hike.models.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TraseuCommentService {
    Page<TraseuComment> getAllCommentsByTraseu(Traseu traseu, Pageable pageable);

    List<TraseuComment> getAllCommentsByTraseu(Traseu traseu);

    int noOfCommentsByTraseu(Long id);

    void save(TraseuCommentDto traseuCommentDto);

    void delete(Long commId);

    int countAllByUser(UserEntity user);
}
